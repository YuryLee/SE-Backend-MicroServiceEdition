package com.bilimili.user.controller;

import com.bilimili.user.dao.DailyWatch;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.service.DailyWatchService;
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
@Tag(name = "视频周报每日记录api")
public class DailyWatchController {

    @Autowired
    private DailyWatchService dailyWatchService;

    @GetMapping("/daily/sid-watch/{sid}")
    @Operation(summary = "获取sid一周的数据")
    public CustomResponse getDailyData(@PathVariable String sid) {
        CustomResponse customResponse = new CustomResponse();
        List<DailyWatch> dailyWatchList = dailyWatchService.getDailyData(sid);
        customResponse.setData(dailyWatchList);
        return customResponse;
    }
}
