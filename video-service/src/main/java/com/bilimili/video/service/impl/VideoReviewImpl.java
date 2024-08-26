package com.bilimili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bilimili.video.clients.MsgClient;
import com.bilimili.video.clients.PostClient;
import com.bilimili.video.clients.UserClient;
import com.bilimili.video.converter.VideoConverter;
import com.bilimili.video.dao.Danmu;
import com.bilimili.video.mapper.VideoMapper;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.DanmuService;
import com.bilimili.video.service.VideoReview;
import com.bilimili.video.dao.Video;
import com.bilimili.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.PrimitiveIterator;

/**
 * Description:
 *
 * @author Yury
 */
@Slf4j
@Service
public class VideoReviewImpl implements VideoReview {
    
    @Value("${ecs.bucket.path}")
    private String ECSBucketPath;

    @Value("${ecs.url}")
    private String ECSUrl;

    @Autowired
    private UserClient userClient;

    @Autowired
    private MsgClient msgClient;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private PostClient postClient;

    @Autowired
    private VideoConverter videoConverter;

    @Autowired
    private DanmuService danmuService;

    /**
     * 更新视频状态，包括过审、不通过、删除，其中审核相关需要管理员权限，删除可以是管理员或者投稿用户
     *
     * @param vid    视频ID
     * @param status 要修改的状态，1通过 2不通过 3删除
     * @param advice 建议
     * @return 无data返回，仅返回响应信息
     */
    @Override
    @Transactional
    public CustomResponse updateVideoStatus(Integer vid, Integer status, String advice) throws IOException {
        CustomResponse customResponse = new CustomResponse();
        if (status == 1 || status == 2) {
            if (status == 1) {
                QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("vid", vid).ne("status", 3);
                Video video = videoMapper.selectOne(queryWrapper);
                if (video == null) {
                    customResponse.setCode(404);
                    customResponse.setMessage("视频不见了");
                    return customResponse;
                }

                int lastStatus = video.getStatus();

                video.setStatus(1);
                UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("vid", vid).set("status", 1).set("upload_date", new Date()).set("advice", advice);     // 更新视频状态审核通过
                int flag = videoMapper.update(null, updateWrapper);
                if (flag <= 0) {
                    // 更新失败，处理错误情况
                    customResponse.setCode(500);
                    customResponse.setMessage("更新状态失败");
                    return customResponse;
                }

                userClient.updateStats(video.getSid(), "video_count", true, 1);

                if (lastStatus == 0) {
                    msgClient.sendMsg(video.getSid(), vid, 0);
                    postClient.sendVideoPost(videoConverter.Converter(video));

                    danmuService.sendDanmu(
                            new Danmu(null,
                                    vid,
                                    video.getSid(),
                                    "scroll",
                                    "#FFFFFF",
                                    1000,
                                    18,
                                    1.2,
                                    false,
                                    "text",
                                    10000000.0,
                                    new Date()
                            )
                    );
                }

            }
            else {
                // 目前逻辑跟上面一样的，但是可能以后要做一些如 记录不通过原因 等操作，所以就分开写了
                QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("vid", vid).ne("status", 3);
                Video video = videoMapper.selectOne(queryWrapper);
                if (video == null) {
                    customResponse.setCode(404);
                    customResponse.setMessage("视频不见了");
                    return customResponse;
                }

                video.setStatus(2);
                UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("vid", vid).set("status", 2).set("advice", advice);     // 更新视频状态审核不通过
                int flag = videoMapper.update(null, updateWrapper);
                if (flag <= 0) {
                    // 更新失败，处理错误情况
                    customResponse.setCode(500);
                    customResponse.setMessage("更新状态失败");
                    return customResponse;
                }
            }
            customResponse.setCode(200);
            customResponse.setMessage("更新状态成功");
            return customResponse;
        } else if (status == 3) {
            QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("vid", vid).ne("status", 3);
            Video video = videoMapper.selectOne(queryWrapper);
            if (video == null) {
                customResponse.setCode(404);
                customResponse.setMessage("视频不见了");
                return customResponse;
            }
//            String videoUrl = video.getVideoUrl();
//            String videoPrefix = videoUrl.split(ECSUrl + "/video/play/")[1];  // 视频文件名
//            System.out.println(videoPrefix);
//            String coverUrl = video.getCoverUrl();
//            String coverPrefix = coverUrl.split(ECSUrl + "/video/cover/")[1];  // 封面文件名
//            System.out.println(coverPrefix);
//            Integer lastStatus = video.getStatus();
//            File file1 = new File(ECSBucketPath + "video/", videoPrefix);
//            File file2 = new File(ECSBucketPath + "cover/", coverPrefix);
//            file1.delete();
//            file2.delete();
            UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("vid", vid).set("status", 3).set("delete_date", new Date());     // 更新视频状态已删除
            int flag = videoMapper.update(null, updateWrapper);
            if (flag <= 0) {
                // 更新失败，处理错误情况
                customResponse.setCode(500);
                customResponse.setMessage("更新状态失败");
                return customResponse;
            }
            customResponse.setCode(200);
            customResponse.setMessage("更新状态成功");

            userClient.updateStats(video.getSid(), "video_count", false, 1);

            return customResponse;
        } else if (status == 4) {
            QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("vid", vid).ne("status", 3);
            Video video = videoMapper.selectOne(queryWrapper);
            if (video == null) {
                customResponse.setCode(404);
                customResponse.setMessage("视频不见了");
                return customResponse;
            }

            UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("vid", vid).set("status", 4);
            int flag = videoMapper.update(null, updateWrapper);
            if (flag <= 0) {
                // 更新失败，处理错误情况
                customResponse.setCode(500);
                customResponse.setMessage("更新状态失败");
                return customResponse;
            }
            customResponse.setCode(200);
            customResponse.setMessage("更新状态成功");

            //userService.updateStats(video.getSid(), "video_count", false, 1);

            return customResponse;
        }
        customResponse.setCode(500);
        customResponse.setMessage("更新状态失败");
        return customResponse;
    }
}
