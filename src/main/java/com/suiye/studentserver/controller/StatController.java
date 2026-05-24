package com.suiye.studentserver.controller;

import com.suiye.studentserver.common.Result;
import com.suiye.studentserver.entity.DashboardStat;
import com.suiye.studentserver.entity.ScoreVO;
import com.suiye.studentserver.mapper.CourseMapper;
import com.suiye.studentserver.mapper.ScoreMapper;
import com.suiye.studentserver.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stat")
@CrossOrigin
public class StatController {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private ScoreMapper scoreMapper;

    @GetMapping("/dashboard")
    public Result<DashboardStat> getDashboardStat() {
        DashboardStat stat = new DashboardStat();
        
        // 1. 基础统计
        long studentCount = studentMapper.count();
        long courseCount = courseMapper.count();
        stat.setStudentCount(studentCount);
        stat.setCourseCount(courseCount);

        // 2. 成绩相关统计
        List<ScoreVO> allScores = scoreMapper.findAll();
        if (!allScores.isEmpty()) {
            // 及格率基于百分制成绩 (originalScore >= 60)
            long passCount = allScores.stream().filter(s -> s.getOriginalScore() != null && s.getOriginalScore() >= 60).count();
            stat.setPassRate(Math.round((double) passCount / allScores.size() * 1000.0) / 10.0);
            
            // 平均学分基于折算后的 score
            double totalCreditScore = allScores.stream().mapToDouble(s -> s.getScore() != null ? s.getScore() : 0.0).sum();
            stat.setAvgCredit(Math.round(totalCreditScore / studentCount * 10.0) / 10.0);
        }

        // 3. 专业分布
        List<com.suiye.studentserver.entity.Student> allStudents = studentMapper.findAll();
        Map<String, Long> majorMap = allStudents.stream()
                .filter(s -> s.getMajor() != null)
                .collect(Collectors.groupingBy(com.suiye.studentserver.entity.Student::getMajor, Collectors.counting()));
        
        List<Map<String, Object>> majorDist = new ArrayList<>();
        majorMap.forEach((major, count) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", major);
            item.put("value", count);
            majorDist.add(item);
        });
        stat.setMajorDistribution(majorDist);

        // 4. 最近动态 (取最后5条)
        List<ScoreVO> recent = scoreMapper.findByPage(0, 5, null);
        stat.setRecentActivities(recent);

        // 5. 历年成绩趋势 (按学期聚合)
        Map<String, List<ScoreVO>> semesterMap = allScores.stream()
                .filter(s -> s.getCourseName() != null)
                .collect(Collectors.groupingBy(s -> s.getCourseNumber().substring(0, 4), Collectors.toList())); // 简单按课程编号前4位模拟年份

        List<String> periods = semesterMap.keySet().stream().sorted().collect(Collectors.toList());
        List<Double> avgScores = new ArrayList<>();
        List<Double> passRates = new ArrayList<>();

        for (String p : periods) {
            List<ScoreVO> scores = semesterMap.get(p);
            // 平均分基于百分制成绩
            double avg = scores.stream().filter(s -> s.getOriginalScore() != null).mapToDouble(ScoreVO::getOriginalScore).average().orElse(0.0);
            // 及格率基于百分制成绩
            long pass = scores.stream().filter(s -> s.getOriginalScore() != null && s.getOriginalScore() >= 60).count();
            avgScores.add(Math.round(avg * 10.0) / 10.0);
            passRates.add(Math.round((double) pass / scores.size() * 1000.0) / 10.0);
        }
        stat.setTrendPeriods(periods);
        stat.setTrendAvgScores(avgScores);
        stat.setTrendPassRates(passRates);

        // 6. 课程难度评估 (雷达图算法)
        Map<String, List<ScoreVO>> courseMap = allScores.stream()
                .collect(Collectors.groupingBy(ScoreVO::getCourseName, Collectors.toList()));

        List<Map<String, Object>> difficultyList = new ArrayList<>();
        courseMap.forEach((courseName, scores) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", courseName);
            
            // 计算维度：1.平均分反比(难度) 2.及格率反比(风险) 3.低分率 4.分差波动(挂科风险) 5.学分权重
            // 所有计算基于百分制成绩 originalScore
            double avg = scores.stream().filter(s -> s.getOriginalScore() != null).mapToDouble(ScoreVO::getOriginalScore).average().orElse(0.0);
            long pass = scores.stream().filter(s -> s.getOriginalScore() != null && s.getOriginalScore() >= 60).count();
            double passRate = (double) pass / scores.size();
            long lowScoreCount = scores.stream().filter(s -> s.getOriginalScore() != null && s.getOriginalScore() < 40).count();
            
            double d1 = Math.max(0, 100 - avg); // 理论难度
            double d2 = Math.max(0, (1 - passRate) * 100); // 挂科风险
            double d3 = (double) lowScoreCount / scores.size() * 100; // 极端低分率
            double d4 = scores.size() > 1 ? calculateSD(scores) * 2 : 50; // 波动性
            double d5 = scores.get(0).getCredit() * 10; // 学分压力
            
            item.put("value", java.util.Arrays.asList(
                Math.round(d1), Math.round(d2), Math.round(passRate * 100), Math.round(d3), Math.round(d5)
            ));
            difficultyList.add(item);
        });
        stat.setCourseDifficulty(difficultyList.stream().limit(5).collect(Collectors.toList())); // 取前5门课展示

        return Result.success(stat);
    }

    private double calculateSD(List<ScoreVO> scores) {
        List<ScoreVO> validScores = scores.stream()
                .filter(s -> s.getOriginalScore() != null)
                .collect(Collectors.toList());
        if (validScores.isEmpty()) return 0;
        
        double avg = validScores.stream().mapToDouble(ScoreVO::getOriginalScore).average().orElse(0.0);
        double sum = 0;
        for (ScoreVO s : validScores) {
            sum += Math.pow(s.getOriginalScore() - avg, 2);
        }
        return Math.sqrt(sum / validScores.size());
    }
}
