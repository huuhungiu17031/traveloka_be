package com.traveloka_project.traveloka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.traveloka_project.traveloka.model.MediaFile;

public interface MediaFileRepository extends JpaRepository<MediaFile, Integer> {
    Optional<MediaFile> findByName(String fileName);
}
