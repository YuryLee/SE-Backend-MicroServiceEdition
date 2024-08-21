package com.bilimili.user.controller;

import com.bilimili.user.dao.DailyPlay;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.service.DailyPlayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@CrossOrigin
@RestController
@Tag(name = "创作周报每日记录api")
public class DailyPlayController {

    @Autowired
    private DailyPlayService dailyPlayService;

    @GetMapping("/daily/sid/{sid}")
    @Operation(summary = "获取sid一周的数据")
    public CustomResponse getDailyData(@PathVariable String sid) {
        CustomResponse customResponse = new CustomResponse();
        List<DailyPlay> dailyPlayList = dailyPlayService.getDailyData(sid);
        customResponse.setData(dailyPlayList);
        return customResponse;
    }
}
