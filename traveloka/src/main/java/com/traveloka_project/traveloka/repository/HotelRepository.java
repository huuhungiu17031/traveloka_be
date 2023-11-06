package com.traveloka_project.traveloka.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.traveloka_project.traveloka.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    Page<Hotel> findByLocation(String location, Pageable pageable);
}
