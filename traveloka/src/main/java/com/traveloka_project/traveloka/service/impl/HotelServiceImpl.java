package com.traveloka_project.traveloka.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.traveloka_project.traveloka.exception.NotFoundException;
import com.traveloka_project.traveloka.model.Hotel;
import com.traveloka_project.traveloka.model.Location;
import com.traveloka_project.traveloka.model.MediaFile;
import com.traveloka_project.traveloka.payload.response.PaginationResponse;
import com.traveloka_project.traveloka.payload.resquest.HotelRequest;
import com.traveloka_project.traveloka.repository.HotelRepository;
import com.traveloka_project.traveloka.service.HotelService;
import com.traveloka_project.traveloka.service.LocationService;
import com.traveloka_project.traveloka.service.MediaFileService;

import jakarta.transaction.Transactional;

@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final LocationService locationService;
    private final MediaFileService mediaFileService;
    private final Gson gson;

    public HotelServiceImpl(
            HotelRepository hotelRepository,
            LocationService locationService,
            Gson gson,
            MediaFileService mediaFileService) {
        this.hotelRepository = hotelRepository;
        this.locationService = locationService;
        this.gson = gson;
        this.mediaFileService = mediaFileService;
    }

    @Override
    @Transactional(rollbackOn = { NotFoundException.class, Exception.class })
    public HotelRequest save(String hotelRequest, List<MultipartFile> multipartFiles) {
        HotelRequest newHotel = gson.fromJson(hotelRequest, HotelRequest.class);
        Hotel hotelEntity = newHotel.otd();
        Location location = locationService.findLocationById(newHotel.getLocation().getId());
        hotelEntity.setLocation(location);
        if (multipartFiles.size() > 0) {
            List<MediaFile> listMedia = mediaFileService.save(multipartFiles);
            List<Integer> listImageId = listMedia.stream().map(file -> file.getId()).collect(Collectors.toList());
            String listImageIdString = gson.toJson(listImageId);
            hotelEntity.setListMediaFile(listImageIdString);
        }
        return newHotel.dto(hotelRepository.save(hotelEntity));
    }

    @Override
    public PaginationResponse<HotelRequest> findByLocation(Integer pageNum, Integer pageSize, String location) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        String lowerCaseString = location.toLowerCase();
        Page<Hotel> pageHotel = hotelRepository.findByLocation_Name(lowerCaseString, pageable);
        List<HotelRequest> hotelRequests = pageHotel.getContent().stream()
                .map(hotel -> {
                    HotelRequest hotelRequest = new HotelRequest();
                    List<Integer> listIdImage = gson.fromJson(hotel.getListMediaFile(), new TypeToken<List<Integer>>(){}.getType());
                    List<MediaFile> listMediaFile = listIdImage.stream().map(id -> mediaFileService.findById(id))
                            .collect(Collectors.toList());
                    hotelRequest.setListMediaFiles(listMediaFile);
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
