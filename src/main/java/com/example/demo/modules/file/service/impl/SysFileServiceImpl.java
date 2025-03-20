package com.example.demo.modules.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.modules.file.entity.FileDTO;
import com.example.demo.modules.file.entity.SysFile;
import com.example.demo.modules.file.service.SysFileService;
import com.example.demo.modules.file.mapper.SysFileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.UTF_8;


/**
* @author Administrator
* @description 针对表【sys_file】的数据库操作Service实现
* @createDate 2025-03-20 16:34:20
*/
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile>
    implements SysFileService{

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public List<FileDTO> getFileList() {
        List<SysFile> files = this.list(new LambdaQueryWrapper<SysFile>()
                .orderByDesc(SysFile::getCreateTime));

        return files.stream().map(file -> {
            FileDTO dto = new FileDTO();
            dto.setId(file.getId());
            dto.setName(file.getName());
            dto.setCreateTime(file.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void downloadFile(String id, HttpServletResponse response) throws  UnsupportedEncodingException{
        SysFile file = this.getById(id);
        if (file == null) {
            throw new RuntimeException("文件不存在");
        }

        String filePath = uploadPath + File.separator + file.getPath();
        File downloadFile = new File(filePath);
        if (!downloadFile.exists()) {
            throw new RuntimeException("文件不存在");
        }

        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(file.getName(), "UTF-8"));

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downloadFile));
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int i;
            while ((i = bis.read(buffer)) != -1) {
                os.write(buffer, 0, i);
            }
        } catch (IOException e) {
            log.error("文件下载失败", e);
            throw new RuntimeException("文件下载失败");
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空");
        }

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() +
                originalFilename.substring(originalFilename.lastIndexOf("."));

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String filePath = today + File.separator + fileName;

        File dest = new File(uploadPath + File.separator + filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);

            SysFile sysFile = new SysFile();
            sysFile.setName(originalFilename);
            sysFile.setPath(filePath);
            sysFile.setSize(file.getSize());
            sysFile.setType(file.getContentType());
            this.save(sysFile);

            return sysFile.getId();
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        }
    }

    @Override
    public String getPreviewUrl(String id) throws  UnsupportedEncodingException{
        SysFile file = this.getById(id);
        if (file == null) {
            throw new RuntimeException("文件不存在");
        }

        // 根据文件类型返回不同的预览URL
        String fileType = file.getType().toLowerCase();
        if (fileType.endsWith("pdf")) {
            // PDF 直接返回文件URL
            return "/api/file/download/" + id;
        } else if (fileType.endsWith("doc") || fileType.endsWith("docx") ||
                fileType.endsWith("xls") || fileType.endsWith("xlsx")) {
            // Office 文件可以使用第三方服务预览，比如：
            // 1. 微软 Office Online
            // 2. 永中 DCS
            // 3. 自己部署的 LibreOffice
            return generateOfficePreviewUrl(file);
        } else {
            throw new RuntimeException("不支持该文件类型的预览");
        }
    }

    private String generateOfficePreviewUrl(SysFile file) throws  UnsupportedEncodingException{
        // 这里以永中 DCS 为例
        String dcsServer = "http://your-dcs-server";
        String sourceUrl = URLEncoder.encode("/api/file/download/" + file.getId(), UTF_8);
        return dcsServer + "/view/url?url=" + sourceUrl;
    }
}




