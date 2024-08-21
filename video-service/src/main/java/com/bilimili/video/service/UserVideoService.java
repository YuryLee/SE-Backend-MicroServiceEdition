package com.bilimili.video.service;

import com.bilimili.video.dao.UserVideo;
import com.bilimili.video.response.CustomResponse;

import java.util.List;

public interface UserVideoService {

    UserVideo updatePlay(String sid, Integer vid);

    UserVideo setLoveOrUnlove(String uid, Integer vid, boolean isLove, boolean isSet);

    List<Integer> getIdsBySid(String sid, Integer quantity);

    Boolean isCollected(String sid, Integer vid);

    CustomResponse addOrCancelFavoriteVideo(String sid, Integer vid);

    List<Integer> getUserCollectVideoList(String sid);

    Boolean isLoved(String sid, Integer vid);

    CustomResponse deleteVideosFromFavorite(String sid, List<Integer> vidList);
}
