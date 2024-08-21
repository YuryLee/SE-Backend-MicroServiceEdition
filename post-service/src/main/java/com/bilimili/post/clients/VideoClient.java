package com.bilimili.post.clients;

import com.bilimili.post.dto.VideoDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/8/23 上午12:54
 */
@FeignClient(value = "video-service")
public interface VideoClient {
    @GetMapping("/video-service/vid/{vid}")
    @Operation(summary = "服务接口：通过vid获取视频")
    VideoDTO getVideoWithDataByVid(@PathVariable("vid") Integer vid);
}
