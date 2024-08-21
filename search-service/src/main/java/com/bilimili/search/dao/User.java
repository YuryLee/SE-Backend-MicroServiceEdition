package com.bilimili.search.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Description:
 *
 * @author Yury
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user")
public class User {

    @Id
    @TableId(type = IdType.AUTO)
    @Column(name = "uid")
    @GeneratedValue(strategy = IDENTITY)
    private Long uid;

    @Column(name = "sid")
    private String sid;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar")
    private String avatar;
    @Column(name = "background")
    private String background;
    @Column(name = "gender")
    private Integer gender; // 性别，0女性 1男性 2无性别，默认2
    @Column(name = "signature")
    private String signature;
    @Column(name = "exp")
    private Integer exp;    // 经验值 50/200/1500/4500/10800/28800 分别是0~6级的区间
    @Column(name = "coin")
    private Double coin;    // 硬币数 保留一位小数
    @Column(name = "vip")
    private Integer vip;    // 0 普通用户，1 月度大会员，2 季度大会员，3 年度大会员
    @Column(name = "state")
    private Integer state;  // 0 正常，1 封禁中，2 已注销
    @Column(name = "role")
    private Integer role;   // 0 普通用户，1 普通管理员，2 超级管理员

    @Column(name = "video_count")
    private Integer videoCount; // 视频投稿数
    @Column(name = "follows_count")
    private Integer followsCount;   // 关注数
    @Column(name = "fans_count")
    private Integer fansCount;  // 粉丝数
    @Column(name = "love_count")
    private Integer loveCount;  // 获赞数
    @Column(name = "play_count")
    private Integer playCount;  // 播放数

    @Column(name = "collect_video_count")
    private Integer collectVideoCount;

    @Column(name = "collect_column_count")
    private Integer collectColumnCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    @Column(name = "create_date")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    @Column(name = "delete_date")
    private Date deleteDate;

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getFollowsCount() {
        return followsCount;
    }

    public void setFollowsCount(Integer followsCount) {
        this.followsCount = followsCount;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Integer getLoveCount() {
        return loveCount;
    }

    public void setLoveCount(Integer loveCount) {
        this.loveCount = loveCount;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Double getCoin() {
        return coin;
    }

    public void setCoin(Double coin) {
        this.coin = coin;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }
}
