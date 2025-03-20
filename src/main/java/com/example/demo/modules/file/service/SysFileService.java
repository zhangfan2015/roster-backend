package com.example.demo.modules.file.service;

import com.example.demo.modules.file.entity.FileDTO;
import com.example.demo.modules.file.entity.SysFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_file】的数据库操作Service
* @createDate 2025-03-20 16:34:20
*/
public interface SysFileService extends IService<SysFile> {

    List<FileDTO> getFileList();

    void downloadFile(String id, HttpServletResponse response) throws UnsupportedEncodingException;

    String uploadFile(MultipartFile file);

    String getPreviewUrl(String id)throws  UnsupportedEncodingException;
}
