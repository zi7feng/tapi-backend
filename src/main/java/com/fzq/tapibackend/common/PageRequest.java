package com.fzq.tapibackend.common;

import com.fzq.tapibackend.constant.CommonConstant;
import lombok.Data;

@Data
public class PageRequest {

    private int current = 1;

    private int pageSize = 10;

    private String sortField;

    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
