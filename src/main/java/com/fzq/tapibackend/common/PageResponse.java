package com.fzq.tapibackend.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private long total;
    private List<T> records;

    private Integer currentPage = 1;
    private Integer pageSize = 10;


}
