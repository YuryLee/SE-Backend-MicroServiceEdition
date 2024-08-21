package com.bilimili.video.controller;


import com.bilimili.video.dto.VideoUploadInfoDTO;
import com.bilimili.video.handler.NonStaticResourceHttpRequestHandler;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.VideoUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Description:
 *
 * @author Yury
 */
@CrossOrigin
@RestController
@Tag(name = "视频上传api")
public class VideoUploadController {

    @Value("${ecs.bucket.path}")
    private String ECSBucketPath;

    @Autowired
    private VideoUploadService videoUploadService;

    //引入返回视频流的组件
    @Autowired
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    /**
     * 上传视频
     * @param video 文件
     * @return
     * @throws IOException
     */
    @PostMapping("/video/upload")
    @Operation(summary = "上传视频，直接返回播放链接")
    public CustomResponse uploadVideo(@RequestParam("video") MultipartFile video) throws IOException {
        try {
            return videoUploadService.uploadVideo(video);
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "上传失败", null);
        }

    }


    //查询视频流的接口
    @GetMapping("/video/play/{name}")
    @Operation(summary = "根据视频名播放视频")
    public void videoPreview(HttpServletRequest request, HttpServletResponse response, @PathVariable("name") String name) throws Exception {

        Path filePath = Path.of(ECSBucketPath + "video/" + name);
        //Files.exists：用来测试路径文件是否存在
        if (Files.exists(filePath)) {
            //获取视频的类型，比如是MP4这样
            String mimeType = Files.probeContentType(filePath);
            if (StringUtils.hasText(mimeType)) {
                //判断类型，根据不同的类型文件来处理对应的数据
                response.setContentType(mimeType);
            }
            //转换视频流部分
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }


    @GetMapping("/video/download/{name}")
    @Operation(summary = "下载视频，没啥用")
    public void download(@PathVariable String name, HttpServletResponse response) {

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(ECSBucketPath + "video/" + name));

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

    /**
     * 添加视频投稿
     * @param cover 封面文件
     * @param title 投稿标题
     * @param type  视频类型 1自制 2转载
     * @param auth  作者声明 0不声明 1未经允许禁止转载
     * @param duration 视频总时长
     * @param mcid  主分区ID
     * @param scid  子分区ID
     * @param tags  标签
     * @param descr 简介
     * @return  响应对象
     */
    @PostMapping("/video/add")
    @Operation(summary = "添加视频投稿")
    public CustomResponse addVideo(@RequestParam("cover") MultipartFile cover,
                                   @RequestParam("uid") Integer uid,
                                   @RequestParam("sid") String sid,
                                   @RequestParam("title") String title,
                                   @RequestParam("type") Integer type,
                                   @RequestParam("auth") Integer auth,
                                   @RequestParam("duration") Double duration,
                                   @RequestParam("mcid") String mcid,
                                   @RequestParam("scid") String scid,
                                   @RequestParam("tags") String tags,
                                   @RequestParam("descr") String descr,
                                   @RequestParam("videoUrl") String videoUrl) {
        VideoUploadInfoDTO videoUploadInfoDTO = new VideoUploadInfoDTO(uid, sid, title, type, auth, duration, mcid, scid, tags, descr, null, null, videoUrl);
        try {
            return videoUploadService.addVideo(cover, videoUploadInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "封面上传失败", null);
        }
    }

    /**
     * 修改视频投稿
     * @param cover 封面文件
     * @param title 投稿标题
     * @param type  视频类型 1自制 2转载
     * @param auth  作者声明 0不声明 1未经允许禁止转载
     * @param duration 视频总时长
     * @param mcid  主分区ID
     * @param scid  子分区ID
     * @param tags  标签
     * @param descr 简介
     * @return  响应对象
     */
    @PostMapping("/video/vid/{vid}/update")
    @Operation(summary = "修改视频投稿")
    public CustomResponse updateVideo(@PathVariable("vid") Integer vid,
                                      @RequestParam("cover") MultipartFile cover,
                                      @RequestParam("uid") Integer uid,
                                      @RequestParam("sid") String sid,
                                   @RequestParam("title") String title,
                                   @RequestParam("type") Integer type,
                                   @RequestParam("auth") Integer auth,
                                   @RequestParam("duration") Double duration,
                                   @RequestParam("mcid") String mcid,
                                   @RequestParam("scid") String scid,
                                   @RequestParam("tags") String tags,
                                   @RequestParam("descr") String descr,
                                   @RequestParam("videoUrl") String videoUrl) {
        VideoUploadInfoDTO videoUploadInfoDTO = new VideoUploadInfoDTO(uid, sid, title, type, auth, duration, mcid, scid, tags, descr, null, null, videoUrl);
        try {
            return videoUploadService.updateVideo(vid, cover, videoUploadInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "封面上传失败", null);
        }
    }

    @GetMapping("/video/cover/{name}")
    @Operation(summary = "获得视频封面")
    public void getVideoCover(@PathVariable String name, HttpServletResponse response) {

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(ECSBucketPath + "cover/" + name));

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

    /**
     * 取消上传
     * @param videoUrl
     * @return
     */
    @GetMapping("/video/cancel")
    @Operation(summary = "取消上传，如果已上传视频文件，则删除服务器中的该文件")
    public CustomResponse cancelUpload(@RequestParam("videoUrl") String videoUrl) {
        return videoUploadService.cancelUpload(videoUrl);
    }

}
