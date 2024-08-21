package com.bilimili.video.service;

import com.bilimili.video.dto.VideoUploadInfoDTO;
import com.bilimili.video.response.CustomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoUploadService {
    CustomResponse uploadVideo(MultipartFile video) throws IOException;

    CustomResponse addVideo(MultipartFile cover, VideoUploadInfoDTO videoUploadInfoDTO) throws IOException;

    CustomResponse cancelUpload(String videoUrl);

    CustomResponse updateVideo(Integer vid, MultipartFile cover, VideoUploadInfoDTO videoUploadInfoDTO) throws IOException;
}
