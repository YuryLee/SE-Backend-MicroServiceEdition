package com.bilimili.video.converter;


import com.bilimili.video.clients.UserClient;
import com.bilimili.video.dao.Comment;
import com.bilimili.video.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author Yury
 */
@Component
public class CommentConverter {
    @Autowired
    private UserClient userClient;
    public CommentDTO commentConverter(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCid(comment.getCid());
        commentDTO.setVid(comment.getVid());
        commentDTO.setSid(comment.getSid());
        commentDTO.setFromUser(userClient.getUserDTOBySid(comment.getSid()));
        commentDTO.setParentId(comment.getParentId());
        commentDTO.setToUserSid(comment.getToUserSid());
        commentDTO.setToUser(userClient.getUserDTOBySid(comment.getToUserSid()));
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreateTime(comment.getCreateTime());
        return commentDTO;
    }
}
