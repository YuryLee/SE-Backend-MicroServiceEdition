package com.bilimili.video.controller;

import com.bilimili.video.converter.CommentConverter;
import com.bilimili.video.dao.Comment;
import com.bilimili.video.dto.CommentDTO;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentControllerTest {
    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Mock
    private CommentConverter commentConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRootCommentByVidPositive() {
        Integer vid = 100;
        List<Comment> mockComments = new ArrayList<>();
        Comment mockComment = new Comment();
        mockComments.add(mockComment);

        CommentDTO mockCommentDTO = new CommentDTO();

        when(commentService.getRootCommentsByVid(vid)).thenReturn(mockComments);
        when(commentConverter.commentConverter(mockComment)).thenReturn(mockCommentDTO);

        CustomResponse response = commentController.getRootCommentByVid(vid);

        assertNotNull(response);
        assertEquals(1, ((List<?>) response.getData()).size());
        assertSame(mockCommentDTO, ((List<?>) response.getData()).get(0));
        verify(commentService, times(1)).getRootCommentsByVid(vid);
        verify(commentConverter, times(1)).commentConverter(mockComment);
    }

    @Test
    void testGetRootCommentByVidNegative() {
        Integer vid = -1;
        when(commentService.getRootCommentsByVid(vid)).thenReturn(new ArrayList<>());

        CustomResponse response = commentController.getRootCommentByVid(vid);

        assertNotNull(response);
        assertEquals(0, ((List<?>) response.getData()).size());
        verify(commentService, times(1)).getRootCommentsByVid(vid);
        verify(commentConverter, never()).commentConverter(any());
    }

    @Test
    void testGetChildCommentByVidPositive() {
        Integer vid = 100;
        Integer parentId = 200;
        List<Comment> mockComments = new ArrayList<>();
        Comment mockComment = new Comment();
        mockComments.add(mockComment);

        CommentDTO mockCommentDTO = new CommentDTO();

        when(commentService.getChildCommentsByParentId(vid, parentId)).thenReturn(mockComments);
        when(commentConverter.commentConverter(mockComment)).thenReturn(mockCommentDTO);

        CustomResponse response = commentController.getChildCommentByVid(vid, parentId);

        assertNotNull(response);
        assertEquals(1, ((List<?>) response.getData()).size());
        assertSame(mockCommentDTO, ((List<?>) response.getData()).get(0));
        verify(commentService, times(1)).getChildCommentsByParentId(vid, parentId);
        verify(commentConverter, times(1)).commentConverter(mockComment);
    }

    @Test
    void testGetChildCommentByVidNegative() {
        Integer vid = -1;
        Integer parentId = 200;
        when(commentService.getChildCommentsByParentId(vid, parentId)).thenReturn(new ArrayList<>());

        CustomResponse response = commentController.getChildCommentByVid(vid, parentId);

        assertNotNull(response);
        assertEquals(0, ((List<?>) response.getData()).size());

        verify(commentService, times(1)).getChildCommentsByParentId(vid, parentId);
        verify(commentConverter, never()).commentConverter(any());
    }

    @Test
    void testAddCommentPositive() {
        Integer vid = 100;
        String sid = "00000001";
        Integer parentId = 200;
        String toUserSid = "00000002";
        String content = "Hello World";

        Comment mockComment = new Comment();

        when(commentService.sendComment(vid, sid, parentId, toUserSid, content)).thenReturn(mockComment);
        CustomResponse response = commentController.addComment(vid, sid, parentId, toUserSid, content);

        assertNotNull(response);
        assertSame(response.getData(), mockComment);
        assertEquals(200, response.getCode());
        verify(commentService, times(1)).sendComment(vid, sid, parentId, toUserSid, content);
    }

    @Test
    void testAddCommentNegative() {
        Integer vid = 100;
        String sid = "00000001";
        Integer parentId = 200;
        String toUserSid = "00000002";
        String content = "Hello World";

        when(commentService.sendComment(vid, sid, parentId, toUserSid, content)).thenReturn(null);
        CustomResponse response = commentController.addComment(vid, sid, parentId, toUserSid, content);
        assertNull(response.getData());
        assertEquals(500, response.getCode());
        assertEquals("发送失败！", response.getMessage());
        verify(commentService, times(1)).sendComment(vid, sid, parentId, toUserSid, content);

    }

    @Test
    void testDelCommentPositive() {
        Integer cid = 100;
        CustomResponse customResponse = new CustomResponse();
        when(commentService.deleteComment(cid)).thenReturn(customResponse);
        CustomResponse response = commentController.delComment(cid);
        assertSame(customResponse, response);
        verify(commentService, times(1)).deleteComment(cid);
    }

    @Test
    void testDelCommentNegative() {
        Integer cid = 999;
        when(commentService.deleteComment(cid)).thenReturn(null);
        CustomResponse response = commentController.delComment(cid);
        assertNull(response);
        verify(commentService, times(1)).deleteComment(cid);
    }

    @Test
    void testGetAllCommentPositive() {
        CommentDTO commentDTO = new CommentDTO();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentDTOList.add(commentDTO);
        when(commentService.getAllComments()).thenReturn(commentDTOList);

        CustomResponse customResponse = commentController.getAllComments();

        assertNotNull(customResponse);
        assertEquals(1, ((List<?>) customResponse.getData()).size());
        assertSame(commentDTO, ((List<?>) customResponse.getData()).get(0));
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    void testGetAllCommentNegative() {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        when(commentService.getAllComments()).thenReturn(commentDTOList);
        CustomResponse customResponse = commentController.getAllComments();
        assertNotNull(customResponse);
        assertEquals(0, ((List<?>) customResponse.getData()).size());
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    void testGetAllCommentByVidPositive() {
        Integer vid = 100;
        CommentDTO commentDTO = new CommentDTO();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentDTOList.add(commentDTO);
        when(commentService.getAllCommentsByVid(vid)).thenReturn(commentDTOList);

        CustomResponse customResponse = commentController.getAllCommentsByVid(vid);

        assertNotNull(customResponse);
        assertEquals(1, ((List<?>) customResponse.getData()).size());
        assertSame(commentDTO, ((List<?>) customResponse.getData()).get(0));
        verify(commentService, times(1)).getAllCommentsByVid(vid);
    }

    @Test
    void testGetAllCommentByVidNegative() {
        Integer vid = -1;
        List<CommentDTO> commentDTOList = new ArrayList<>();
        when(commentService.getAllCommentsByVid(vid)).thenReturn(commentDTOList);
        CustomResponse customResponse = commentController.getAllCommentsByVid(vid);

        assertNotNull(customResponse);
        assertEquals(0, ((List<?>) customResponse.getData()).size());
        verify(commentService, times(1)).getAllCommentsByVid(-1);
    }
}