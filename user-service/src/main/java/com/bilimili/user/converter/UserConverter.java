package com.bilimili.user.converter;

import com.bilimili.user.dao.User;
import com.bilimili.user.dto.UserDTO;

/**
 * Description:
 *
 * @author Yury
 */
public class UserConverter {

    public static UserDTO convertUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(user.getUid());
        userDTO.setSid(user.getSid());
        userDTO.setName(user.getName());
        //userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setAvatar_url(user.getAvatar());
        userDTO.setBg_url(user.getBackground());
        userDTO.setGender(user.getGender());
        userDTO.setSignature(user.getSignature());
        userDTO.setExp(user.getExp());
        userDTO.setCoin(user.getCoin());
        userDTO.setVip(user.getVip());
        userDTO.setState(user.getState());
        userDTO.setFansCount(user.getFansCount());
        userDTO.setFollowsCount(user.getFollowsCount());
        userDTO.setVideoCount(user.getVideoCount());
        userDTO.setLoveCount(user.getLoveCount());
        userDTO.setPlayCount(user.getPlayCount());
        userDTO.setCollectVideoCount(user.getCollectVideoCount());
        userDTO.setCollectColumnCount(user.getCollectColumnCount());
        userDTO.setCreateDate(user.getCreateDate());
        userDTO.setDeleteDate(user.getDeleteDate());
        return userDTO;
    }

    public static User convertUser(UserDTO userDTO) {
        User user = new User();
        user.setSid(userDTO.getSid());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setAvatar(userDTO.getAvatar_url());
        user.setBackground(userDTO.getBg_url());
        user.setGender(userDTO.getGender());
        user.setSignature(userDTO.getSignature());
        user.setExp(userDTO.getExp());
        user.setCoin(userDTO.getCoin());
        user.setVip(userDTO.getVip());
        user.setState(userDTO.getState());
        user.setFansCount(userDTO.getFansCount());
        user.setFollowsCount(userDTO.getFollowsCount());
        user.setVideoCount(userDTO.getVideoCount());
        user.setLoveCount(userDTO.getLoveCount());
        user.setPlayCount(userDTO.getPlayCount());
        user.setCollectVideoCount(userDTO.getCollectVideoCount());
        user.setCollectColumnCount(userDTO.getCollectColumnCount());
        return user;
    }
}
