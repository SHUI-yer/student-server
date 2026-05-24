package com.suiye.studentserver.service.impl;

import com.suiye.studentserver.entity.Course;
import com.suiye.studentserver.entity.Score;
import com.suiye.studentserver.entity.ScoreImportDTO;
import com.suiye.studentserver.entity.ScoreVO;
import com.suiye.studentserver.entity.Student;
import com.suiye.studentserver.mapper.CourseMapper;
import com.suiye.studentserver.mapper.ScoreMapper;
import com.suiye.studentserver.mapper.StudentMapper;
import com.suiye.studentserver.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Map<String, Object> getScorePage(int pageNum, int pageSize, String keyword) {
        int offset = (pageNum - 1) * pageSize;
        
        List<ScoreVO> list = scoreMapper.findByPage(offset, pageSize, keyword);
        int total = scoreMapper.count(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @Override
    public int saveScore(Score score) {
        // 自动计算成绩：百分制成绩 * 学分 / 100
        Course course = courseMapper.findById(score.getCourseId());
        if (course != null && score.getOriginalScore() != null) {
            double calculatedScore = score.getOriginalScore() * course.getCredit() / 100.0;
            score.setScore(Math.round(calculatedScore * 100.0) / 100.0); // 保留两位小数
        }

        if (score.getId() == null) {
            Score exist = scoreMapper.findByStudentAndCourse(score.getStudentId(), score.getCourseId());
            if (exist != null) {
                throw new RuntimeException("该学生已存在该课程的成绩，请直接修改！");
            }
            return scoreMapper.insert(score);
        } else {
            // Edit mode, verify if changing to an existing combination
            Score exist = scoreMapper.findByStudentAndCourse(score.getStudentId(), score.getCourseId());
            if (exist != null && !exist.getId().equals(score.getId())) {
                throw new RuntimeException("该学生已存在该课程的成绩，无法修改为重复记录！");
            }
            return scoreMapper.update(score);
        }
    }

    @Override
    public int deleteScore(Integer id) {
        return scoreMapper.delete(id);
    }

    @Override
    public List<ScoreVO> getAllScores() {
        return scoreMapper.findAll();
    }

    @Override
    @Transactional
    public void batchImport(List<ScoreImportDTO> scores) {
        for (ScoreImportDTO dto : scores) {
            Student student = studentMapper.findByStudentNumber(dto.getStudentNumber());
            Course course = courseMapper.findByCourseNumber(dto.getCourseNumber());
            
            if (student != null && course != null) {
                Score score = new Score();
                score.setStudentId(student.getId());
                score.setCourseId(course.getId());
                score.setOriginalScore(dto.getOriginalScore());
                
                // 检查是否已存在
                Score exist = scoreMapper.findByStudentAndCourse(student.getId(), course.getId());
                if (exist != null) {
                    score.setId(exist.getId());
                    saveScore(score); // 更新
                } else {
                    saveScore(score); // 新增
                }
            }
        }
    }
}