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
public class PostDTO {
    private Integer pid;         // 动态id
    private String type;
    private String sid;     // 动态发送者sid
    private Integer vid;
    private String picture;
    private String content;     // 消息内容
    private String name;
    private String signature;
    private String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date createTime;          // 发送消息的时间
    private VideoDTO videoDTO;
}
