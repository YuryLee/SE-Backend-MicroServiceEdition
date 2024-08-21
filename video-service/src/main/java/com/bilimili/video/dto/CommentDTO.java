package com.bilimili.video.dto;

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
public class CommentDTO {
    @TableId(type = IdType.AUTO)
    private Integer cid;
    private Integer vid;
    private String sid;
    private UserDTO fromUser;
    private Integer parentId;
    private String toUserSid;
    private UserDTO toUser;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date createTime;
}
