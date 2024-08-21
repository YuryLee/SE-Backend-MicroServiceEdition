package com.bilimili.user.dao;

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
public class DailyWatch {
    @TableId(type = IdType.AUTO)
    private Integer id;     // ID
    private String sid;    // 用户ID
    private Integer weekday;
    private Integer loveCount;
    private Integer collectCount;
    private Integer playCount;
    private Integer newPlayCount;
    private Integer userPlayCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date storeDate;    // 日期时间
}
