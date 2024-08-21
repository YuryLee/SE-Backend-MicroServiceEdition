package com.bilimili.video.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Danmu {
    @TableId(type = IdType.AUTO)
    private Integer id;     // 弹幕ID
    private Integer vid;    // 视频ID
    private String sid;    // 用户ID
    private String barrageType;
    private String color;   // 字体颜色 6位十六进制标准格式 #FFFFFF
    private Integer duration;
    private Integer fontsize;   // 字体大小 默认25 小18
    private Double lineHeight;
    private Boolean prior;
    private String content; // 弹幕内容
    private Double time;   // 弹幕在视频中的时间点位置（秒）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date createDate;    // 弹幕发送日期时间
}
