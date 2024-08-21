package com.bilimili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.bilimili.video.converter.CommentConverter;
import com.bilimili.video.dao.Comment;
import com.bilimili.video.dto.CommentDTO;
import com.bilimili.video.mapper.CommentMapper;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.CommentService;
import com.bilimili.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private VideoService videoService;

    @Autowired
    private CommentConverter commentConverter;


    /**
     * 根据视频 vid 获取根评论列表，
     * @param vid 视频 id
     * @return List<Comment>
     */
    @Override
    public List<Comment> getRootCommentsByVid(Integer vid) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("vid", vid).eq("parent_id", 0);
        wrapper.orderByDesc("create_time");
        return commentMapper.selectList(wrapper);
    }

    /**
     * @param parentId 根级节点的评论 id, 即楼层 id
     * @param vid 视频的 vid
     * @return 1. 根据 redis 查找出回复该评论的子评论 id 列表
     * 2. 根据 id 多线程查询出所有评论的详细信息
     */
    @Override
    public List<Comment> getChildCommentsByParentId(Integer vid, Integer parentId) {

        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("vid", vid).eq("parent_id", parentId);
        wrapper.orderByDesc("create_time");

        return commentMapper.selectList(wrapper);
    }


    /**
     * 发送评论，字数不得大于2000或为空
     *
     * @param vid      视频id
     * @param sid      发布者uid
     * @param parentId 被回复的评论id
     * @param toUserSid 被回复用户uid
     * @param content  评论内容
     * @return true 发送成功 false 发送失败
     */
    @Override
    @Transactional
    public Comment sendComment(Integer vid, String sid, Integer parentId, String toUserSid, String content) {
        if (content == null || content.isEmpty() || content.length() > 2000) return null;
        Date now = new Date();
        Comment comment = new Comment(
                null,
                vid,
                sid,
                parentId,
                toUserSid,
                content,
                now
        );
        System.out.println(comment);
        commentMapper.insert(comment);
        // 更新视频评论 + 1
        videoService.updateStats(comment.getVid(), "comment", true, 1);

        return comment;
    }

    @Override
    public List<CommentDTO> getAllComments() {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<CommentDTO> commentDTOList = new ArrayList<>();
        List<Comment> commentList = commentMapper.selectList(wrapper);
        for (Comment comment: commentList) {
            commentDTOList.add(commentConverter.commentConverter(comment));
        }
        return commentDTOList;
    }

    @Override
    public List<CommentDTO> getAllCommentsByVid(Integer vid) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("vid", vid).orderByDesc("create_time");
        List<CommentDTO> commentDTOList = new ArrayList<>();
        List<Comment> commentList = commentMapper.selectList(wrapper);
        for (Comment comment: commentList) {
            commentDTOList.add(commentConverter.commentConverter(comment));
        }
        return commentDTOList;
    }

    /**
     * 删除评论
     *
     * @param cid 评论id
     * @return 响应对象
     */
    @Override
    @Transactional
    public CustomResponse deleteComment(Integer cid) {
        CustomResponse customResponse = new CustomResponse();
        QueryWrapper<Comment> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("cid", cid);
        Comment comment = commentMapper.selectOne(queryWrapper1);


            if (Objects.equals(comment.getParentId(), 0)) {
                // 查询总共要减少多少评论数
                QueryWrapper<Comment> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("parent_id", cid);
                Long count = commentMapper.selectCount(queryWrapper2);
                videoService.updateStats(comment.getVid(), "comment", false, Math.toIntExact(count));
                commentMapper.delete(queryWrapper2);
            }

            videoService.updateStats(comment.getVid(), "comment", false, 1);
            commentMapper.delete(queryWrapper1);

            customResponse.setCode(200);
            customResponse.setMessage("删除成功!");

        return customResponse;
    }

}

