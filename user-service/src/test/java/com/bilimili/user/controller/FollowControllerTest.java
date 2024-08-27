package com.bilimili.user.controller;

import com.bilimili.user.dto.UserDTO;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.service.FollowService;
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
public class FollowControllerTest {
    @InjectMocks
    FollowController followController;

    @Mock
    private FollowService followService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrCancelFollowPositive() {
        String sidFrom = "00000001";
        String sidTo = "00000002";

        CustomResponse expectedResponse = new CustomResponse();
        when(followService.addOrCancelFollow(sidFrom, sidTo)).thenReturn(expectedResponse);
        CustomResponse actualResponse = followController.addOrCancelFollow(sidFrom, sidTo);
        assertSame(expectedResponse, actualResponse);
        verify(followService, times(1)).addOrCancelFollow(sidFrom, sidTo);
    }

    @Test
    void testAddOrCancelFollowNegative() {
        when(followService.addOrCancelFollow(null, null)).thenReturn(null);
        CustomResponse actualResponse = followController.addOrCancelFollow(null, null);
        assertNull(actualResponse);
        verify(followService, times(1)).addOrCancelFollow(null, null);
    }

    @Test
    void testIsFollowedPositive() {
        String sidFrom = "00000001";
        String sidTo = "00000002";
        when(followService.isFollowed(sidFrom, sidTo)).thenReturn(true);
        CustomResponse actualResponse = followController.isFollowed(sidFrom, sidTo);
        assertNotNull(actualResponse);
        assertTrue((boolean) actualResponse.getData());
        verify(followService, times(1)).isFollowed(sidFrom, sidTo);
    }

    @Test
    void testIsFollowedNegative() {
        when(followService.isFollowed(null, null)).thenReturn(false);
        CustomResponse actualResponse = followController.isFollowed(null, null);
        assertNotNull(actualResponse);
        assertFalse((boolean) actualResponse.getData());
        verify(followService, times(1)).isFollowed(null, null);
    }

    @Test
    void testGetFansPositive() {
        String sid = "00000001";
        UserDTO userDTO = new UserDTO();
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);
        when(followService.getFans(sid)).thenReturn(userDTOList);
        CustomResponse actualResponse = followController.getFans(sid);
        assertNotNull(actualResponse);
        assertEquals(1, ((List<?>) actualResponse.getData()).size());
        assertSame(userDTO, ((List<?>) actualResponse.getData()).get(0));
        verify(followService, times(1)).getFans(sid);
    }

    @Test
    void testGetFansNegative() {
        when(followService.getFans(null)).thenReturn(new ArrayList<>());
        CustomResponse actualResponse = followController.getFans(null);
        assertNotNull(actualResponse);
        assertTrue(((List<?>) actualResponse.getData()).isEmpty());
        verify(followService, times(1)).getFans(null);
    }

    @Test
    void testGetFollowsPositive() {
        String sid = "00000001";
        UserDTO userDTO = new UserDTO();
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);
        when(followService.getFollows(sid)).thenReturn(userDTOList);
        CustomResponse actualResponse = followController.getFollows(sid);
        assertNotNull(actualResponse);
        assertEquals(1, ((List<?>) actualResponse.getData()).size());
        assertSame(userDTO, ((List<?>) actualResponse.getData()).get(0));
        verify(followService, times(1)).getFollows(sid);
    }

    @Test
    void testGetFollowsNegative() {
        when(followService.getFollows(null)).thenReturn(new ArrayList<>());
        CustomResponse actualResponse = followController.getFollows(null);
        assertNotNull(actualResponse);
        assertTrue(((List<?>) actualResponse.getData()).isEmpty());
        verify(followService, times(1)).getFollows(null);
    }
}
