package com.bilimili.video.dao;

import com.bilimili.video.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentTree {
    private Integer cid;
    private Integer vid;
    private Integer rootId;
    private Integer parentId;
    private String content;
    private UserDTO user;
    private UserDTO toUser;
    private List<CommentTree> replies;
    private Date createTime;
    private Long count;
}
