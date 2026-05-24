package com.suiye.studentserver.controller;

import com.suiye.studentserver.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@CrossOrigin
public class FileController {

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(400, "上传文件为空");
        }

        try {
            // 获取项目根目录下的 uploads 文件夹
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // 目录不存在则自动创建
            }

            // 生成唯一的文件名，防止文件名冲突被覆盖
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = UUID.randomUUID().toString() + extension;

            // 将文件保存至目标路径
            File dest = new File(uploadDir + newFilename);
            file.transferTo(dest);

            // 返回文件的相对访问路径
            String fileUrl = "/uploads/" + newFilename;
            return Result.success(fileUrl);
            
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(500, "文件上传失败：" + e.getMessage());
        }
    }
}