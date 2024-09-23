package com.fzq.tapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzq.tapibackend.common.ErrorCode;
import com.fzq.tapibackend.common.PasswordUtils;
import com.fzq.tapibackend.exception.BusinessException;
import com.fzq.tapibackend.mapper.UserMapper;
import com.fzq.tapibackend.model.dto.user.UserLoginDTO;
import com.fzq.tapibackend.model.dto.user.UserQueryDTO;
import com.fzq.tapibackend.model.dto.user.UserRegisterDTO;
import com.fzq.tapibackend.model.dto.user.UserUpdateDTO;
import com.fzq.tapibackend.model.entity.User;
import com.fzq.tapibackend.model.vo.UserVO;
import com.fzq.tapibackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fzq.tapibackend.constant.UserConstant.ADMIN_ROLE;
import static com.fzq.tapibackend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author zfeng
* @description 针对表【User(User table)】的数据库操作Service实现
* @createDate 2024-09-22 13:08:18
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private static final String SALT = "fzq";

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long userRegister(UserRegisterDTO userRegisterDTO) {
        String userName = userRegisterDTO.getUserName();
        String userAccount = userRegisterDTO.getUserAccount();
        String userPassword = userRegisterDTO.getUserPassword();
        String email = userRegisterDTO.getEmail();
        String checkPassword = userRegisterDTO.getCheckPassword();

        if (StringUtils.isAnyBlank(userName, userAccount, userPassword, checkPassword, email)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The length of the user account cannot be less than 4");
        }
        if (userName.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The length of the user name cannot be less than 4");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The length of the password cannot be less than 8");
        }

        // no special characters
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The user account cannot contain special characters");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The two passwords are not matched");
        }


        // duplicate account
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);

        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account already exists.");
        }

        // encrypt
        String encryptPassword = PasswordUtils.encryptPassword(userPassword, SALT);        // is account exist
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserName(userName);
        user.setUserPassword(encryptPassword);
        user.setEmail(email);
        boolean saveResult = this.save(user);
        if (saveResult) {
            return user.getId();
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Register failed.");
        }
    }

    @Override
    public UserVO userLogin(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = userLoginDTO.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // encrypt
        String encryptPassword = PasswordUtils.encryptPassword(userPassword, SALT);        // is account exist
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // doesn't exist
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Wrong account or password.");
        }
        UserVO safeUser = getSafeUser(user);
        // 4. set login state
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, safeUser);
        session.setMaxInactiveInterval(60 * 60); // expire time: 60 * 60s

        return safeUser;

    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getCurrentUser(HttpServletRequest request) {
        UserVO user = (UserVO) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return user;
    }

    @Override
    public int updateUser(UserUpdateDTO userUpdateDTO, UserVO currentUser, HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long id = userUpdateDTO.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // check authority
        // 2.1 admin can update any info, user can only update their info
        if (!isAdmin(currentUser) && !id.equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = this.getById(userUpdateDTO.getId());
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Cannot find user with id: " + userUpdateDTO.getId());
        }
        oldUser.setUserName(userUpdateDTO.getUserName());
        oldUser.setUserAvatar(userUpdateDTO.getUserAvatar());
        oldUser.setEmail(userUpdateDTO.getEmail());
        return this.baseMapper.updateById(oldUser);
    }




    private UserVO getSafeUser(User user) {
        UserVO safeUser = new UserVO();
        safeUser.setUserName(user.getUserName());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setEmail(user.getEmail());
        safeUser.setId(user.getId());
        safeUser.setUserAvatar(user.getUserAvatar());
        safeUser.setUserRole(user.getUserRole());
        safeUser.setBalance(user.getBalance());
        safeUser.setStatus(user.getStatus());
        return safeUser;
    }


    @Override
    public boolean isAdmin(UserVO user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return user.getUserRole().equals(ADMIN_ROLE);
    }

    @Override
    public IPage<UserVO> listUserByPage(UserQueryDTO userQueryDTO) {
        if (userQueryDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "userQueryDTO is null");
        }
        Page<User> page = new Page<>(userQueryDTO.getCurrentPage(), userQueryDTO.getPageSize());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String userAccount = userQueryDTO.getUserAccount();
        String userName = userQueryDTO.getUserName();
        String email = userQueryDTO.getEmail();
        long id = userQueryDTO.getId();

        if (id > 0) {
            queryWrapper.eq("id", id);
        } else {
            if (StringUtils.isNotBlank(userAccount)) {
                queryWrapper.like("user_account", userAccount);
            }
            if (StringUtils.isNotBlank(userName)) {
                queryWrapper.like("user_name", userName);
            }
            if (StringUtils.isNotBlank(email)) {
                queryWrapper.like("email", email);
            }
        }
        IPage<User> userPageResult = this.page(page, queryWrapper);

        IPage<UserVO> userVOPageResult = userPageResult.convert(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        });

        return userVOPageResult;

    }
}




