package com.bilimili.video.controller;


import com.bilimili.video.converter.CommentConverter;
import com.bilimili.video.dao.Comment;
import com.bilimili.video.dto.CommentDTO;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@Tag(name = "评论api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentConverter commentConverter;

    @GetMapping("/comment/root")
    @Operation(summary = "获取vid的根评论")
    public CustomResponse getRootCommentByVid(@RequestParam("vid") Integer vid) {
        CustomResponse customResponse = new CustomResponse();
        List<Comment> commentList = commentService.getRootCommentsByVid(vid);
        List<CommentDTO> commentDTOList= new ArrayList<>();
        for (Comment comment: commentList) {
            commentDTOList.add(commentConverter.commentConverter(comment));
        }
        customResponse.setData(commentDTOList);
        return customResponse;
    }

    @GetMapping("/comment/child")
    @Operation(summary = "获取vid，parentId的子评论")
    public CustomResponse getChildCommentByVid(@RequestParam("vid") Integer vid, @RequestParam("parentId") Integer parentId) {
        CustomResponse customResponse = new CustomResponse();
        List<Comment> commentList = commentService.getChildCommentsByParentId(vid, parentId);
        List<CommentDTO> commentDTOList= new ArrayList<>();
        //System.out.println(commentList);
        for (Comment comment: commentList) {
            commentDTOList.add(commentConverter.commentConverter(comment));
        }
        customResponse.setData(commentDTOList);
        return customResponse;
    }

    /**
     * 发表评论
     * @param vid   视频id
     * @param parentId  被回复评论id
     * @param toUserSid  被回复者uid
     * @param content   评论内容
     * @return  响应对象
     */
    @PostMapping("/comment/add")
    @Operation(summary = "发表评论")
    public CustomResponse addComment(
            @RequestParam("vid") Integer vid,
            @RequestParam("sid") String sid,
            @RequestParam("parent_id") Integer parentId,
            @RequestParam(value = "to_user_sid", required = false) String toUserSid,
            @RequestParam("content") String content ) {

        CustomResponse customResponse = new CustomResponse();
        Comment comment = commentService.sendComment(vid, sid, parentId, toUserSid, content);
        if (comment == null) {
            customResponse.setCode(500);
            customResponse.setMessage("发送失败！");
        }
        customResponse.setData(comment);
        return customResponse;
    }

    /**
     * 删除评论
     * @param cid 评论id
     * @return  响应对象
     */
    @PostMapping("/comment/delete")
    @Operation(summary = "删除评论")
    public CustomResponse delComment(@RequestParam("cid") Integer cid) {
        return commentService.deleteComment(cid);
    }

    @GetMapping("/comment/all")
    @Operation(summary = "获取所有评论")
    public CustomResponse getAllComments() {
        CustomResponse customResponse = new CustomResponse();
        List<CommentDTO> commentDTOList = commentService.getAllComments();
        customResponse.setData(commentDTOList);
        return customResponse;
    }

    @GetMapping("/comment/vid/{vid}")
    @Operation(summary = "获取vid的所有评论")
    public CustomResponse getAllCommentsByVid(@PathVariable Integer vid) {
        CustomResponse customResponse = new CustomResponse();
        List<CommentDTO> commentDTOList = commentService.getAllCommentsByVid(vid);
        customResponse.setData(commentDTOList);
        return customResponse;
    }

}

