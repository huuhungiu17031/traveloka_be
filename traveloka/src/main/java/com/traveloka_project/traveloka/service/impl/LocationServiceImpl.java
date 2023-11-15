package com.traveloka_project.traveloka.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.traveloka_project.traveloka.exception.NotFoundException;
import com.traveloka_project.traveloka.model.Location;
import com.traveloka_project.traveloka.payload.response.LocationResponse;
import com.traveloka_project.traveloka.repository.LocationRepository;
import com.traveloka_project.traveloka.service.LocationService;
import com.traveloka_project.traveloka.util.ErrorMessage;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location findLocationById(Integer id) {
        Optional<Location> optional = locationRepository.findById(id);
        if (optional.isEmpty())
            throw new NotFoundException(ErrorMessage.generateNotFoundMessage("Location") + Integer.toString(id));
        return optional.get();
    }

    @Override
    public List<LocationResponse> findLocationLikeName(String name) {
        List<Location> locations;

        if (name != null) {
            locations = locationRepository.findByNameLike("%" + name + "%");
        } else {
            locations = locationRepository.findAll();
        }

        return locations.stream()
                .map(location -> new LocationResponse(location.getId(), location.getName()))
                .collect(Collectors.toList());
    }
}
