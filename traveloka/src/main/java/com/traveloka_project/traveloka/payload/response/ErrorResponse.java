package com.traveloka_project.traveloka.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private Integer statusCode;
    private Long timeStamp;
    private String message;
}
