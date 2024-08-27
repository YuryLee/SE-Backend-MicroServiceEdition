package com.bilimili.search.controller;

import com.bilimili.search.converter.UserConverter;
import com.bilimili.search.converter.VideoConverter;
import com.bilimili.search.dao.User;
import com.bilimili.search.dao.Video;
import com.bilimili.search.dao.Zhuanlan;
import com.bilimili.search.dto.UserDTO;
import com.bilimili.search.dto.VideoDTO;
import com.bilimili.search.response.CustomResponse;
import com.bilimili.search.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SearchControllerTest {
    @InjectMocks
    private SearchController controller;

    @Mock
    private VideoConverter videoConverter;

    @Mock
    private SearchService searchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchUsersPositive() {
        String text = "text";
        User user = new User();
        List<User> userList = new ArrayList<>();
        UserDTO userDTO = new UserDTO();
        userList.add(user);
        try (MockedStatic<UserConverter> mockedStatic = mockStatic(UserConverter.class)) {
            when(searchService.searchUsers(text)).thenReturn(userList);
            when(UserConverter.convertUser(user)).thenReturn(userDTO);
            CustomResponse actualResponse = controller.searchUsers(text);
            assertNotNull(actualResponse);
            assertEquals(1, ((List<?>) actualResponse.getData()).size());
            assertSame(userDTO, ((List<?>) actualResponse.getData()).get(0));
            verify(searchService, times(1)).searchUsers(text);
            mockedStatic.verify(() -> UserConverter.convertUser(user), times(1));
        }
    }

    @Test
    void testSearchUsersNegative() {
        String text = "text";
        List<User> userList = new ArrayList<>();
        when(searchService.searchUsers(text)).thenReturn(userList);
        CustomResponse actualResponse = controller.searchUsers(text);
        assertNotNull(actualResponse);
        assertEquals(((List<?>) actualResponse.getData()).size(), 0);
        verify(searchService, times(1)).searchUsers(text);
    }

    @Test
    void testSearchVideosPositive() {
        String text = "text";
        Video video = new Video();
        List<Video> videoList = new ArrayList<>();
        VideoDTO videoDTO = new VideoDTO();
        videoList.add(video);
        when(searchService.searchVideos(text)).thenReturn(videoList);
        when(videoConverter.Converter(video)).thenReturn(videoDTO);
        CustomResponse actualResponse = controller.searchVideos(text);
        assertNotNull(actualResponse);
        assertEquals(1, ((List<?>) actualResponse.getData()).size());
        assertSame(videoDTO, ((List<?>) actualResponse.getData()).get(0));
        verify(searchService, times(1)).searchVideos(text);
        verify(videoConverter, times(1)).Converter(video);
    }

    @Test
    void testSearchVideosNegative() {
        String text = "text";
        List<Video> videoList = new ArrayList<>();

        when(searchService.searchVideos(text)).thenReturn(videoList);
        CustomResponse actualResponse = controller.searchVideos(text);
        assertNotNull(actualResponse);
        assertTrue(((List<?>) actualResponse.getData()).isEmpty());
        verify(searchService, times(1)).searchVideos(text);
        verify(videoConverter, never()).Converter(any());
    }

    @Test
    void testSearchZhuanLansPositive() {
        String text = "text";
        Zhuanlan zhuanlan = new Zhuanlan();
        List<Zhuanlan> zhuanlanList = new ArrayList<>();
        zhuanlanList.add(zhuanlan);
        when(searchService.searchZhuanlans(text)).thenReturn(zhuanlanList);
        CustomResponse actualResponse = controller.searchZhuanlans(text);
        assertNotNull(actualResponse);
        assertEquals(1, ((List<?>) actualResponse.getData()).size());
        assertSame(zhuanlan, ((List<?>) actualResponse.getData()).get(0));
        verify(searchService, times(1)).searchZhuanlans(text);
    }

    @Test
    void testSearchZhuanLansNegative() {
        String text = "text";
        List<Zhuanlan> zhuanlanList = new ArrayList<>();
        when(searchService.searchZhuanlans(text)).thenReturn(zhuanlanList);
        CustomResponse actualResponse = controller.searchZhuanlans(text);
        assertNotNull(actualResponse);
        assertTrue(((List<?>) actualResponse.getData()).isEmpty());
        verify(searchService, times(1)).searchZhuanlans(text);
    }
}
