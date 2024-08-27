package com.bilimili.specol.controller;

import com.bilimili.specol.dao.Zhuanlan;
import com.bilimili.specol.dto.VideoDTO;
import com.bilimili.specol.dto.ZhuanlanDTO;
import com.bilimili.specol.response.CustomResponse;
import com.bilimili.specol.service.ZhuanlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ZhuanlanControllerTest {
    @InjectMocks
    private ZhuanlanController playlistController;

    @Mock
    private ZhuanlanService playlistService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePlaylistPositive() {
        MultipartFile cover = mock(MultipartFile.class);
        CustomResponse expectedResponse = new CustomResponse(200, "成功", null);
        try {
            when(playlistService.addZhuanlan(anyString(), anyString(), anyString(), anyInt(), any(MultipartFile.class)))
                    .thenReturn(expectedResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse actualResponse;
        try {
            actualResponse = playlistController.createZhuanlan("sid", "title", "desc", 1, cover);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertSame(expectedResponse, actualResponse);
        try {
            verify(playlistService).addZhuanlan(anyString(), anyString(), anyString(), anyInt(), any(MultipartFile.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void testCreatePlaylistNegative() {
        MultipartFile cover = mock(MultipartFile.class);
        try {
            when(playlistService.addZhuanlan(anyString(), anyString(), anyString(), anyInt(), any(MultipartFile.class)))
                    .thenThrow(new IOException("Test Exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse actualResponse;
        try {
            actualResponse = playlistController.createZhuanlan("sid", "title", "desc", 1, cover);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCode());
        assertEquals("封面上传失败", actualResponse.getMessage());
        try {
            verify(playlistService, times(1)).addZhuanlan(anyString(), anyString(), anyString(), anyInt(), any(MultipartFile.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDeletePlaylistPositive() {
        CustomResponse expectedResponse = new CustomResponse(200, "成功", null);
        when(playlistService.delZhuanlan(anyInt())).thenReturn(expectedResponse);

        CustomResponse actualResponse;
        try {
            actualResponse = playlistController.createZhuanlan(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertSame(expectedResponse, actualResponse);
        verify(playlistService, times(1)).delZhuanlan(anyInt());
    }


    @Test
    void testDeletePlaylistNegative() {
        when(playlistService.delZhuanlan(anyInt())).thenThrow(new RuntimeException("Test Exception"));

        CustomResponse actualResponse;
        try {
            actualResponse = playlistController.createZhuanlan(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCode());
        assertEquals("删除失败", actualResponse.getMessage());
        verify(playlistService, times(1)).delZhuanlan(anyInt());
    }

    @Test
    void testUpdatePlaylistPositive() {
        Zhuanlan playlist = new Zhuanlan();
        when(playlistService.updateZhuanlan(anyInt(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(playlist);

        CustomResponse actualResponse;
        try {
            actualResponse = playlistController.updateZhuanlan(1, "sid", "title", "desc", 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(actualResponse);
        assertEquals(200, actualResponse.getCode());
        assertEquals("修改成功", actualResponse.getMessage());
        assertSame(playlist, actualResponse.getData());
        verify(playlistService, times(1)).updateZhuanlan(anyInt(), anyString(), anyString(), anyString(), anyInt());
    }


    @Test
    void testUpdatePlaylistNegative() {
        when(playlistService.updateZhuanlan(anyInt(), anyString(), anyString(), anyString(), anyInt()))
                .thenThrow(new RuntimeException("Test Exception"));

        CustomResponse actualResponse;
        try {
            actualResponse = playlistController.updateZhuanlan(1, "sid", "title", "desc", 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCode());
        assertEquals("修改失败", actualResponse.getMessage());
        assertNull(actualResponse.getData());
        verify(playlistService, times(1)).updateZhuanlan(anyInt(), anyString(), anyString(), anyString(), anyInt());
    }


    @Test
    void testUpdatePlaylistVisiblePositive() {
        Zhuanlan playlist = new Zhuanlan();
        when(playlistService.updateZhuanlanVisible(anyInt(), anyInt())).thenReturn(playlist);

        CustomResponse actualResponse;
        try {
            actualResponse = playlistController.updateZhuanlan(1, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(actualResponse);
        assertEquals(200, actualResponse.getCode());
        assertEquals("修改成功", actualResponse.getMessage());
        assertSame(playlist, actualResponse.getData());
        verify(playlistService, times(1)).updateZhuanlanVisible(anyInt(), anyInt());
    }


    @Test
    void testUpdatePlaylistVisibleNegative() {
        when(playlistService.updateZhuanlanVisible(anyInt(), anyInt())).thenThrow(new RuntimeException("Test Exception"));

        CustomResponse actualResponse;
        try {
            actualResponse = playlistController.updateZhuanlan(1, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCode());
        assertEquals("修改失败", actualResponse.getMessage());
        assertNull(actualResponse.getData());
        verify(playlistService, times(1)).updateZhuanlanVisible(anyInt(), anyInt());
    }

    @Test
    void testAddVideosToPlaylistPositive() {
        CustomResponse expectedResponse = new CustomResponse(200, "成功", null);
        when(playlistService.addVideosToZhuanlan(anyInt(), anyList())).thenReturn(expectedResponse);

        CustomResponse actualResponse = playlistController.addVideosToZhuanlan(1, "1,2,3");

        assertSame(expectedResponse, actualResponse);
        verify(playlistService, times(1)).addVideosToZhuanlan(anyInt(), anyList());
    }

    @Test
    void testAddVideosToPlaylistNegative() {
        CustomResponse expectedResponse = new CustomResponse(500, "失败", null);
        when(playlistService.addVideosToZhuanlan(anyInt(), anyList())).thenReturn(expectedResponse);

        CustomResponse actualResponse = playlistController.addVideosToZhuanlan(1, "1,2,3");

        assertSame(expectedResponse, actualResponse);
        verify(playlistService, times(1)).addVideosToZhuanlan(anyInt(), anyList());
    }

    @Test
    void testUpdateVideosFromPlaylistPositive() {
        CustomResponse expectedResponse = new CustomResponse(200, "成功", null);
        when(playlistService.updateVideosFromZhuanlan(anyInt(), anyList())).thenReturn(expectedResponse);

        CustomResponse actualResponse = playlistController.updateVideosFromZhuanlan(1, "1,2,3");

        assertSame(expectedResponse, actualResponse);
        verify(playlistService, times(1)).updateVideosFromZhuanlan(anyInt(), anyList());
    }

    @Test
    void testUpdateVideosFromPlaylistNegative() {
        CustomResponse expectedResponse = new CustomResponse(500, "失败", null);
        when(playlistService.updateVideosFromZhuanlan(anyInt(), anyList())).thenReturn(expectedResponse);

        CustomResponse actualResponse = playlistController.updateVideosFromZhuanlan(1, "1,2,3");

        assertSame(expectedResponse, actualResponse);
        verify(playlistService, times(1)).updateVideosFromZhuanlan(anyInt(), anyList());
    }

    @Test
    void testGetAllPlaylistForUserPositive() {
        List<Zhuanlan> playlistList = new ArrayList<>();
        when(playlistService.getZhuanlans(anyString(), anyBoolean())).thenReturn(playlistList);

        CustomResponse actualResponse = playlistController.getAllZhuanlansForUser("sid1", "sid2");

        assertNotNull(actualResponse);
        assertEquals(playlistList, actualResponse.getData());
        verify(playlistService, times(1)).getZhuanlans(anyString(), anyBoolean());
    }

    @Test
    void testGetAllPlaylistForUserNegative() {
        List<Zhuanlan> playlistList = new ArrayList<>();
        when(playlistService.getZhuanlans(anyString(), anyBoolean())).thenReturn(playlistList);
        CustomResponse actualResponse = playlistController.getAllZhuanlansForUser("sid1", "sid1");

        assertNotNull(actualResponse);
        assertEquals(playlistList, actualResponse.getData());
        verify(playlistService, times(1)).getZhuanlans(anyString(), anyBoolean());
    }

    @Test
    void testGetAllPlaylistForVisitorPositive() {
        List<Zhuanlan> playlistList = new ArrayList<>();
        when(playlistService.getZhuanlans(anyString(), anyBoolean())).thenReturn(playlistList);
        CustomResponse actualResponse = playlistController.getAllZhuanlansForVisitor("sid1");

        assertNotNull(actualResponse);
        assertEquals(playlistList, actualResponse.getData());
        verify(playlistService, times(1)).getZhuanlans(anyString(), anyBoolean());
    }

    @Test
    void testGetAllPlaylistForVisitorNegative() {
        List<Zhuanlan> playlistList = new ArrayList<>();
        when(playlistService.getZhuanlans(anyString(), anyBoolean())).thenReturn(playlistList);
        CustomResponse actualResponse = playlistController.getAllZhuanlansForVisitor("sid1");

        assertNotNull(actualResponse);
        assertEquals(playlistList, actualResponse.getData());
        verify(playlistService, times(1)).getZhuanlans(anyString(), anyBoolean());
    }

    @Test
    void testGetAllPlaylistPositive() {
        List<Zhuanlan> playlistList = new ArrayList<>();
        when(playlistService.getAllZhuanlans()).thenReturn(playlistList);
        CustomResponse actualResponse = playlistController.getAllZhuanlans();

        assertNotNull(actualResponse);
        assertEquals(playlistList, actualResponse.getData());
        verify(playlistService, times(1)).getAllZhuanlans();

    }

    @Test
    void testGetAllPlaylistNegative() {
        List<Zhuanlan> playlistList = new ArrayList<>();
        when(playlistService.getAllZhuanlans()).thenReturn(playlistList);
        CustomResponse actualResponse = playlistController.getAllZhuanlans();

        assertNotNull(actualResponse);
        assertEquals(playlistList, actualResponse.getData());
        verify(playlistService, times(1)).getAllZhuanlans();
    }

    @Test
    void testGetAllVideosByZidPositive() {
        List<VideoDTO> videoDTOList = new ArrayList<>();
        when(playlistService.getAllVideosByZid(anyInt())).thenReturn(videoDTOList);
        CustomResponse actualResponse = playlistController.getAllVideosByZid(1);
        assertNotNull(actualResponse);
        assertEquals(videoDTOList, actualResponse.getData());
        verify(playlistService, times(1)).getAllVideosByZid(anyInt());
    }

    @Test
    void testGetAllVideosByZidNegative() {
        when(playlistService.getAllVideosByZid(anyInt())).thenReturn(null);
        CustomResponse actualResponse = playlistController.getAllVideosByZid(1);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(playlistService, times(1)).getAllVideosByZid(anyInt());
    }

    @Test
    void testGetAllDataByZidPositive() {
        ZhuanlanDTO playlistDTO = new ZhuanlanDTO();
        when(playlistService.getAllDataByZid(anyInt())).thenReturn(playlistDTO);
        CustomResponse actualResponse = playlistController.getAllDataByZid(1);
        assertNotNull(actualResponse);
        assertEquals(playlistDTO, actualResponse.getData());
        verify(playlistService, times(1)).getAllDataByZid(anyInt());
    }

    @Test
    void testGetAllDataByZidNegative() {
        when(playlistService.getAllDataByZid(anyInt())).thenReturn(null);
        CustomResponse actualResponse = playlistController.getAllDataByZid(1);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(playlistService, times(1)).getAllDataByZid(anyInt());
    }

    @Test
    void testGetVideosNotInZidPositive() {
        List<VideoDTO> videoDTOList = new ArrayList<>();
        when(playlistService.getVideosNotInZid(anyString(), anyInt())).thenReturn(videoDTOList);
        CustomResponse actualResponse = playlistController.getVideosNotInZid("test", 1);
        assertNotNull(actualResponse);
        assertEquals(videoDTOList, actualResponse.getData());
        verify(playlistService, times(1)).getVideosNotInZid(anyString(), anyInt());
    }

    @Test
    void testGetVideosNotInZidNegative() {
        when(playlistService.getVideosNotInZid(anyString(), anyInt())).thenReturn(null);
        CustomResponse actualResponse = playlistController.getVideosNotInZid("test", 1);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(playlistService, times(1)).getVideosNotInZid(anyString(), anyInt());
    }

    @Test
    void testUpdateVideoStatusPositive() {
        CustomResponse expectedResponse = new CustomResponse(200, "操作成功", null);
        when(playlistService.updateZhuanlanStatus(1, 1)).thenReturn(expectedResponse);
        CustomResponse actualResponse = playlistController.updateVideoStatus(1, 1);
        assertNotNull(actualResponse);
        assertSame(expectedResponse, actualResponse);
        verify(playlistService, times(1)).updateZhuanlanStatus(1, 1);
    }

    @Test
    void testUpdateVideoStatusNegative() {
        when(playlistService.updateZhuanlanStatus(1, 1)).thenThrow(new RuntimeException("Test exception"));
        CustomResponse actualResponse = playlistController.updateVideoStatus(1, 1);
        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCode());
        assertEquals("操作失败", actualResponse.getMessage());
        assertNull(actualResponse.getData());
        verify(playlistService, times(1)).updateZhuanlanStatus(1, 1);
    }

    @Test
    void testCollectVideoPositive() {
        CustomResponse expectedResponse = new CustomResponse();
        when(playlistService.collectVideos(anyString(), anyString())).thenReturn(expectedResponse);
        CustomResponse actualResponse = playlistController.collectVideo("test", "test");
        assertSame(expectedResponse, actualResponse);
        verify(playlistService, times(1)).collectVideos(anyString(), anyString());
    }

    @Test
    void testCollectVideoNegative() {
        when(playlistService.collectVideos(anyString(), anyString())).thenReturn(null);
        CustomResponse actualResponse = playlistController.collectVideo("test", "test");
        assertNull(actualResponse);
        verify(playlistService, times(1)).collectVideos(anyString(), anyString());
    }
}
