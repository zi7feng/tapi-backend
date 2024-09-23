package com.fzq.tapibackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fzq.tapibackend.common.BaseResponse;
import com.fzq.tapibackend.common.ErrorCode;
import com.fzq.tapibackend.common.ResultUtils;
import com.fzq.tapibackend.exception.BusinessException;
import com.fzq.tapibackend.model.dto.common.DeleteDTO;
import com.fzq.tapibackend.model.dto.user.UserLoginDTO;
import com.fzq.tapibackend.model.dto.user.UserQueryDTO;
import com.fzq.tapibackend.model.dto.user.UserRegisterDTO;
import com.fzq.tapibackend.model.dto.user.UserUpdateDTO;
import com.fzq.tapibackend.model.entity.User;
import com.fzq.tapibackend.model.vo.UserVO;
import com.fzq.tapibackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * User register
     * @param userRegisterDTO user register request
     * @return BaseResponse<Long>
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long result = userService.userRegister(userRegisterDTO);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        UserVO user = userService.userLogin(userLoginDTO, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<UserVO> getCurrentUser(HttpServletRequest request) {
        UserVO user = userService.getCurrentUser(request);
        return ResultUtils.success(user);
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteDTO deleteDTO, HttpServletRequest request) {
        if (ObjectUtils.anyNull(deleteDTO, deleteDTO.getId())) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (deleteDTO.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO currentUser = userService.getCurrentUser(request);
        if (!userService.isAdmin(currentUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return ResultUtils.success(userService.removeById(deleteDTO.getId()));
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, HttpServletRequest request) {
        UserVO user = userService.getCurrentUser(request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int result = userService.updateUser(userUpdateDTO, user, request);
        if (result > 0) {
            return ResultUtils.success(true);
        }

        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Update fail.");

    }

    @GetMapping("/getById")
    public BaseResponse<UserVO> getUserById(int id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    @GetMapping("/list")
    public BaseResponse<IPage<UserVO>> listInterfaceInfo(UserQueryDTO userQueryDTO, HttpServletRequest request) {
        if (userQueryDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (userQueryDTO.getCurrentPage() <= 0 || userQueryDTO.getPageSize() <= 0) {
            userQueryDTO.setCurrentPage(1);
            userQueryDTO.setPageSize(10);
        }

        IPage<UserVO> result = userService.listUserByPage(userQueryDTO);
        return ResultUtils.success(result);
    }



}
