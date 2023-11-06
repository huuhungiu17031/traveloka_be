package com.traveloka_project.traveloka.payload.resquest;

import java.util.List;

import com.traveloka_project.traveloka.model.Hotel;
import com.traveloka_project.traveloka.model.Room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HotelRequest {
    private Integer id;
    private String name;
    private String address;
    private String description;
    private Integer locationId;
    private String checkInInstruction;
    private Boolean status = false;
    private Integer price;
    private List<Room> listRooms;

    public Hotel otd() {
        Hotel newHotel = new Hotel();
        newHotel.setId(this.id);
        newHotel.setName(this.name);
        newHotel.setAddress(this.address);
        newHotel.setDescription(this.description);
        newHotel.setCheckInInstruction(this.checkInInstruction);
        newHotel.setStatus(this.status);
        return newHotel;
    }

    public HotelRequest dto(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.description = hotel.getDescription();
        this.locationId = hotel.getLocation().getId();
        this.checkInInstruction = hotel.getCheckInInstruction();
        this.status = hotel.getStatus();
        return this;
    }
}
