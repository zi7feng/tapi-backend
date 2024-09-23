package com.fzq.tapibackend.model.dto.interfaceInfo;

import lombok.Data;

@Data
public class AddInterfaceInfoDTO {

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
     * need validate
     */
    private Boolean needVerify;
}
