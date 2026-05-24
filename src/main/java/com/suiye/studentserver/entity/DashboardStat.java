package com.suiye.studentserver.entity;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardStat {
    private long studentCount;
    private long courseCount;
    private double passRate;
    private double avgCredit;
    private List<Map<String, Object>> majorDistribution; // [{name: '计算机', value: 10}]
    private List<ScoreVO> recentActivities;
    
    // 历年趋势数据
    private List<String> trendPeriods; // ['2023秋', '2024春']
    private List<Double> trendAvgScores;
    private List<Double> trendPassRates;

    // 课程难度雷达图数据
    private List<Map<String, Object>> courseDifficulty; // {name: '高数', value: [80, 90, ...]}
}
