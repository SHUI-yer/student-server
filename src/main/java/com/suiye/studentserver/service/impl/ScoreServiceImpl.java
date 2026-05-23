package com.suiye.studentserver.service.impl;

import com.suiye.studentserver.entity.Score;
import com.suiye.studentserver.entity.ScoreVO;
import com.suiye.studentserver.mapper.ScoreMapper;
import com.suiye.studentserver.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

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
}