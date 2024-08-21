package com.bilimili.video.converter;

import com.bilimili.video.clients.UserClient;
import com.bilimili.video.dao.Video;
import com.bilimili.video.dto.VideoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author Yury
 */
@Component
public class VideoConverter {
    @Autowired
    private UserClient userClient;
    public VideoDTO Converter(Video video) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setVid(video.getVid());
        videoDTO.setUid(video.getUid());
        videoDTO.setSid(video.getSid());
        //videoDTO.setName(userClient.getUserDTOBySid(video.getSid()).getName());
        videoDTO.setTitle(video.getTitle());
        videoDTO.setType(video.getType());
        videoDTO.setAuth(video.getAuth());
        videoDTO.setDuration(video.getDuration());
        videoDTO.setMcId(video.getMcId());
        videoDTO.setTags(video.getTags());
        videoDTO.setDescr(video.getDescr());
        videoDTO.setCoverUrl(video.getCoverUrl());
        videoDTO.setVideoUrl(video.getVideoUrl());
        videoDTO.setStatus(video.getStatus());
        videoDTO.setComplain(video.getComplain());
        videoDTO.setAdvice(video.getAdvice());
        videoDTO.setUploadDate(video.getUploadDate());
        videoDTO.setDeleteDate(video.getDeleteDate());
        videoDTO.setPlay(video.getPlay());
        videoDTO.setDanmu(video.getDanmu());
        videoDTO.setGood(video.getGood());
        videoDTO.setBad(video.getBad());
        videoDTO.setCoin(video.getCoin());
        videoDTO.setCollect(video.getCollect());
        videoDTO.setShare(video.getShare());
        videoDTO.setComment(video.getComment());
        return videoDTO;
    }
}
