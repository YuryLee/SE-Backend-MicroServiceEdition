package com.bilimili.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
public class UserDTO {
    private Long uid;

    private String sid;

    @Getter
    private String name;

    private String password;

    private int role;
    private String avatar_url;
    private String bg_url;
    private Integer gender; // 性别，0女性 1男性 2无性别，默认2
    private String signature;
    private Integer exp;    // 经验值 50/200/1500/4500/10800/28800 分别是0~6级的区间
    private Double coin;    // 硬币数  保留一位小数
    private Integer vip;    // 0 普通用户，1 月度大会员，2 季度大会员，3 年度大会员
    private Integer state;  // 0 正常，1 封禁中
    private Integer videoCount; // 视频投稿数
    private Integer followsCount;   // 关注数
    private Integer fansCount;  // 粉丝数
    private Integer loveCount;  // 获赞数
    private Integer playCount;  // 播放数
    private Integer collectVideoCount;
    private Integer collectColumnCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date deleteDate;

}
