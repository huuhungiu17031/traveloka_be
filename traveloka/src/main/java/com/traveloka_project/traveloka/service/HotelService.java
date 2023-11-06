package com.traveloka_project.traveloka.service;

import com.traveloka_project.traveloka.payload.response.PaginationResponse;
import com.traveloka_project.traveloka.payload.resquest.HotelRequest;

public interface HotelService {
    HotelRequest save(HotelRequest hotelRequest);

    PaginationResponse<HotelRequest> findByLocation(Integer pageNum, Integer pageSize, String location);
}
