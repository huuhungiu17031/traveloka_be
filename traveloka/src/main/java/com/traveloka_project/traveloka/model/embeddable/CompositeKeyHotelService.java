package com.traveloka_project.traveloka.model.embeddable;

import java.io.Serializable;

import com.traveloka_project.traveloka.model.Hotel;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class CompositeKeyHotelService implements Serializable {
    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;

    
}
