package com.traveloka_project.traveloka.service;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.traveloka_project.traveloka.model.MediaFile;

public interface MediaFileService {
    List<MediaFile> save(List<MultipartFile> listFile);

    MediaFile findById(Integer id);

    ResponseEntity<Resource> downloadFile(String fileName);
}
