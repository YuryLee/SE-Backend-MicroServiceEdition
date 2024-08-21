package com.bilimili.user.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

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

    @Getter
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

    @Setter
    @Getter
    @Column(name = "avatar")
    private String avatar;
    @Setter
    @Getter
    @Column(name = "background")
    private String background;
    @Setter
    @Getter
    @Column(name = "gender")
    private Integer gender; // 性别，0女性 1男性 2无性别，默认2
    @Setter
    @Getter
    @Column(name = "signature")
    private String signature;
    @Setter
    @Getter
    @Column(name = "exp")
    private Integer exp;    // 经验值 50/200/1500/4500/10800/28800 分别是0~6级的区间
    @Setter
    @Getter
    @Column(name = "coin")
    private Double coin;    // 硬币数 保留一位小数
    @Setter
    @Getter
    @Column(name = "vip")
    private Integer vip;    // 0 普通用户，1 月度大会员，2 季度大会员，3 年度大会员
    @Setter
    @Getter
    @Column(name = "state")
    private Integer state;  // 0 正常，1 封禁中，2 已注销
    @Setter
    @Getter
    @Column(name = "role")
    private Integer role;   // 0 普通用户，1 普通管理员，2 超级管理员

    @Setter
    @Getter
    @Column(name = "video_count")
    private Integer videoCount; // 视频投稿数
    @Setter
    @Getter
    @Column(name = "follows_count")
    private Integer followsCount;   // 关注数
    @Setter
    @Getter
    @Column(name = "fans_count")
    private Integer fansCount;  // 粉丝数
    @Setter
    @Getter
    @Column(name = "love_count")
    private Integer loveCount;  // 获赞数
    @Setter
    @Getter
    @Column(name = "play_count")
    private Integer playCount;  // 播放数

    @Column(name = "collect_video_count")
    private Integer collectVideoCount;

    @Column(name = "collect_column_count")
    private Integer collectColumnCount;

    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    @Column(name = "create_date")
    private Date createDate;

    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    @Column(name = "delete_date")
    private Date deleteDate;

}
