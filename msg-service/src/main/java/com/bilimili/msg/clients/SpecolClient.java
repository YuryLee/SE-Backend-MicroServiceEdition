package com.bilimili.msg.clients;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.msg.dao.Zhuanlan;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Description:
 *
 * @author Yury
 * @date 2024/8/23 上午10:56
 */
@FeignClient(value = "specol-service")
public interface SpecolClient {
    @GetMapping("/specol-service/zid/{zid}")
    @Operation(summary = "服务接口：根据zid获取专栏信息")
    Zhuanlan getSpecolByZid(@PathVariable Integer zid);


    @GetMapping("/specol-service/search/{text}")
    @Operation(summary = "服务接口：搜索text相关专栏")
    List<Zhuanlan> searchSpecol(@PathVariable String text);
}
