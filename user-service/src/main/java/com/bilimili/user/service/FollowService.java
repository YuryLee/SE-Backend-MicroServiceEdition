package com.bilimili.user.service;

import com.bilimili.user.dto.UserDTO;
import com.bilimili.user.response.CustomResponse;

import java.util.List;

public interface FollowService {
    CustomResponse addOrCancelFollow(String sidFrom, String sidTo);

    List<UserDTO> getFans(String sid);
    Boolean isFollowed(String sidFrom, String sidTo);

    List<UserDTO> getFollows(String sid);
}
