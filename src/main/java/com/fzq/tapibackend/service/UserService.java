package com.fzq.tapibackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fzq.tapibackend.model.dto.common.DeleteDTO;
import com.fzq.tapibackend.model.dto.user.UserLoginDTO;
import com.fzq.tapibackend.model.dto.user.UserQueryDTO;
import com.fzq.tapibackend.model.dto.user.UserRegisterDTO;
import com.fzq.tapibackend.model.dto.user.UserUpdateDTO;
import com.fzq.tapibackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fzq.tapibackend.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author zfeng
* @description 针对表【User(User table)】的数据库操作Service
* @createDate 2024-09-22 13:08:18
*/
public interface UserService extends IService<User> {

    Long userRegister(UserRegisterDTO userRegisterDTO);

    UserVO userLogin(UserLoginDTO userLoginDTO, HttpServletRequest request);

    Boolean userLogout(HttpServletRequest request);

    UserVO getCurrentUser(HttpServletRequest request);

    int updateUser(UserUpdateDTO userUpdateDTO, UserVO userVO, HttpServletRequest request);

    boolean isAdmin(UserVO user);

    IPage<UserVO> listUserByPage(UserQueryDTO userQueryDTO);

}
