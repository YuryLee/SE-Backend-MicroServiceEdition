package com.bilimili.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilimili.user.converter.UserConverter;
import com.bilimili.user.dao.User;
import com.bilimili.user.dao.UserRepository;
import com.bilimili.user.dto.UserDTO;
import com.bilimili.user.mapper.UserMapper;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.service.UserService;
import com.bilimili.user.utils.UploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * Description:
 *
 * @author Yury
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    UploadUtils uploadUtils;

//    @Autowired
//    private DailyPlayService dailyPlayService;
//
//    @Autowired
//    private DailyWatchService dailyWatchService;

    @Override
    public UserDTO getUserBySid(String sid) {
        List<User> userList = userRepository.findBySid(sid);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
            // throw new IllegalStateException("sid:" + sid + " doesn't exist");
        }
        return UserConverter.convertUser(userList.get(0));
    }

    @Override
    public Long addNewUser(UserDTO userDTO) {
//        List<User> userList = userRepository.findByName(userDTO.getName());
//        if (!CollectionUtils.isEmpty(userList)) {
//            throw new IllegalStateException("name:" + userDTO.getName() + "has been taken");
//        }
        List<User> userList = userRepository.findBySid(userDTO.getSid());
        if (!CollectionUtils.isEmpty(userList)) {
            return (long) -1;
            //throw new IllegalStateException("sid:" + userDTO.getSid() + "has been taken");
        }
        User user = UserConverter.convertUser(userDTO);
        user.setRole(0);
        user.setGender(2);
        userRepository.save(UserConverter.convertUser(userDTO));
        return user.getUid();
    }

    @Override
    public Long deleteUserBySid(String sid) {
        List<User> userList = userRepository.findBySid(sid);
        if (CollectionUtils.isEmpty(userList)) {
            return (long) -1;
        }
        userRepository.deleteBySid(sid);
        return (long) 0;
    }

    @Override
    @Transactional
    public UserDTO updateUserBySid(String sid, String name, String password) {
        List<User> userList = userRepository.findBySid(sid);
        if (CollectionUtils.isEmpty(userList)) {
            throw new IllegalStateException("sid:" + sid + " doesn't exist");
        }
        User userInDB = userList.get(0);
        if (StringUtils.hasLength(name) && !userInDB.getName().equals(name)) {
            userInDB.setName(name);
        }
        if (StringUtils.hasLength(password) && !userInDB.getPassword().equals(password)) {
            userInDB.setPassword(password);
        }
        User user = userRepository.save(userInDB);
        return UserConverter.convertUser(user);
    }

    @Override
    public Long loginAdmin(UserDTO userDTO) {
        List<User> userList = userRepository.findBySid(userDTO.getSid());
        if (CollectionUtils.isEmpty(userList)) {
            return (long) -1;
            //throw new IllegalStateException("sid:" + userDTO.getSid() + "has been taken");
        }
        User user = userList.get(0);
        // System.out.println(userDTO.getPassword() + " " + user.getPassword());
        if (user.getRole() != 1) {
            return (long) -1;
        }
        if (user.getPassword().equals(userDTO.getPassword())) {
            return (long) 1;
        }
        return (long) -1;
    }

    @Override
    public Long loginUser(UserDTO userDTO) {
        Long result;
        List<User> userList = userRepository.findBySid(userDTO.getSid());
        if (CollectionUtils.isEmpty(userList)) {
            result = (long) -1;
            //throw new IllegalStateException("sid:" + userDTO.getSid() + "has been taken");
        } else {
            User user = userList.get(0);// System.out.println(userDTO.getPassword() + " " + user.getPassword());
            if (user.getPassword().equals(userDTO.getPassword())) {
                result = (long) 1;
            } else {
                result = (long) -1;
            }
        }
        return result;
    }

    @Override
    public List<UserDTO> getUserByName(String name) {
        List<User> userList = userRepository.findByName(name);
        if (CollectionUtils.isEmpty(userList)) {
            throw new IllegalStateException("name:" + name + " doesn't exist");
        }
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            userDTOList.add(UserConverter.convertUser(user));
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            userDTOList.add(UserConverter.convertUser(user));
        }
        return userDTOList;
    }

    @Override
    public UserDTO updateUserNameBySid(String sid, String name) {
        List<User> userList = userRepository.findBySid(sid);
        if (CollectionUtils.isEmpty(userList)) {
            throw new IllegalStateException("sid:" + sid + " doesn't exist");
        }
        User userInDB = userList.get(0);
        if (StringUtils.hasLength(name) && !userInDB.getName().equals(name)) {
            userInDB.setName(name);
        }
        User user = userRepository.save(userInDB);
        return UserConverter.convertUser(user);
    }

    @Override
    public UserDTO updateUserPasswordBySid(String sid, String password) {
        List<User> userList = userRepository.findBySid(sid);
        if (CollectionUtils.isEmpty(userList)) {
            throw new IllegalStateException("sid:" + sid + " doesn't exist");
        }
        User userInDB = userList.get(0);
        if (StringUtils.hasLength(password) && !userInDB.getPassword().equals(password)) {
            userInDB.setPassword(password);
        }
        User user = userRepository.save(userInDB);
        return UserConverter.convertUser(user);
    }

    @Override
    public UserDTO updateUserRoleBySid(String sid, int role) {
        List<User> userList = userRepository.findBySid(sid);
        if (CollectionUtils.isEmpty(userList)) {
            throw new IllegalStateException("sid:" + sid + " doesn't exist");
        }
        User userInDB = userList.get(0);
        if (!(userInDB.getRole() == role)) {
            userInDB.setRole(role);
        }
        User user = userRepository.save(userInDB);
        return UserConverter.convertUser(user);
    }

