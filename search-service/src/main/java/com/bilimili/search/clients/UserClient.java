package com.bilimili.search.clients;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.search.dao.User;
import com.bilimili.search.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/8/22 下午4:26
 */
@FeignClient(value = "user-service")
public interface UserClient {
    @GetMapping("/user-service/userDTO/sid/{sid}")
    @Operation(summary = "服务接口：由sid获取用户信息")
    UserDTO getUserDTOBySid(@PathVariable String sid);

    @PostMapping("/user-service/updateStats")
    @Operation(summary = "服务接口：更改用户的某个视频统计数据")
    void updateStats(@RequestParam("sid") String sid, @RequestParam("column") String column,
                            @RequestParam("increase") boolean increase, @RequestParam("count") Integer count);

    @GetMapping("follow-service/follows")
    @Operation(summary = "服务接口：获得sid关注列表")
    List<UserDTO> getFollowsService(@RequestParam("sid") String sid);

    @GetMapping("/follow-service/fans")
    @Operation(summary = "服务接口：获得sid粉丝列表")
    List<UserDTO> getFansService(@RequestParam("sid") String sid);

    @GetMapping("/user-service/search/{text}")
    @Operation(summary = "服务接口：搜索text相关用户")
    List<User> searchUser(@PathVariable String text);
}
