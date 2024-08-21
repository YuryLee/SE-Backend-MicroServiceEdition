package com.bilimili.post.dto;

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
public class UserDTO {
    private Long uid;

    private String sid;

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
    public void setRole(int role) {
        this.role = role;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public void setCoin(Double coin) {
        this.coin = coin;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public void setFollowsCount(Integer followsCount) {
        this.followsCount = followsCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public void setLoveCount(Integer loveCount) {
        this.loveCount = loveCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
