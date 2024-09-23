package com.fzq.tapibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserQueryDTO implements Serializable {

    private static final long serialVersionUID = 2395180992525422364L;
    private Integer currentPage = 1;
    private Integer pageSize = 10;

    private Long id;

    private String userName;

    private String userAccount;

    private String email;
}
