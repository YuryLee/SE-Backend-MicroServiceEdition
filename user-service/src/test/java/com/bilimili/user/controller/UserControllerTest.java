package com.bilimili.user.controller;

import com.bilimili.user.dto.UserDTO;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.response.Response;
import com.bilimili.user.response.ResponseList;
import com.bilimili.user.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@SpringBootTest
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private final String ECS_BUCKET_PATH = "src/test/resources/com/bilimili/user/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userController, "ECSBucketPath", ECS_BUCKET_PATH);
    }

    @Test
    void testGetUserBySidPositive() {
        String sid = "00000001";
        UserDTO userDTO = new UserDTO();
        when(userService.getUserBySid(sid)).thenReturn(userDTO);
        Response<UserDTO> response = userController.getUserBySid(sid);
        assertNotNull(response);
        assertSame(response.getData(), userDTO);
        assertTrue(response.isSuccess());
        verify(userService, times(1)).getUserBySid(sid);
    }

    @Test
    void testGetUserBySidNegative() {
        String sid = "nonexistentSid";
        when(userService.getUserBySid(sid)).thenThrow(new IllegalStateException());
        Response<String> response = userController.getUserBySid(sid);
        assertNotNull(response);
        assertEquals("sid: nonexistentSid doesn't exist", response.getMessage());
        assertFalse(response.isSuccess());
        verify(userService, times(1)).getUserBySid(sid);
    }

    @Test
    void testGetAllUsersPositive() {
        UserDTO userDTO = new UserDTO();
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);
        when(userService.findAll()).thenReturn(userDTOList);

        ResponseList<UserDTO> response = userController.getAllUsers();

        assertNotNull(response);
        assertSame(response.getData(), userDTOList);
        assertTrue(response.isSuccess());
        assertEquals(userDTOList.size(), response.getCount());
        verify(userService, times(1)).findAll();
    }

    @Test
    void testGetAllUsersNegative() {
        List<UserDTO> userDTOList = new ArrayList<>();
        when(userService.findAll()).thenReturn(userDTOList);

        ResponseList<UserDTO> response = userController.getAllUsers();
        assertNotNull(response);
        assertSame(response.getData(), userDTOList);
        assertTrue(response.isSuccess());
        assertEquals(userDTOList.size(), response.getCount());
        assertTrue(((List<?>) response.getData()).isEmpty());
        verify(userService, times(1)).findAll();
    }

    @Test
    void testGetUserListBySidPositive() {
        String sid = "00000001";
        UserDTO userDTO = new UserDTO();
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);
        when(userService.getUserBySid(sid)).thenReturn(userDTO);
        ResponseList<UserDTO> response = userController.getUserListBySid(sid);
        assertNotNull(response);
        assertEquals(userDTOList.size(), ((List<?>) response.getData()).size());
        assertEquals(1, ((List<?>) response.getData()).size());
        assertSame(userDTOList.get(0), ((List<?>) response.getData()).get(0));
        assertEquals(userDTOList.size(), response.getCount());
        assertTrue(response.isSuccess());
        verify(userService, times(1)).getUserBySid(sid);
    }

    @Test
    void testGetUserListBySidNegative() {
        String sid = "nonexistentSid";
        when(userService.getUserBySid(sid)).thenThrow(new IllegalStateException());
        ResponseList<UserDTO> response = userController.getUserListBySid(sid);
        assertNotNull(response);
        assertEquals("sid: nonexistentSid doesn't exist", response.getMessage());
        assertFalse(response.isSuccess());
        verify(userService, times(1)).getUserBySid(sid);
    }

    @Test
    void testGetUserListByNamePositive() {
        String name = "testName";
        UserDTO userDTO = new UserDTO();
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);
        when(userService.getUserByName(name)).thenReturn(userDTOList);
        ResponseList<UserDTO> response = userController.getUserListByName(name);
        assertNotNull(response);
        assertSame(response.getData(), userDTOList);
        assertTrue(response.isSuccess());
        assertEquals(userDTOList.size(), response.getCount());
        verify(userService, times(1)).getUserByName(name);
    }

    @Test
    void testGetUserListByNameNegative() {
        String name = "illegalName";
        when(userService.getUserByName(name)).thenThrow(new IllegalStateException());
        ResponseList<UserDTO> response = userController.getUserListByName(name);
        assertNotNull(response);
        assertEquals("name: illegalName doesn't exist", response.getMessage());
        assertFalse(response.isSuccess());
        verify(userService, times(1)).getUserByName(name);
    }

    @Test
    void testAddNewUserPositive() {
        UserDTO userDTO = new UserDTO();
        Long expected = (long) 1;
        when(userService.addNewUser(userDTO)).thenReturn(expected);
        Response<Long> actualResponse = userController.addNewUser(userDTO);
        assertNotNull(actualResponse);
        assertSame(expected, actualResponse.getData());
        assertTrue(actualResponse.isSuccess());
        verify(userService, times(1)).addNewUser(userDTO);
    }

    @Test
    void testAddNewUserNegative() {
        UserDTO userDTO = new UserDTO();
        Long expected = (long) -1;
        when(userService.addNewUser(userDTO)).thenReturn(expected);
        Response<String> actualResponse = userController.addNewUser(userDTO);
        assertNotNull(actualResponse);
        assertEquals(userDTO.getSid(), actualResponse.getData());
        assertFalse(actualResponse.isSuccess());
        verify(userService, times(1)).addNewUser(userDTO);
    }

    @Test
    void testRegisterPositive() {
        UserDTO userDTO = new UserDTO();
        CustomResponse expectedResponse = new CustomResponse();
        try {
            when(userService.register(userDTO.getSid(), userDTO.getName(), userDTO.getPassword())).thenReturn(expectedResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CustomResponse actualResponse = userController.register(userDTO);
        assertSame(expectedResponse, actualResponse);
        try {
            verify(userService, times(1)).register(userDTO.getSid(), userDTO.getName(), userDTO.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRegisterNegative() {
        UserDTO userDTO = new UserDTO();
        try {
            when(userService.register(userDTO.getSid(), userDTO.getName(), userDTO.getPassword())).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CustomResponse actualResponse = userController.register(userDTO);
        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCode());
        assertEquals("注册失败", actualResponse.getMessage());
        try {
            verify(userService, times(1)).register(userDTO.getSid(), userDTO.getName(), userDTO.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDeleteUserBySidPositive() {
        String sid = "sid";
        Long res = (long) 1;
        when(userService.deleteUserBySid(sid)).thenReturn(res);
        Response<String> response = userController.deleteUserBySid(sid);
        assertNotNull(response);
        assertEquals("delete successfully", response.getData());
        assertTrue(response.isSuccess());
        verify(userService, times(1)).deleteUserBySid(sid);
    }

    @Test
    void testDeleteUserBySidNegative() {
        String sid = "nonexistentSid";
        Long res = (long) -1;
        when(userService.deleteUserBySid(sid)).thenReturn(res);
        Response<String> response = userController.deleteUserBySid(sid);
        assertNotNull(response);
        assertEquals("sid: nonexistentSid doesn't exist", response.getMessage());
        assertFalse(response.isSuccess());
        verify(userService, times(1)).deleteUserBySid(sid);
    }

    @Test
    void testUpdateUserNameBySidPositive() {
        UserDTO userDTO = new UserDTO();
        when(userService.updateUserNameBySid(userDTO.getSid(), userDTO.getName())).thenReturn(userDTO);
        Response<UserDTO> response = userController.updateUserNameBySid(userDTO.getSid(), userDTO.getName());
        assertNotNull(response);
        assertEquals(userDTO, response.getData());
        assertTrue(response.isSuccess());
        verify(userService, times(1)).updateUserNameBySid(userDTO.getSid(), userDTO.getName());
    }

    @Test
    void testUpdateUserNameBySidNegative() {
        UserDTO userDTO = new UserDTO();
        userDTO.setSid("nonexistentSid");
        when(userService.updateUserNameBySid(userDTO.getSid(), userDTO.getName())).thenThrow(new IllegalStateException());
        Response<String> response = userController.updateUserNameBySid(userDTO.getSid(), userDTO.getName());
        assertNotNull(response);
        assertEquals("sid: nonexistentSid doesn't exist", response.getMessage());
        assertFalse(response.isSuccess());
        verify(userService, times(1)).updateUserNameBySid(userDTO.getSid(), userDTO.getName());
    }

    @Test
    void testUpdateUserPasswordBySidPositive() {
        UserDTO userDTO = new UserDTO();
        when(userService.updateUserPasswordBySid(userDTO.getSid(), userDTO.getPassword())).thenReturn(userDTO);
        Response<UserDTO> response = userController.updateUserPasswordBySid(userDTO.getSid(), userDTO.getPassword());
        assertNotNull(response);
        assertEquals(userDTO, response.getData());
        assertTrue(response.isSuccess());
        verify(userService, times(1)).updateUserPasswordBySid(userDTO.getSid(), userDTO.getPassword());
    }

    @Test
    void testUpdateUserPasswordBySidNegative() {
        UserDTO userDTO = new UserDTO();
        userDTO.setSid("nonexistentSid");
        when(userService.updateUserPasswordBySid(userDTO.getSid(), userDTO.getPassword())).thenThrow(new IllegalStateException());
        Response<UserDTO> response = userController.updateUserPasswordBySid(userDTO.getSid(), userDTO.getPassword());
        assertNotNull(response);
        assertEquals("sid: nonexistentSid doesn't exist", response.getMessage());
        assertFalse(response.isSuccess());
        verify(userService, times(1)).updateUserPasswordBySid(userDTO.getSid(), userDTO.getPassword());
    }

    @Test
    void testUpdateUserRoleBySidPositive() {
        UserDTO userDTO = new UserDTO();
        when(userService.updateUserRoleBySid(userDTO.getSid(), userDTO.getRole())).thenReturn(userDTO);
        Response<UserDTO> response = userController.updateUserRoleBySid(userDTO.getSid(), userDTO.getRole());
        assertNotNull(response);
        assertEquals(userDTO, response.getData());
        assertTrue(response.isSuccess());
        verify(userService, times(1)).updateUserRoleBySid(userDTO.getSid(), userDTO.getRole());
    }

    @Test
    void testUpdateUserRoleBySidNegative() {
        UserDTO userDTO = new UserDTO();
        userDTO.setSid("nonexistentSid");
        when(userService.updateUserRoleBySid(userDTO.getSid(), userDTO.getRole())).thenThrow(new IllegalStateException());
        Response<String> response = userController.updateUserRoleBySid(userDTO.getSid(), userDTO.getRole());
        assertNotNull(response);
        assertEquals("sid: nonexistentSid doesn't exist", response.getMessage());
        assertFalse(response.isSuccess());
        verify(userService, times(1)).updateUserRoleBySid(userDTO.getSid(), userDTO.getRole());
    }

    @Test
    void testUpdateUserInfoPositive() {
        String sid = "sid";
        String name = "name";
        String signature = "signature";
        Integer gender = 1;
        CustomResponse expectedResponse = new CustomResponse();
        try {
            when(userService.updateUserInfo(sid, name, signature, gender)).thenReturn(expectedResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CustomResponse actualResponse = userController.updateUserInfo(sid, name, signature, gender);
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        try {
            verify(userService, times(1)).updateUserInfo(sid, name, signature, gender);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateUserInfoNegative() {
        String sid = "nonexistentSid";
        String name = "name";
        String signature = "signature";
        Integer gender = 1;
        try {
            when(userService.updateUserInfo(sid, name, signature, gender)).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CustomResponse actualResponse = userController.updateUserInfo(sid, name, signature, gender);
        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCode());
        assertEquals("修改失败", actualResponse.getMessage());
        try {
            verify(userService, times(1)).updateUserInfo(sid, name, signature, gender);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateUserInfoGenderPositive() {
        String sid = "testSid";
        Integer gender = 1;
        CustomResponse expectedResponse = new CustomResponse();

        try {
            when(userService.updateUserGenderBySid(sid, gender)).thenReturn(expectedResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse actualResponse = userController.updateUserInfo(sid, gender);

        assertSame(expectedResponse, actualResponse);
        try {
            verify(userService, times(1)).updateUserGenderBySid(anyString(), anyInt());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateUserInfoGenderNegative() {
        String sid = "testSid";
        Integer gender = 1;

        try {
            when(userService.updateUserGenderBySid(sid, gender)).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse actualResponse = userController.updateUserInfo(sid, gender);

        assertEquals(500, actualResponse.getCode());
        assertEquals("修改失败", actualResponse.getMessage());
        try {
            verify(userService, times(1)).updateUserGenderBySid(anyString(), anyInt());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateUserInfoSignaturePositive() {
        String sid = "testSid";
        String signature = "Test signature";
        CustomResponse expectedResponse = new CustomResponse();
        try {
            when(userService.updateUserSignatureBySid(sid, signature)).thenReturn(expectedResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CustomResponse actualResponse = userController.updateUserInfo(sid, signature);
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        try {
            verify(userService, times(1)).updateUserSignatureBySid(sid, signature);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateUserInfoSignatureNegative() {
        String sid = "testSid";
        String signature = "Test signature";
        try {
            when(userService.updateUserSignatureBySid(sid, signature)).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CustomResponse actualResponse = userController.updateUserInfo(sid, signature);
        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCode());
        assertEquals("修改失败", actualResponse.getMessage());
        try {
            verify(userService, times(1)).updateUserSignatureBySid(sid, signature);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testLoginAdminPositive() {
        UserDTO userDTO = new UserDTO();
        userDTO.setSid("adminSid");
        userDTO.setPassword("correctPassword");
        when(userService.loginAdmin(userDTO)).thenReturn(1L);
        Response<?> response = userController.loginAdmin(userDTO);
        assertNotNull(response);
        assertEquals("login successfully", response.getData());
        assertTrue(response.isSuccess());
        verify(userService, times(1)).loginAdmin(userDTO);
    }

    @Test
    void testLoginAdminNegative() {
        UserDTO userDTO = new UserDTO();
        userDTO.setSid("adminSid");
        userDTO.setPassword("wrongPassword");
        when(userService.loginAdmin(userDTO)).thenReturn(-1L);
        Response<?> response = userController.loginAdmin(userDTO);
        assertNotNull(response);
        assertEquals("wrong sid or password", response.getMessage());
        assertFalse(response.isSuccess());
        verify(userService, times(1)).loginAdmin(userDTO);
    }

    @Test
    void testLoginUserPositive() {
        UserDTO userDTO = new UserDTO();
        userDTO.setSid("userSid");
        userDTO.setPassword("correctPassword");
        when(userService.loginUser(userDTO)).thenReturn(1L);
        Response<?> response = userController.loginUser(userDTO);
        assertNotNull(response);
        assertEquals("login successfully", response.getData());
        assertTrue(response.isSuccess());
        verify(userService, times(1)).loginUser(userDTO);
    }

    @Test
    void testLoginUserNegative() {
        UserDTO userDTO = new UserDTO();
        userDTO.setSid("userSid");
        userDTO.setPassword("wrongPassword");
        when(userService.loginUser(userDTO)).thenReturn(-1L);
        Response<?> response = userController.loginUser(userDTO);
        assertNotNull(response);
        assertEquals("wrong sid or password", response.getMessage());
        assertFalse(response.isSuccess());
        verify(userService, times(1)).loginUser(userDTO);
    }

    @Test
    void testUpdateUserAvatarPositive() {
        String sid = "testSid";
        MultipartFile file = mock(MultipartFile.class);
        CustomResponse expectedResponse = new CustomResponse(200, "success", null);
        try {
            when(userService.updateUserAvatarBySid(sid, file)).thenReturn(expectedResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse actualResponse = userController.updateUserAvatar(sid, file);
        assertSame(expectedResponse, actualResponse);
        try {
            verify(userService, times(1)).updateUserAvatarBySid(sid, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateUserAvatarNegative() {
        String sid = "testSid";
        MultipartFile file = mock(MultipartFile.class);

        try {
            when(userService.updateUserAvatarBySid(sid, file)).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CustomResponse actualResponse = userController.updateUserAvatar(sid, file);
        assertNotNull(actualResponse);
        assertEquals(500, actualResponse.getCode());
        assertEquals("头像更新失败", actualResponse.getMessage());
        assertNull(actualResponse.getData());
        try {
            verify(userService, times(1)).updateUserAvatarBySid(sid, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetUserAvatarPositive() {
        String avatarName = "testAvatar.png";

        HttpServletResponse response = mock(HttpServletResponse.class);
        byte[] expectedBytes;
        byte[] actualBytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ServletOutputStream servletOutputStream = new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override
                public void write(int b) {
                    outputStream.write(b);
                }
            };
            when(response.getOutputStream()).thenReturn(servletOutputStream);
            userController.getUserAvatar(avatarName, response);
            verify(response, times(1)).getOutputStream();

            actualBytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String filePath = ECS_BUCKET_PATH + "avatar/" + avatarName;
        File file = new File(filePath);

        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            expectedBytes = bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertArrayEquals(expectedBytes, actualBytes);
        try {
            verify(response, times(1)).getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetUserAvatarNegative() {
        String imageName = "testAvatar.png";
        HttpServletResponse response = mock(HttpServletResponse.class);
        try {
            when(response.getOutputStream()).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userController.getUserAvatar(imageName, response);
        try {
            verify(response, times(1)).getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
