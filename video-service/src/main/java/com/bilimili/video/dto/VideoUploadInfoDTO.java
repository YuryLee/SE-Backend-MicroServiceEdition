package com.bilimili.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadInfoDTO {
    private Integer uid;
    private String sid;
    private String title;
    private Integer type;
    private Integer auth;
    private Double duration;
    private String mcId;
    private String scId;
    private String tags;
    private String descr;
    private String advice;
    private String coverUrl;
    private String videoUrl;
}
