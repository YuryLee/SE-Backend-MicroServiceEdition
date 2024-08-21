package com.bilimili.video.clients;

import com.bilimili.video.dto.VideoDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/8/23 上午12:10
 */
@FeignClient(value = "post-service")
public interface PostClient {
    @PostMapping("post-service/sendVideoPost")
    @Operation(summary = "发送视频动态")
    void sendVideoPost(@RequestBody VideoDTO videoDTO);
}
