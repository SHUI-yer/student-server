package com.suiye.studentserver.service;

import com.suiye.studentserver.entity.Score;

import java.util.Map;

public interface ScoreService {
    Map<String, Object> getScorePage(int pageNum, int pageSize, String keyword);
    int saveScore(Score score);
    int deleteScore(Integer id);
    java.util.List<com.suiye.studentserver.entity.ScoreVO> getAllScores();
    java.util.List<com.suiye.studentserver.entity.ScoreVO> getFilteredScores(String studentNumber, String courseNumber, String major);
    void batchImport(java.util.List<com.suiye.studentserver.entity.ScoreImportDTO> scores);
}