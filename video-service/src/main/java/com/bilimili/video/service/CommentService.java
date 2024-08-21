package com.bilimili.video.service;

import com.bilimili.video.dao.Comment;
import com.bilimili.video.dto.CommentDTO;
import com.bilimili.video.response.CustomResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {

    List<Comment> getRootCommentsByVid(Integer vid);

    List<Comment> getChildCommentsByParentId(Integer vid, Integer parentId);

    CustomResponse deleteComment(Integer cid);

    @Transactional
    Comment sendComment(Integer vid, String sid, Integer parentId, String toUserSid, String content);

    List<CommentDTO> getAllComments();

    List<CommentDTO> getAllCommentsByVid(Integer vid);
}
