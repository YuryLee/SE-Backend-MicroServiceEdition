package com.bilimili.specol.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.specol.dao.Zhuanlan;
import com.bilimili.specol.mapper.ZhuanlanMapper;
import com.bilimili.specol.response.CustomResponse;
import com.bilimili.specol.service.ZhuanlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author Yury
 */
@CrossOrigin
@RestController
@Tag(name = "专栏api")
public class ZhuanlanController {

    @Autowired
    private ZhuanlanService zhuanlanService;

    @Autowired
    private ZhuanlanMapper zhuanlanMapper;

    /**
     * 创建一个新的专栏
     * @param title 标题  限80字（需前端做合法判断）
     * @param desc  简介  限200字（需前端做合法判断）
     * @param visible   是否公开 0否 1是
     * @return  包含新创建的收藏夹信息的响应对象
     */
    @PostMapping("/zhuanlan/create")
    @Operation(summary = "创建一个新的专栏，visible是否公开 0否 1是")
    public CustomResponse createZhuanlan(@RequestParam("sid") String sid,
                                         @RequestParam("title") String title,
                                         @RequestParam("desc") String desc,
                                         @RequestParam("visible") Integer visible,
                                         @RequestParam("cover") MultipartFile cover) throws IOException {
        try {
            return zhuanlanService.addZhuanlan(sid, title, desc, visible, cover);
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "封面上传失败", null);
        }
    }

    @PostMapping("/zhuanlan/delete")
    @Operation(summary = "删除专栏")
    public CustomResponse createZhuanlan(@RequestParam("zid") Integer zid) throws IOException {
        try {
            return zhuanlanService.delZhuanlan(zid);
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "删除失败", null);
        }
    }

    @PostMapping("/zhuanlan/update-info")
    @Operation(summary = "修改专栏信息，visible是否公开 0否 1是")
    public CustomResponse updateZhuanlan(@RequestParam("zid") Integer zid,
                                         @RequestParam("sid") String sid,
                                         @RequestParam("title") String title,
                                         @RequestParam("desc") String desc,
                                         @RequestParam("visible") Integer visible) throws IOException {
        try {
            return new CustomResponse(200, "修改成功", zhuanlanService.updateZhuanlan(zid, sid, title, desc, visible));
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "修改失败", null);
        }
    }

    @PostMapping("/zhuanlan/update-visible")
    @Operation(summary = "修改visible是否公开 0否 1是")
    public CustomResponse updateZhuanlan(@RequestParam("zid") Integer zid,
                                         @RequestParam("visible") Integer visible) throws IOException {
        try {
            return new CustomResponse(200, "修改成功", zhuanlanService.updateZhuanlanVisible(zid, visible));
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "修改失败", null);
        }
    }

    @PostMapping("/zhuanlan/{zid}/add")
    @Operation(summary = "给zid专栏添加视频，传入视频列表vid的字符串形式，逗号隔开")
    public CustomResponse addVideosToZhuanlan(@PathVariable("zid") Integer zid, @RequestParam String vidListString) {
        CustomResponse customResponse = new CustomResponse();
        String[] vidStringList = vidListString.split(",");
        List<Integer> vidList = new ArrayList<>();
        for (String s: vidStringList) {
            vidList.add(Integer.parseInt(s));
        }

        customResponse = zhuanlanService.addVideosToZhuanlan(zid, vidList);
        return customResponse;
    }

    @PostMapping("/zhuanlan/{zid}/delete")
    @Operation(summary = "给zid专栏删除视频，传入视频列表vid的字符串形式，逗号隔开")
    public CustomResponse updateVideosFromZhuanlan(@PathVariable("zid") Integer zid, @RequestParam String vidListString) {
        CustomResponse customResponse = new CustomResponse();
        String[] vidStringList = vidListString.split(",");
        List<Integer> vidList = new ArrayList<>();
        for (String s: vidStringList) {
            vidList.add(Integer.parseInt(s));
        }

        customResponse = zhuanlanService.updateVideosFromZhuanlan(zid, vidList);
        return customResponse;
    }

    /**
     * 站内用户请求某个用户的专栏列表（需要jwt鉴权）
     * @param sid2   被查看的用户ID
     * @return  包含收藏夹列表的响应对象
     */
    @GetMapping("/zhuanlan/get-all/user")
    @Operation(summary = "sid1获取sid2的所有专栏，sid相同则显示隐藏专栏")
    public CustomResponse getAllZhuanlansForUser(@RequestParam("sid1") String sid1, @RequestParam("sid2") String sid2) {
        CustomResponse customResponse = new CustomResponse();
        if (Objects.equals(sid1, sid2)) {
            customResponse.setData(zhuanlanService.getZhuanlans(sid2, true));
        } else {
            customResponse.setData(zhuanlanService.getZhuanlans(sid2, false));
        }
        return customResponse;
    }

    /**
     * 游客请求某个用户的收藏夹列表（不需要jwt鉴权）
     * @param sid   被查看的用户ID
     * @return  包含收藏夹列表的响应对象
     */
    @GetMapping("/zhuanlan/get-all/visitor")
    @Operation(summary = "游客获取sid的所有专栏")
    public CustomResponse getAllZhuanlansForVisitor(@RequestParam("sid") String sid) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(zhuanlanService.getZhuanlans(sid, false));
        return customResponse;
    }

    @GetMapping("/zhuanlan/all")
    @Operation(summary = "获取所有专栏")
    public CustomResponse getAllZhuanlans() {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(zhuanlanService.getAllZhuanlans());
        return customResponse;
    }


    @GetMapping("/zhuanlan/video/zid/{zid}")
    @Operation(summary = "获取专栏zid的所有视频")
    public CustomResponse getAllVideosByZid(@PathVariable Integer zid) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(zhuanlanService.getAllVideosByZid(zid));
        return customResponse;
    }

    @GetMapping("/zhuanlan/zid/{zid}")
    @Operation(summary = "获取专栏zid的所有信息包括视频")
    public CustomResponse getAllDataByZid(@PathVariable Integer zid) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(zhuanlanService.getAllDataByZid(zid));
        return customResponse;
    }

    @GetMapping("/zhuanlan/video-not-in-zid")
    @Operation(summary = "获取sid不在专栏zid的所有视频")
    public CustomResponse getVideosNotInZid(@RequestParam("sid") String sid, @RequestParam("zid") Integer zid) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(zhuanlanService.getVideosNotInZid(sid, zid));
        return customResponse;
    }

    @PostMapping("/zhuanlan/change/status")
    @Operation(summary = "修改专栏状态，要修改的状态")
    public CustomResponse updateVideoStatus(@RequestParam("zid") Integer zid,
                                            @RequestParam("status") Integer status) {
        try {
            return zhuanlanService.updateZhuanlanStatus(zid, status);
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "操作失败", null);
        }
    }

    @PostMapping("/zhuanlan/collect/")
    @Operation(summary = "收藏一些vid进sid收藏夹")
    public CustomResponse collectVideo(@RequestParam("sid") String sid, @RequestParam("vid_string") String vidString) {
        CustomResponse customResponse = new CustomResponse();
        customResponse = zhuanlanService.collectVideos(sid, vidString);

        return customResponse;
    }

    // 微服务接口
    @GetMapping("/specol-service/zid/{zid}")
    @Operation(summary = "服务接口：根据zid获取专栏信息")
    public Zhuanlan getSpecolByZid(@PathVariable Integer zid) {
        return zhuanlanMapper.selectById(zid);
    }

    @GetMapping("/specol-service/search/{text}")
    @Operation(summary = "服务接口：搜索text相关专栏")
    public List<Zhuanlan> searchSpecol(@PathVariable String text) {
        QueryWrapper<Zhuanlan> queryWrapper = new QueryWrapper<>();
        text = '%' + text + '%';
        queryWrapper.like("title", text).or().like("description", text);
        return zhuanlanMapper.selectList(queryWrapper);
    }
}
