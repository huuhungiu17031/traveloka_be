package com.traveloka_project.traveloka.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationResponse<U> {
    private List<U> content;
    private int pageNum;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;

    public PaginationResponse() {}

    public PaginationResponse(int pageNum, int pageSize, long totalElements, int totalPage, boolean isLast) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPage;
        this.isLast = isLast;
    }

}