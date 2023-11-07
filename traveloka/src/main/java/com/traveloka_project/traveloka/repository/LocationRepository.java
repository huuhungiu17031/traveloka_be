package com.traveloka_project.traveloka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.traveloka_project.traveloka.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {}
