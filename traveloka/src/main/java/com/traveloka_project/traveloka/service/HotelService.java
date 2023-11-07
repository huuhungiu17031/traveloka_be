package com.traveloka_project.traveloka.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.traveloka_project.traveloka.payload.response.PaginationResponse;
import com.traveloka_project.traveloka.payload.resquest.HotelRequest;

public interface HotelService {

    HotelRequest save(String hotelRequest, List<MultipartFile> multipartFiles);

    PaginationResponse<HotelRequest> findByLocation(Integer pageNum, Integer pageSize, String location);

}