//    @Autowired
//    private MsgUnreadMapper msgUnreadMapper;
//
//    @Autowired
//    private FavoriteMapper favoriteMapper;

    /**
     * 用户注册
     * @param username 账号
     * @param password 密码
     * @return CustomResponse对象
     */
    @Override
    @Transactional
    public CustomResponse register(String sid, String username, String password) throws IOException {
        CustomResponse customResponse = new CustomResponse();
        if (username == null) {
            customResponse.setCode(403);
            customResponse.setMessage("账号不能为空");
            return customResponse;
        }
        if (password == null) {
            customResponse.setCode(403);
            customResponse.setMessage("密码不能为空");
            return customResponse;
        }
        username = username.trim();   //删掉用户名的空白符
        if (username.length() == 0) {
            customResponse.setCode(403);
            customResponse.setMessage("账号不能为空");
            return customResponse;
        }
        if (username.length() > 50) {
            customResponse.setCode(403);
            customResponse.setMessage("账号长度不能大于50");
            return customResponse;
        }
        if (password.length() == 0) {
            customResponse.setCode(403);
            customResponse.setMessage("密码不能为空");
            return customResponse;
        }
        if (password.length() > 50) {
            customResponse.setCode(403);
            customResponse.setMessage("密码长度不能大于50");
            return customResponse;
        }
//        if (!password.equals(confirmedPassword)) {
//            customResponse.setCode(403);
//            customResponse.setMessage("两次输入的密码不一致");
//            return customResponse;
//        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid);
        queryWrapper.ne("state", 2);
        User user = userMapper.selectOne(queryWrapper);   //查询数据库里值等于username并且没有注销的数据
        if (user != null) {
            customResponse.setCode(403);
            customResponse.setMessage("账号已存在");
            return customResponse;
        }


        String avatar_url = "cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png";
        String bg_url = "tinypic.host/images/2023/11/15/69PB2Q5W9D2U7L.png";
        Date now = new Date();
        User new_user = new User(
                null,
                sid,
                username,
                password,
                avatar_url,
                bg_url,
                2,
                "这个人很懒，什么都没留下~",
                0,
                (double) 0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                now,
                null
        );
        userMapper.insert(new_user);
        //msgUnreadMapper.insert(new MsgUnread(new_user.getUid(),0,0,0,0,0,0));
        //favoriteMapper.insert(new Favorite(null, new_user.getSid(), 1, 1, null, "默认收藏夹", "", 0, null));
        //esUtil.addUser(new_user);
        customResponse.setMessage("注册成功！欢迎加入Bilimili");

//        dailyPlayService.initData(sid);
//        dailyWatchService.initData(sid);

        return customResponse;
    }


    @Override
    public CustomResponse updateUserAvatarBySid(String sid, MultipartFile file) throws IOException {
        // 保存封面 返回URL
        String avatarUrl = uploadUtils.uploadImage(file, "avatar", "user");
        // 先更新数据库
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sid", sid).set("avatar", avatarUrl);
        userMapper.update(null, updateWrapper);

//        if (user.getAvatar().startsWith(OSS_BUCKET_URL)) {
//            String filename = user.getAvatar().substring(OSS_BUCKET_URL.length());
////              System.out.println("要删除的源文件：" + filename);
//            ossUtil.deleteFiles(filename);
//        }
        return new CustomResponse(200, "OK", avatarUrl);
    }

    @Override
    @Transactional
    public CustomResponse updateUserInfo(String sid, String name, String signature, Integer gender) throws IOException {
        CustomResponse customResponse = new CustomResponse();
        if (name == null || name.trim().isEmpty()) {
            customResponse.setCode(500);
            customResponse.setMessage("昵称不能为空");
            return customResponse;
        }
        if (name.length() > 24 || signature.length() > 100) {
            customResponse.setCode(500);
            customResponse.setMessage("输入字符过长");
            return customResponse;
        }
        if (Objects.equals(name, "账号已注销")) {
            customResponse.setCode(500);
            customResponse.setMessage("昵称非法");
            return customResponse;
        }
        // 查重
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("name", name).ne("uid", uid);
//        User user = userMapper.selectOne(queryWrapper);
//        if (user != null) {
//            customResponse.setCode(500);
//            customResponse.setMessage("该昵称已被其他用户占用");
//            return customResponse;
//        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sid", sid)
                .set("name", name)
                .set("signature", signature)
                .set("gender", gender);
        userMapper.update(null, updateWrapper);
//        User new_user = new User();
//        new_user.setUid((long) uid);
//        new_user.setname(name);
//        esUtil.updateUser(new_user);
//        redisUtil.delValue("user:" + uid);
        return customResponse;
    }

    @Override
    public CustomResponse updateUserSignatureBySid(String sid, String signature) throws IOException {
        CustomResponse customResponse = new CustomResponse();

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sid", sid)
                .set("signature", signature);
        userMapper.update(null, updateWrapper);
//        User new_user = new User();
//        new_user.setUid((long) uid);
//        new_user.setname(name);
//        esUtil.updateUser(new_user);
//        redisUtil.delValue("user:" + uid);
        return customResponse;
    }

    @Override
    public CustomResponse updateUserGenderBySid(String sid, Integer gender) throws IOException {
        CustomResponse customResponse = new CustomResponse();

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sid", sid)
                .set("gender", gender);
        userMapper.update(null, updateWrapper);
//        User new_user = new User();
//        new_user.setUid((long) uid);
//        new_user.setname(name);
//        esUtil.updateUser(new_user);
//        redisUtil.delValue("user:" + uid);
        return customResponse;
    }

    @Override
    public void updateStats(String sid, String column, boolean increase, Integer count) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sid", sid);
        if (increase) {
            updateWrapper.setSql(column + " = " + column + " + " + count);
        } else {
            // 更新后的字段不能小于0
            updateWrapper.setSql(column + " = CASE WHEN " + column + " - " + count + " < 0 THEN 0 ELSE " + column + " - " + count + " END");
        }
        userMapper.update(null, updateWrapper);
    }



}
