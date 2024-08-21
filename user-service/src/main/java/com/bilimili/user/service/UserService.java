package com.bilimili.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bilimili.user.dao.User;
import com.bilimili.user.dto.UserDTO;
import com.bilimili.user.response.CustomResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends IService<User> {
    UserDTO getUserBySid(String sid);

    Long addNewUser(UserDTO userDTO);

    Long deleteUserBySid(String sid);

    UserDTO updateUserBySid(String sid, String name, String password);

    Long loginAdmin(UserDTO userDTO);

    Long loginUser(UserDTO userDTO);

    List<UserDTO> getUserByName(String name);

    List<UserDTO> findAll();

    UserDTO updateUserNameBySid(String sid, String name);

    UserDTO updateUserPasswordBySid(String sid, String password);

    UserDTO updateUserRoleBySid(String sid, int role);

    @Transactional
    CustomResponse register(String sid, String username, String password) throws IOException;

    CustomResponse updateUserAvatarBySid(String sid, MultipartFile file) throws IOException;

    CustomResponse updateUserInfo(String sid, String name, String signature, Integer gender) throws IOException;

    CustomResponse updateUserSignatureBySid(String sid, String signature) throws IOException;

    CustomResponse updateUserGenderBySid(String id, Integer gender) throws IOException;

    void updateStats(String sid, String column, boolean increase, Integer count);

}
