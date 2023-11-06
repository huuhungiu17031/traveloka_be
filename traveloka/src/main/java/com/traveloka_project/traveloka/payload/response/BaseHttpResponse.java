package com.traveloka_project.traveloka.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static java.time.LocalTime.now;
@Getter
@Setter
@NoArgsConstructor
public class BaseHttpResponse {
    private Integer statusCode;
    private String timeStamp;
    private Object data;

    public BaseHttpResponse(Integer statusCode, Object data) {
        this.statusCode = statusCode;
        this.data = data;
        this.timeStamp = now().toString();
    }
}
