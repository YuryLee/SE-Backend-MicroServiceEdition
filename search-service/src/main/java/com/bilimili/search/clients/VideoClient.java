package com.bilimili.search.clients;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.search.dao.Video;
import com.bilimili.search.dto.VideoDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/8/23 上午12:54
 */
@FeignClient(value = "video-service")
public interface VideoClient {
    @GetMapping("/video-service/vid/{vid}")
    @Operation(summary = "微服务接口：通过vid获取视频")
    VideoDTO getVideoWithDataByVid(@PathVariable("vid") Integer vid);

    @PostMapping("/video-service/updateStats")
    @Operation(summary = "微服务接口：更改vid视频数据")
    void updateStats(@RequestParam("vid") Integer vid, @RequestParam("column") String column,
                            @RequestParam("increase") boolean increase, @RequestParam("count") Integer count);

    @GetMapping("/video-service/videoList/sid/{sid}")
    @Operation(summary = "微服务接口：获取sid所有视频")
    List<Video> getVideoListBySid(@PathVariable("sid") String sid);

    @GetMapping("/video-service/user/is-collected")
    @Operation(summary = "微服务接口：sid是否收藏vid")
    Boolean isCollectService(@RequestParam("sid") String sid, @RequestParam("vid") Integer vid);

    @GetMapping("/video-service/search/{text}")
    @Operation(summary = "服务接口：搜索text相关视频")
    List<Video> searchVideo(@PathVariable String text);
}
