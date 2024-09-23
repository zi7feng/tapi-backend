package com.fzq.tapibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateDTO implements Serializable {
    private static final long serialVersionUID = -4547466694395285741L;


    private Long id;
    /**
     * User Name
     */
    private String userName;

    /**
     * email
     */
    private String email;


    private String userAvatar;
}
