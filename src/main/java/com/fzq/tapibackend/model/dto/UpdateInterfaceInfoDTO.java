package com.fzq.tapibackend.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateInterfaceInfoDTO implements Serializable {

    private static final long serialVersionUID = 6675662158388321207L;

    private long id;

    /**
     * Interface name
     */
    private String name;

    /**
     * description
     */
    private String description;

    /**
     * url
     */
    private String url;

    /**
     * request parameters
     */
    private String requestParams;

    /**
     * request header
     */
    private String requestHeader;

    /**
     * response header
     */
    private String responseHeader;

    /**
     * request method
     */
    private String method;

    /**
     * status(0-close 1-open)
     */
    private Integer status;
}
