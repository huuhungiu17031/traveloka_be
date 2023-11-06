package com.traveloka_project.traveloka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.traveloka_project.traveloka.payload.response.BaseHttpResponse;
import com.traveloka_project.traveloka.payload.response.PaginationResponse;
import com.traveloka_project.traveloka.payload.resquest.HotelRequest;
import com.traveloka_project.traveloka.service.HotelService;

@RestController
@RequestMapping("hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping
    public ResponseEntity<BaseHttpResponse> createHotel(@RequestBody HotelRequest hotelRequest) {
        HotelRequest newHotel = hotelService.save(hotelRequest);
        BaseHttpResponse response = new BaseHttpResponse(HttpStatus.OK.value(), newHotel);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("search")
    public ResponseEntity<BaseHttpResponse> findByLocation(
            @RequestParam(defaultValue = "0", required = false) Integer pageNum,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "Ha Noi", required = false) String location) {
        PaginationResponse<HotelRequest> pageHotelRequest = hotelService.findByLocation(pageNum, pageSize,
                location);
        BaseHttpResponse response = new BaseHttpResponse(HttpStatus.OK.value(), pageHotelRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
