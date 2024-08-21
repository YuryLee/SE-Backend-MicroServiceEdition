package com.bilimili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bilimili.video.dao.Video;
import com.bilimili.video.dto.VideoUploadInfoDTO;
import com.bilimili.video.mapper.VideoMapper;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.VideoUploadService;
import com.bilimili.video.utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Description:
 *
 * @author Yury
 */
@Slf4j
@Service
public class VideoUploadServiceImpl implements VideoUploadService {
    @Value("${ecs.bucket.path}")
    private String ECSBucketPath;

    @Value("${ecs.url}")
    private String ECSUrl;

    @Autowired
    UploadUtils uploadUtils;

    @Autowired
    private VideoMapper videoMapper;

    /**
     * 上传单个视频
     * @param video 分片文件
     * @return  CustomResponse对象
     * @throws IOException
     */
    @Override
    public CustomResponse uploadVideo(MultipartFile video) throws IOException {

        String originalFilename = video.getOriginalFilename();   // 获取原文件名
        String ext = "." + FilenameUtils.getExtension(originalFilename);    // 获取文件后缀
        String uuid = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-", "");

        String fileName = date + uuid + ext;

        // 完整路径名
        String filePath = ECSBucketPath + "video" + "/";
        String filePathName = ECSBucketPath + "video" + "/" + fileName;
        System.out.println(filePathName);

        File file1 = new File(filePath, fileName);
        //如果目标文件夹不存在就创建
        if (!file1.exists()){
            file1.mkdirs();
            file1.createNewFile();
        }
        video.transferTo(file1);

        String video_url = ECSUrl + "/video/" + "play" + "/" + fileName;

        return new CustomResponse(200, "OK", video_url);
    }

    /**
     * 接收前端提供的视频信息，包括封面文件和稿件的其他信息，保存完封面后将信息发送到消息队列，并返回投稿成功响应
     * @param cover 封面图片文件
     * @param videoUploadInfoDTO 存放投稿信息的 VideoUploadInfo 对象
     * @return  CustomResponse对象
     * @throws IOException
     */
    @Override
    public CustomResponse addVideo(MultipartFile cover, VideoUploadInfoDTO videoUploadInfoDTO) throws IOException {
        // 值的判定 虽然前端会判 防止非法请求 不过数据库也写不进去 但会影响封面保存
        if (videoUploadInfoDTO.getTitle().trim().isEmpty()) {
            return new CustomResponse(500, "标题不能为空", null);
        }
        if (videoUploadInfoDTO.getTitle().length() > 80) {
            return new CustomResponse(500, "标题不能超过80字", null);
        }
        if (videoUploadInfoDTO.getDescr().length() > 2000) {
            return new CustomResponse(500, "简介太长啦", null);
        }


        // 保存封面服务器，返回URL
        String coverUrl = uploadUtils.uploadImage(cover, "cover", "video");

        // 将投稿信息封装
        videoUploadInfoDTO.setCoverUrl(coverUrl);

//        mergeChunks(videoUploadInfo);   // 这里是串行操作，严重影响性能

        // 发送到消息队列等待监听写库
        // 序列化 videoUploadInfo 对象为 String， 往 rabbitmq 中发送投稿信息，也可以使用多线程异步
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonPayload = objectMapper.writeValueAsString(videoUploadInfo);
//        rabbitTemplate.convertAndSend("direct_upload_exchange", "videoUpload", jsonPayload);

        // 存入数据库
        Date now = new Date();
        Video video = new Video(
                null,
                videoUploadInfoDTO.getUid(),
                videoUploadInfoDTO.getSid(),
                videoUploadInfoDTO.getTitle(),
                videoUploadInfoDTO.getType(),
                videoUploadInfoDTO.getAuth(),
                videoUploadInfoDTO.getDuration(),
                videoUploadInfoDTO.getMcId(),
                videoUploadInfoDTO.getTags(),
                videoUploadInfoDTO.getDescr(),
                videoUploadInfoDTO.getCoverUrl(),
                videoUploadInfoDTO.getVideoUrl(),
                0,
                0,
                null,
                now,
                null,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );
        videoMapper.insert(video);
        //userService.updateStats(videoUploadInfoDTO.getSid(), "video_count", true, 1);
//        VideoStats videoStats = new VideoStats(video.getVid(),0,0,0,0,0,0,0,0);
//        videoStatsMapper.insert(videoStats);
//        esUtil.addVideo(video);
//        CompletableFuture.runAsync(() -> redisUtil.setExObjectValue("video:" + video.getVid(), video), taskExecutor);
//        CompletableFuture.runAsync(() -> redisUtil.addMember("video_status:0", video.getVid()), taskExecutor);
//        CompletableFuture.runAsync(() -> redisUtil.setExObjectValue("videoStats:" + video.getVid(), videoStats), taskExecutor);

        // 其他逻辑 （发送消息通知写库成功）
        return new CustomResponse();
    }

    /**
     * 取消上传并且删除该视频的分片文件
     * @param videoUrl 视频的hash值
     * @return CustomResponse对象
     */
    @Override
    public CustomResponse cancelUpload(String videoUrl) {
        String path = ECSBucketPath + "video/";
        // 删除本地分片文件
        // 获取分片文件的存储目录
        File videoDir = new File(path);
        // 获取存储在目录中的所有分片文件
        int index = videoUrl.indexOf(path);
        String videoName = videoUrl.substring(index + path.length());
        File video = new File(path, videoName);
        // 删除全部分片文件
        if (video.exists()) {
            video.delete();
        }
//        System.out.println("删除分片完成");


        // 不管删没删成功 返回成功响应
        return new CustomResponse();
    }

    @Override
    public CustomResponse updateVideo(Integer vid, MultipartFile cover, VideoUploadInfoDTO videoUploadInfoDTO) throws IOException {
        // 值的判定 虽然前端会判 防止非法请求 不过数据库也写不进去 但会影响封面保存
        if (videoUploadInfoDTO.getTitle().trim().isEmpty()) {
            return new CustomResponse(500, "标题不能为空", null);
        }
        if (videoUploadInfoDTO.getTitle().length() > 80) {
            return new CustomResponse(500, "标题不能超过80字", null);
        }
        if (videoUploadInfoDTO.getDescr().length() > 2000) {
            return new CustomResponse(500, "简介太长啦", null);
        }


        // 保存封面服务器，返回URL
        String coverUrl = uploadUtils.uploadImage(cover, "cover", "video");

        // 将投稿信息封装
        videoUploadInfoDTO.setCoverUrl(coverUrl);

        UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("vid", vid)
                .set("title", videoUploadInfoDTO.getTitle())
                .set("type", videoUploadInfoDTO.getType())
                .set("auth", videoUploadInfoDTO.getAuth())
                .set("duration", videoUploadInfoDTO.getDuration())
                .set("mc_id", videoUploadInfoDTO.getMcId())
                .set("sc_id", videoUploadInfoDTO.getScId())
                .set("tags", videoUploadInfoDTO.getTags())
                .set("descr", videoUploadInfoDTO.getDescr())
                .set("cover_url", videoUploadInfoDTO.getCoverUrl())
                .set("video_url", videoUploadInfoDTO.getVideoUrl())
                .set("status", 0)
                .set("complain", 0)
                .set("advice", null)
                .set("upload_date", new Date())
                .set("delete_date", null)
                .set("play", 0)
                .set("danmu", 0)
                .set("good", 0)
                .set("bad", 0)
                .set("coin", 0)
                .set("collect", 0)
                .set("share", 0)
                .set("comment", 0);

        return new CustomResponse();
    }
}
