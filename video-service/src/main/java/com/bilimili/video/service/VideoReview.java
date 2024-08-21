package com.bilimili.video.service;

import com.bilimili.video.response.CustomResponse;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface VideoReview {
    @Transactional
    CustomResponse updateVideoStatus(Integer vid, Integer status, String advice) throws IOException;
}
