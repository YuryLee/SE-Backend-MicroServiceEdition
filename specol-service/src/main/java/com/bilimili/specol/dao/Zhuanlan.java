package com.bilimili.specol.dao;

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
public class Zhuanlan {
    @TableId(type = IdType.AUTO)
    private Integer zid;    // 收藏夹ID
    private String sid;    // 所属用户ID
    private Integer visible;    // 对外开放 0隐藏 1公开
    private String cover;   // 收藏夹封面url
    private String title;   // 收藏夹名称
    private String description; // 简介
    private Integer count;  // 收藏夹中视频数量
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date updateTime;  // 更改时间
    private Integer status;
}
