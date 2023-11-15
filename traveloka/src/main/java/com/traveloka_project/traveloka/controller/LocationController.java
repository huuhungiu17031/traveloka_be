package com.traveloka_project.traveloka.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.traveloka_project.traveloka.payload.response.BaseHttpResponse;
import com.traveloka_project.traveloka.payload.response.LocationResponse;
import com.traveloka_project.traveloka.service.LocationService;

@RestController
@RequestMapping("location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<BaseHttpResponse> getUserInfor(
            @RequestParam(defaultValue = "0", required = false) Integer pageNum,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minPrice) {
        List<LocationResponse> listLocationResponse = locationService.findLocationLikeName(location);
        BaseHttpResponse response = new BaseHttpResponse(HttpStatus.OK.value(), listLocationResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
