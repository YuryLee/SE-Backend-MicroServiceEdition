package com.bilimili.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.user.dao.User;
import com.bilimili.user.dto.UserDTO;
import com.bilimili.user.mapper.UserMapper;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.response.Response;
import com.bilimili.user.response.ResponseList;
import com.bilimili.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Converter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@CrossOrigin
@RestController
@Tag(name = "用户、管理员api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Value("${ecs.bucket.path}")
    private String ECSBucketPath;

    @GetMapping("/user/sid/{sid}")
    @Operation(summary = "由sid获取用户信息")
    public <T> Response<T> getUserBySid(@PathVariable String sid) {
        UserDTO userDTO;
        try {
            userDTO = userService.getUserBySid(sid);
        } catch (IllegalStateException e) {
            return Response.newFail("sid: " + sid + " doesn't exist");
        }
        return Response.newSuccess((T) userDTO);
    }

    @GetMapping("user")
    @Operation(summary = "获取所有用户信息")
    public <T> ResponseList<T> getAllUsers() {
        List<UserDTO> userDTOList = userService.findAll();
        int len = userDTOList.size();
        return ResponseList.newSuccess((T) userDTOList, (long) len);
    }

    @GetMapping("/user/list/sid/{sid}")
    @Operation(summary = "由sid获取用户信息，列表形式返回")
    public <T> ResponseList<T> getUserListBySid(@PathVariable String sid) {
        UserDTO userDTO;
        List<UserDTO> userDTOList;
        int len;
        try {
            userDTO = userService.getUserBySid(sid);
            userDTOList = new ArrayList<>();
            userDTOList.add(userDTO);
            len = userDTOList.size();
        } catch (IllegalStateException e) {
            return ResponseList.newFail("sid: " + sid + " doesn't exist");
        }
        return ResponseList.newSuccess((T) userDTOList, (long) len);
    }

    @GetMapping("user/list/name/{name}")
    @Operation(summary = "由name获取用户信息，列表形式返回")
    public <T> ResponseList<T> getUserListByName(@PathVariable String name) {
        List<UserDTO> userDTOList;
        int len;
        try {
            userDTOList = userService.getUserByName(name);
            len = userDTOList.size();
        } catch (IllegalStateException e) {
            return ResponseList.newFail("name: " + name + " doesn't exist");
        }
        return ResponseList.newSuccess((T) userDTOList, (long) len);
    }

    @PostMapping("/user")
    @Operation(summary = "增加用户")
    public <T> Response<T> addNewUser(@RequestBody UserDTO userDTO) {
        // 校验
        Long res = userService.addNewUser(userDTO);
        if (res == -1) {
            return Response.newFail("sid: " + userDTO.getSid() + " has been taken");
        }

        return (Response<T>) Response.newSuccess(res);
    }


    /**
     * 注册接口
     * @param userDTO 包含 sid username password 的 map
     * @return CustomResponse对象
     */
    // 前端使用axios传递的data是Content-Type: application/json，需要用@RequestBody获取参数
    @PostMapping("/user/register")
    @Operation(summary = "用户注册，只用传sid,name,password")
    public CustomResponse register(@RequestBody UserDTO userDTO) {
        String sid = userDTO.getSid();
        String username = userDTO.getName();
        String password = userDTO.getPassword();
        try {
            return userService.register(sid, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            CustomResponse customResponse = new CustomResponse();
            customResponse.setCode(500);
            customResponse.setMessage("注册失败");
            return customResponse;
        }
    }

    @DeleteMapping("/user/sid/{sid}")
    @Operation(summary = "根据sid删除指定用户")
    public <T> Response<T> deleteUserBySid(@PathVariable String sid) {
        Long res = userService.deleteUserBySid(sid);
        if (res == -1) {
            return Response.newFail("sid: " + sid + " doesn't exist");
        }
        return (Response<T>) Response.newSuccess("delete successfully");
    }

//    @PutMapping("/user/sid/{sid}")
//    @Operation(summary = "根据sid更改用户信息")
//    public <T> Response<T> updateUSerBySid(@PathVariable String sid,
//                                           @RequestParam(required = false) String name,
//                                           @RequestParam(required = false) String password) {
//        UserDTO userDTO;
//        try {
//            userDTO = userService.updateUserBySid(sid, name, password);
//        } catch (IllegalStateException e) {
//            return Response.newFail("sid: " + sid + " doesn't exist");
//        }
//        return (Response<T>) Response.newSuccess(userDTO);
//    }

    @PutMapping("/user/sid/{sid}/name")
    @Operation(summary = "根据sid更改用户name")
    public <T> Response<T> updateUserNameBySid(@PathVariable String sid,
                                               @RequestParam(required = false) String name) {
        UserDTO userDTO;
        try {
            userDTO = userService.updateUserNameBySid(sid, name);
        } catch (IllegalStateException e) {
            return Response.newFail("sid: " + sid + " doesn't exist");
        }
        return (Response<T>) Response.newSuccess(userDTO);
    }

    @PutMapping("/user/sid/{sid}/password")
    @Operation(summary = "根据sid更改用户password")
    public <T> Response<T> updateUserPasswordBySid(@PathVariable String sid,
                                                   @RequestParam(required = false) String password) {
        UserDTO userDTO;
        try {
            userDTO = userService.updateUserPasswordBySid(sid, password);
        } catch (IllegalStateException e) {
            return Response.newFail("sid: " + sid + " doesn't exist");
        }
        return (Response<T>) Response.newSuccess(userDTO);
    }

    @PutMapping("/user/sid/{sid}/role")
    @Operation(summary = "根据sid更改用户role")
    public <T> Response<T> updateUserRoleBySid(@PathVariable String sid,
                                               @RequestParam(required = false) int role) {
        UserDTO userDTO;
        try {
            userDTO = userService.updateUserRoleBySid(sid, role);
        } catch (IllegalStateException e) {
            return Response.newFail("sid: " + sid + " doesn't exist");
        }
        return (Response<T>) Response.newSuccess(userDTO);
    }

    /**
     * 更新用户部分个人信息
     * @param name  昵称
     * @param signature  个性签名
     * @param gender    性别：0 女 1 男 2 保密
     * @return
     */
    @PostMapping("/user/sid/{sid}/update")
    @Operation(summary = "根据uid更改用户name, signature, gender")
    public CustomResponse updateUserInfo(@PathVariable String sid,
                                         @RequestParam("name") String name,
                                         @RequestParam("signature") String signature,
                                         @RequestParam("gender") Integer gender) {
        try {
            return userService.updateUserInfo(sid, name, signature, gender);
        } catch (Exception e) {
            e.printStackTrace();
            CustomResponse customResponse = new CustomResponse();
            customResponse.setCode(500);
            customResponse.setMessage("修改失败");
            return customResponse;
        }
    }

    @PostMapping("/user/sid/{sid}/gender")
    @Operation(summary = "根据uid更改用户gender")
    public CustomResponse updateUserInfo(@PathVariable String sid,
                                         @RequestParam("gender") Integer gender) {
        try {
            return userService.updateUserGenderBySid(sid, gender);
        } catch (Exception e) {
            e.printStackTrace();
            CustomResponse customResponse = new CustomResponse();
            customResponse.setCode(500);
            customResponse.setMessage("修改失败");
            return customResponse;
        }
    }


    @PostMapping("/user/sid/{sid}/signature")
    @Operation(summary = "根据uid更改用户signature")
    public CustomResponse updateUserInfo(@PathVariable String sid,
                                         @RequestParam("signature") String signature) {
        try {
            return userService.updateUserSignatureBySid(sid, signature);
        } catch (Exception e) {
            e.printStackTrace();
            CustomResponse customResponse = new CustomResponse();
            customResponse.setCode(500);
            customResponse.setMessage("修改失败");
            return customResponse;
        }
    }

    @PostMapping("/admin/login")
    @Operation(summary = "管理员登录")
    public <T> Response<T> loginAdmin(@RequestBody UserDTO userDTO) {
        Long res = userService.loginAdmin(userDTO);
        if (res == -1) {
            return Response.newFail("wrong sid or password");
        }

        return (Response<T>) Response.newSuccess("login successfully");
    }


    @PostMapping("/user/login")
    @Operation(summary = "用户登录")
    public <T> Response<T> loginUser(@RequestBody UserDTO userDTO) {
        Long res = userService.loginUser(userDTO);
        if (res == -1) {
            return Response.newFail("wrong sid or password");
        }

        return (Response<T>) Response.newSuccess("login successfully");
    }

    /**
     * 更新用户头像
     * @param file  头像文件
     * @return  成功则返回新头像url
     */
    @PostMapping("/user/sid/{sid}/avatar")
    @Operation(summary = "更新用户头像")
    public CustomResponse updateUserAvatar(@PathVariable String sid, @RequestParam("file") MultipartFile file) {
        try {
            return userService.updateUserAvatarBySid(sid, file);
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "头像更新失败", null);
        }
    }

    @GetMapping("/user/avatar/{name}")
    @Operation(summary = "获得用户头像")
    public void getUserAvatar(@PathVariable String name, HttpServletResponse response) {

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(ECSBucketPath + "avatar/" + name));

            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            //response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Autowired
//    private CurrentUser currentUser;
//
//    /**
//     * 更新用户部分个人信息
//     * @param nickname  昵称
//     * @param desc  个性签名
//     * @param gender    性别：0 女 1 男 2 保密
//     * @return
//     */
//    @PostMapping("/user/info/update")
//    public CustomResponse updateUserInfo(@RequestParam("name") String name,
//                                         @RequestParam("description") String desc,
//                                         @RequestParam("gender") Integer gender) {
//        Integer uid = currentUser.getUserId();
//        try {
//            return userService.updateUserInfo(uid, nickname, desc, gender);
//        } catch (Exception e) {
//            e.printStackTrace();
//            CustomResponse customResponse = new CustomResponse();
//            customResponse.setCode(500);
//            customResponse.setMessage("特丽丽被玩坏了");
//            return customResponse;
//        }
//    }
//
//    /**
//     * 更新用户头像
//     * @param file  头像文件
//     * @return  成功则返回新头像url
//     */
//    @PostMapping("/user/avatar/update")
//    public CustomResponse updateUserAvatar(@RequestParam("file") MultipartFile file) {
//        Integer uid = currentUser.getUserId();
//        try {
//            return userService.updateUserAvatar(uid, file);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new CustomResponse(500, "头像更新失败", null);
//        }
//    }
//
//    @GetMapping("/user/info/get-one")
//    public CustomResponse getOneUserInfo(@RequestParam("uid") Integer uid) {
//        CustomResponse customResponse = new CustomResponse();
//        customResponse.setData(userService.getUserById(uid));
//        return customResponse;
//    }

    // 微服务接口
    @GetMapping("/user-service/userDTO/sid/{sid}")
    @Operation(summary = "服务接口：由sid获取用户信息")
    public UserDTO getUserDTOBySid(@PathVariable String sid) {
        UserDTO userDTO;
        userDTO = userService.getUserBySid(sid);
        return userDTO;
    }

    @PostMapping("/user-service/updateStats")
    @Operation(summary = "服务接口：更改用户的某个视频统计数据")
    public void updateStats(@RequestParam("sid") String sid, @RequestParam("column") String column,
                            @RequestParam("increase") boolean increase, @RequestParam("count") Integer count) {
        userService.updateStats(sid, column, increase, count);
    }

    @GetMapping("/user-service/search/{text}")
    @Operation(summary = "服务接口：搜索text相关用户")
    public List<User> searchUser(@PathVariable String text) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        text = '%' + text + '%';
        queryWrapper.orderByDesc("fans_count").like("name", text).or().like("signature", text).or().like("sid", text);
        return userMapper.selectList(queryWrapper);
    }

}

