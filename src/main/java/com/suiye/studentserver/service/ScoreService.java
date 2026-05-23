package com.suiye.studentserver.service;

import com.suiye.studentserver.entity.Score;

import java.util.Map;

public interface ScoreService {
    Map<String, Object> getScorePage(int pageNum, int pageSize, String keyword);
    int saveScore(Score score);
    int deleteScore(Integer id);
}