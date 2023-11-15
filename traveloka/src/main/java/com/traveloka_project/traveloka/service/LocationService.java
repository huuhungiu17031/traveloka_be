package com.traveloka_project.traveloka.service;

import java.util.List;

import com.traveloka_project.traveloka.model.Location;
import com.traveloka_project.traveloka.payload.response.LocationResponse;

public interface LocationService {

    Location findLocationById(Integer id);

    List<LocationResponse> findLocationLikeName(String name);
}
