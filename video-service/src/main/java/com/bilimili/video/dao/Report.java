package com.bilimili.video.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description:
 *
 * @author Yury
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @TableId(type = IdType.AUTO)
    private Integer rid;
    private Integer vid;
    private String title;
    private String url;
    private String reason;
    private Integer status;
    private String sid;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date reportDate;
    private String authSid;
    private String authName;
    private Integer appeal;
    private String appealReason;
}