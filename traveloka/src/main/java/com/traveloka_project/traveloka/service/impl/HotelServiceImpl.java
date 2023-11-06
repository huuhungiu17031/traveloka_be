package com.traveloka_project.traveloka.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.traveloka_project.traveloka.model.Hotel;
import com.traveloka_project.traveloka.payload.response.PaginationResponse;
import com.traveloka_project.traveloka.payload.resquest.HotelRequest;
import com.traveloka_project.traveloka.repository.HotelRepository;
import com.traveloka_project.traveloka.service.HotelService;

@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public HotelRequest save(HotelRequest hotelRequest) {
        Hotel hotel = hotelRepository.save(hotelRequest.otd());
        return hotelRequest.dto(hotel);
    }

    @Override
    public PaginationResponse<HotelRequest> findByLocation(Integer pageNum, Integer pageSize, String location) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        String lowerCaseString = location.toLowerCase();
        Page<Hotel> pageHotel = hotelRepository.findByLocation_Name(lowerCaseString, pageable);
        List<HotelRequest> hotelRequests = pageHotel.getContent().stream()
                .map(hotel -> {
                    HotelRequest hotelRequest = new HotelRequest();
                    return hotelRequest.dto(hotel);
                })
                .collect(Collectors.toList());
        PaginationResponse<HotelRequest> pageHotelRequest = new PaginationResponse(
                pageNum,
                pageSize,
                pageHotel.getTotalElements(),
                pageHotel.getTotalPages(),
                pageHotel.isLast());
        pageHotelRequest.setContent(hotelRequests);
        return pageHotelRequest;
    }
}
