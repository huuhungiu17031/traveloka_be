package com.traveloka_project.traveloka.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.util.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Path;

import com.traveloka_project.traveloka.exception.FileException;
import com.traveloka_project.traveloka.model.MediaFile;
import com.traveloka_project.traveloka.repository.MediaFileRepository;
import com.traveloka_project.traveloka.service.MediaFileService;
import com.traveloka_project.traveloka.util.ErrorMessage;
import static java.nio.file.Paths.get;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import org.springframework.http.MediaType;

import java.nio.file.Files;

@Service
public class MediaFileServiceImpl implements MediaFileService {
    private final MediaFileRepository mediaFileRepository;
    private static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";
    private static final int fileSize = 10 * 1024 * 1024;

    public MediaFileServiceImpl(MediaFileRepository mediaFileRepository) {
        this.mediaFileRepository = mediaFileRepository;
    }

    @Override
    public List<MediaFile> save(List<MultipartFile> listFile) {
        List<MediaFile> listMediaFile = new ArrayList<>();
        for (MultipartFile file : listFile) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileName.contains("..")) {
                throw new FileException(ErrorMessage.INVALID_CHARATER + fileName);
            }
            if (file.getSize() > fileSize) {
                throw new FileException(ErrorMessage.FILE_EXCEED_LIMIT);
            }
            Path fileStorage = get(DIRECTORY, fileName).toAbsolutePath().normalize();
            try {
                copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            } catch (Exception e) {
                throw new FileException(e.getLocalizedMessage());
            }

            String downloadUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/download/")
                    .path(fileName)
                    .toUriString();
            MediaFile newFile = new MediaFile();
            newFile.setImageUrl(downloadUrl);
            newFile.setName(fileName);
            newFile.setSize(file.getSize());
            listMediaFile.add(mediaFileRepository.save(newFile));
        }
        return listMediaFile;
    }

    @Override
    public MediaFile findById(Integer id) {
        Optional<MediaFile> mediaFile = mediaFileRepository.findById(id);
        if (mediaFile.isEmpty())
            throw new FileException(ErrorMessage.FILE_NOT_FOUND + Integer.toString(id));
        return mediaFile.get();
    }

    private MediaFile findByName(String fileName) {
        Optional<MediaFile> mediaFile = mediaFileRepository.findByName(fileName);
        if (mediaFile.isEmpty())
            throw new FileException(ErrorMessage.FILE_NOT_FOUND + fileName);
        return mediaFile.get();
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String fileName) {
        try {
            MediaFile dbFile = findByName(fileName);
            Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(dbFile.getName());
            if (!Files.exists(filePath)) {
                throw new FileException(ErrorMessage.FILE_NOT_FOUND + fileName);
            }
            Resource resource = new UrlResource(filePath.toUri());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("File-Name", dbFile.getName());
            httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .headers(httpHeaders)
                    .body(resource);
        } catch (Exception e) {
            throw new FileException(e.getLocalizedMessage());
        }
    }

}
