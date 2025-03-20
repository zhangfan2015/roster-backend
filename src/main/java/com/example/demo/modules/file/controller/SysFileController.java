package com.example.demo.modules.file.controller;

import com.example.demo.common.Result;
import com.example.demo.modules.file.entity.FileDTO;
import com.example.demo.modules.file.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
public class SysFileController {


    @Autowired
    private SysFileService fileService;

    @GetMapping("/list")
    public Result<List<FileDTO>> getFileList() {
        try {
            List<FileDTO> files = fileService.getFileList();
            return Result.ok(files);
        } catch (Exception e) {
//            log.error("获取文件列表失败", e);
            return Result.error("获取文件列表失败");
        }
    }

    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable String id, HttpServletResponse response) {
        try {
            fileService.downloadFile(id, response);
        } catch (Exception e) {
//            log.error("文件下载失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileService.uploadFile(file);
            return Result.ok(fileId);
        } catch (Exception e) {
//            log.error("文件上传失败", e);
            return Result.error("文件上传失败");
        }
    }

    @GetMapping("/preview/{id}")
    public Result<Map<String, String>> previewFile(@PathVariable String id) {
        try {
            String previewUrl = fileService.getPreviewUrl(id);
            Map<String, String> result = new HashMap<>();
            result.put("url", previewUrl);
            return Result.ok(result);
        } catch (Exception e) {
//            log.error("获取文件预览失败", e);
            return Result.error("获取文件预览失败");
        }
    }

}
