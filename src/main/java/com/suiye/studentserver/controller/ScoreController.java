package com.suiye.studentserver.controller;

import com.suiye.studentserver.common.Result;
import com.suiye.studentserver.entity.Score;
import com.suiye.studentserver.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/score")
@CrossOrigin
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/page")
    public Result<Map<String, Object>> getScorePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Map<String, Object> pageData = scoreService.getScorePage(pageNum, pageSize, keyword);
        return Result.success(pageData);
    }

    @PostMapping("/save")
    public Result<String> saveScore(@RequestBody @Validated Score score) {
        scoreService.saveScore(score);
        return Result.success("保存成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteScore(@PathVariable Integer id) {
        scoreService.deleteScore(id);
        return Result.success("删除成功");
    }
}