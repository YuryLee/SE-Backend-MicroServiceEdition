package com.bilimili.msg.controller;

import com.bilimili.msg.response.CustomResponse;
import com.bilimili.msg.service.MsgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 *
 * @author Yury
 */
@CrossOrigin
@RestController
@Tag(name = "系统消息api")
public class MsgController {

    @Autowired
    private MsgService msgService;

    @PostMapping("/msg/send")
    @Operation(summary = "给sid发送消息，type 0视频 1专栏 2创作周报 3视频周报 4删除视频 5设置专栏不可见")
    public CustomResponse sendMsg(@RequestParam("sid") String sid,
                                  @RequestParam("vid") Integer vid,
                                  @RequestParam("type") int type) {
        CustomResponse customResponse = new CustomResponse();
        msgService.sendMsg(sid, vid, type);
        return customResponse;
    }

    @GetMapping("/msg/sid/{sid}")
    @Operation(summary = "获取sid的所有消息")
    public CustomResponse getMsgBySid(@PathVariable String sid) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(msgService.getMsgBySid(sid));
        return customResponse;
    }


}
