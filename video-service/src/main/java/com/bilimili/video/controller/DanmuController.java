package com.bilimili.video.controller;


import com.bilimili.video.dao.Danmu;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.DanmuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@Tag(name = "弹幕api")
public class DanmuController {
    @Autowired
    private DanmuService danmuService;

    /**
     * 获取弹幕列表
     * @param vid   视频ID
     * @return  CustomResponse对象
     */
    @GetMapping("/danmu/vid/{vid}")
    public CustomResponse getDanmuList(@PathVariable("vid") Integer vid) {
        List<Danmu> danmuList = danmuService.getDanmuListByVid(vid);
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(danmuList);
        return customResponse;
    }

//    private String barrageType;
//    private String color;   // 字体颜色 6位十六进制标准格式 #FFFFFF
//    private Integer duration;
//    private Integer fontsize;   // 字体大小 默认25 小18
//    private Double lineHeight;
//    private Boolean prior;
//    private String content; // 弹幕内容
//    private Double time;   // 弹幕在视频中的时间点位置（秒）
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
//    private Date createDate;    // 弹幕发送日期时间
//
    @PostMapping("/danmu/send")
    public CustomResponse sendDanmu(@RequestParam("vid") Integer vid,
                                    @RequestParam("sid") String sid,
                                    @RequestParam("barrageType") String barrageType,
                                    @RequestParam("color") String color,
                                    @RequestParam("duration") Integer duration,
                                    @RequestParam("fontsize") Integer fontsize,
                                    @RequestParam("lineHeight") Double lineHeight,
                                    @RequestParam("prior") Boolean prior,
                                    @RequestParam("content") String content,
                                    @RequestParam("time") Double time) {
        Danmu danmu = new Danmu(null, vid, sid, barrageType, color, duration, fontsize, lineHeight, prior, content, time, new Date());
        return danmuService.sendDanmu(danmu);
    }
    /**
     * 删除弹幕
     * @param id    弹幕id
     * @return  响应对象
     */
    @PostMapping("/danmu/delete")
    public CustomResponse deleteDanmu(@RequestParam("id") Integer id,
                                      @RequestParam("sid") String sid) {
        return danmuService.deleteDanmu(id, sid);
    }
}
