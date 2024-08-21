package com.bilimili.search.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Table;
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
@Table(name="video")
public class Video {
    @TableId(type = IdType.AUTO)
    private Integer vid;
    private Integer uid;
    private String sid;
    private String title;
    private Integer type;
    private Integer auth;
    private Double duration;
    private String mcId;
    private String tags;
    private String descr;
    private String coverUrl;
    private String videoUrl;
    private Integer status;     // 0审核中 1通过审核 2打回整改（指投稿信息不符） 3视频违规删除（视频内容违规）
    private Integer complain;
    private String advice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date uploadDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date deleteDate;

    private Integer play;
    private Integer danmu;
    private Integer good;
    private Integer bad;
    private Integer coin;
    private Integer collect;
    private Integer share;
    private Integer comment;
}
