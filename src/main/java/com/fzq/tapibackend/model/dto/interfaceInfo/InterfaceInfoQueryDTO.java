package com.fzq.tapibackend.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoQueryDTO implements Serializable {

    private static final long serialVersionUID = -2245328971410484926L;

    private Integer currentPage = 1;
    private Integer pageSize = 10;

    private String name;

    private String description;

    private String url;

    private Integer status;

    private String method;

}

