package com.bilimili.specol.clients;

import com.bilimili.specol.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/8/22 下午10:35
 */
@FeignClient(value = "msg-service")
public interface MsgClient {
    @PostMapping("/msg/send")
    @Operation(summary = "给sid发送消息，type 0视频 1专栏 2创作周报 3视频周报 4删除视频 5设置专栏不可见")
    CustomResponse sendMsg(@RequestParam("sid") String sid,
                                  @RequestParam("vid") Integer vid,
                                  @RequestParam("type") int type);
}
