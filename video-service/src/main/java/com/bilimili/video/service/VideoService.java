package com.bilimili.video.service;

import com.bilimili.video.dao.Video;
import com.bilimili.video.dto.VideoDTO;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public interface VideoService {
    VideoDTO getVideoWithDataByVid(Integer vid);

    Map<String, Object> getAllVideos(Integer status);

    List<Video> getShuffleValidVideosByCount(Integer count);

    List<VideoDTO> getUserVideosByIdList(List<Integer> idList);


    List<Video> getVideosWithDataBySidOrderByDesc(String sid, @Nullable String column);

    void updateStats(Integer vid, String column, boolean increase, Integer count);

    void updateGoodAndBad(Integer vid, boolean addGood);


    List<Video> getShuffleValidVideosByMcidAndCount(Integer count, String mcid);
}
