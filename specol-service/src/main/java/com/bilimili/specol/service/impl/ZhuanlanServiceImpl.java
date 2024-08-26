package com.bilimili.specol.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bilimili.specol.clients.MsgClient;
import com.bilimili.specol.clients.UserClient;
import com.bilimili.specol.clients.VideoClient;
import com.bilimili.specol.converter.VideoConverter;
import com.bilimili.specol.dao.UserVideo;
import com.bilimili.specol.dao.Video;
import com.bilimili.specol.dao.Zhuanlan;
import com.bilimili.specol.dao.ZhuanlanVideo;
import com.bilimili.specol.dto.VideoDTO;
import com.bilimili.specol.dto.ZhuanlanDTO;
import com.bilimili.specol.mapper.ZhuanlanMapper;
import com.bilimili.specol.mapper.ZhuanlanVideoMapper;
import com.bilimili.specol.response.CustomResponse;
import com.bilimili.specol.service.ZhuanlanService;
import com.bilimili.specol.utils.UploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Description:
 *
 * @author Yury
 */
@Slf4j
@Service
public class ZhuanlanServiceImpl implements ZhuanlanService {

    @Autowired
    private ZhuanlanMapper zhuanlanMapper;

    @Autowired
    UploadUtils uploadUtils;

    @Autowired
    private ZhuanlanVideoMapper zhuanlanVideoMapper;

    @Autowired
    private VideoClient videoClient;

    @Autowired
    private MsgClient msgClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private VideoConverter videoConverter;

    @Override
    public List<Zhuanlan> getZhuanlans(String sid, boolean isOwner) {
        QueryWrapper<Zhuanlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid);
        List<Zhuanlan> list = zhuanlanMapper.selectList(queryWrapper);
        if (list != null) {
            if (!isOwner) {
                List<Zhuanlan> list1 = new ArrayList<>();
                for (Zhuanlan zhuanlan : list) {
                    if (zhuanlan.getVisible() == 1) {
                        list1.add(zhuanlan);
                    }
                }
                return list1;
            }
            return list;
        }
//        queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("uid", uid).ne("is_delete", 1).orderByDesc("fid");
//        list = favoriteMapper.selectList(queryWrapper);
//        if (list != null && !list.isEmpty()) {
//            // 使用事务批量操作 减少连接sql的开销
//            try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
//                // 设置收藏夹封面
//                list.stream().parallel().forEach(favorite -> {
//                    if (favorite.getCover() == null) {
//                        Set<Object> set = redisUtil.zReverange("favorite_video:" + favorite.getFid(), 0, 0);    // 找到最近一个收藏的视频
//                        if (set != null && set.size() > 0) {
//                            Integer vid = (Integer) set.iterator().next();
//                            Video video = videoMapper.selectById(vid);
//                            favorite.setCover(video.getCoverUrl());
//                        }
//                    }
//                });
//                sqlSession.commit();
//            }
//            List<Favorite> finalList = list;
//            CompletableFuture.runAsync(() -> {
//                redisUtil.setExObjectValue(key, finalList);
//            }, taskExecutor);
//            if (!isOwner) {
//                List<Favorite> list1 = new ArrayList<>();
//                for (Favorite favorite : list) {
//                    if (favorite.getVisible() == 1) {
//                        list1.add(favorite);
//                    }
//                }
//                return list1;
//            }
//            return list;
//        }
        return Collections.emptyList();
    }

    @Override
    public CustomResponse addZhuanlan(String sid, String title, String desc, Integer visible, MultipartFile cover) throws IOException {
        // 懒得做字数等的合法判断了，前端做吧

        if (title.trim().isEmpty()) {
            return new CustomResponse(500, "标题不能为空", null);
        }
        if (title.length() > 80) {
            return new CustomResponse(500, "标题不能超过80字", null);
        }
        if (desc.length() > 2000) {
            return new CustomResponse(500, "简介太长啦", null);
        }

        // 保存封面服务器，返回URL
        String coverUrl = uploadUtils.uploadImage(cover, "cover", "video");

        Zhuanlan zhuanlan = new Zhuanlan(null, sid, visible, coverUrl, title, desc, 0, new Date(), 0);
        zhuanlanMapper.insert(zhuanlan);
        return new CustomResponse();
    }

    @Override
    public Zhuanlan updateZhuanlan(Integer zid, String sid, String title, String desc, Integer visible) {
        Zhuanlan zhuanlan = zhuanlanMapper.selectById(zid);
        if (!Objects.equals(zhuanlan.getSid(), sid)) {
            return null;
        }
        UpdateWrapper<Zhuanlan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("zid", zid).set("title", title).set("description", desc).set("visible", visible);
        zhuanlanMapper.update(null, updateWrapper);
        return zhuanlan;
    }

    @Override
    public CustomResponse delZhuanlan(Integer zid) {
        CustomResponse customResponse = new CustomResponse();
        QueryWrapper<ZhuanlanVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("zid", zid);
        zhuanlanVideoMapper.delete(queryWrapper);
        QueryWrapper<Zhuanlan> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("zid", zid);
        zhuanlanMapper.delete(queryWrapper1);
        customResponse.setMessage("删除成功");
        return customResponse;
    }

    @Override
    public List<VideoDTO> getAllVideosByZid(Integer zid) {
        QueryWrapper<ZhuanlanVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("zid", zid);
        List<ZhuanlanVideo> zhuanlanVideoList = zhuanlanVideoMapper.selectList(queryWrapper);
        List<VideoDTO> videoDTOList = new ArrayList<>();
        for (ZhuanlanVideo zhuanlanVideo : zhuanlanVideoList) {
            VideoDTO videoDTO = videoClient.getVideoWithDataByVid(zhuanlanVideo.getVid());
            if (videoDTO.getStatus() == 1) {
                videoDTOList.add(videoDTO);
            }
        }
        return videoDTOList;
    }

    @Override
    public ZhuanlanDTO getAllDataByZid(Integer zid) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Zhuanlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("zid", zid);
        Zhuanlan zhuanlan = zhuanlanMapper.selectOne(queryWrapper);
        ZhuanlanDTO zhuanlanDTO = new ZhuanlanDTO(
                zhuanlan.getZid(),
                zhuanlan.getSid(),
                zhuanlan.getVisible(),
                zhuanlan.getCover(),
                zhuanlan.getTitle(),
                zhuanlan.getDescription(),
                zhuanlan.getCount(),
                zhuanlan.getUpdateTime(),
                zhuanlan.getStatus(),
                getAllVideosByZid(zid)
        );

        return zhuanlanDTO;
    }

    @Override
    public CustomResponse addVideosToZhuanlan(Integer zid, List<Integer> vidList) {
        CustomResponse customResponse = new CustomResponse();
        StringBuilder msg = new StringBuilder();
        for(Integer vid : vidList) {
            if (videoClient.getVideoWithDataByVid(vid).getStatus() == 1) {
                msg.append(',' + vid);
                zhuanlanVideoMapper.insert(new ZhuanlanVideo(null, vid, zid, new Date()));
                zhuanlanMapper.update(null, new UpdateWrapper<Zhuanlan>().eq("zid", zid).setSql("count = count + 1"));
            }
        }
        customResponse.setMessage("加入视频" + msg + "加入成功");

        UpdateWrapper<Zhuanlan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("zid", zid).set("update_time", new Date());
        zhuanlanMapper.update(updateWrapper);

        String sid = zhuanlanMapper.selectById(zid).getSid();

        msgClient.sendMsg(sid, zid, 1);

        return customResponse;
    }

    @Override
    public CustomResponse updateVideosFromZhuanlan(Integer zid, List<Integer> vidList) {
        CustomResponse customResponse = new CustomResponse();
        for(Integer vid : vidList) {
            QueryWrapper<ZhuanlanVideo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("vid", vid).eq("zid", zid);
            if (zhuanlanVideoMapper.exists(queryWrapper)) {
                zhuanlanVideoMapper.delete(queryWrapper);
                zhuanlanMapper.update(null, new UpdateWrapper<Zhuanlan>().eq("zid", zid).setSql("count = count - 1"));
            }

        }
        customResponse.setMessage("删除成功");

        UpdateWrapper<Zhuanlan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("zid", zid).set("update_time", new Date());
        zhuanlanMapper.update(updateWrapper);

        return customResponse;
    }

    @Override
    public List<Zhuanlan> getAllZhuanlans() {
        QueryWrapper<Zhuanlan> queryWrapper = new QueryWrapper<>();
        return zhuanlanMapper.selectList(queryWrapper);
    }

    @Override
    public Zhuanlan updateZhuanlanVisible(Integer zid, Integer visible) {
        Zhuanlan zhuanlan = zhuanlanMapper.selectById(zid);
        UpdateWrapper<Zhuanlan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("zid", zid).set("visible", visible);
        zhuanlanMapper.update(null, updateWrapper);
        return zhuanlan;
    }

    @Override
    public List<VideoDTO> getVideosNotInZid(String sid, Integer zid) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).eq("status", 1);
        List<Video> videoList = videoClient.getVideoListBySid(sid);

        List<VideoDTO> videoDTOList1 = new ArrayList<>();
        for (Video video: videoList) {
            videoDTOList1.add(videoConverter.Converter(video));
        }

        List<VideoDTO> videoDTOList2 = getAllVideosByZid(zid);

        List<VideoDTO> videoDTOList = new ArrayList<>();
        for (VideoDTO videoDTO1 : videoDTOList1) {
            if (!videoDTOList2.contains(videoDTO1)) {
                videoDTOList.add(videoDTO1);
            }
        }
        return videoDTOList;
    }

    @Override
    public CustomResponse updateZhuanlanStatus(Integer zid, Integer status) {
        UpdateWrapper<Zhuanlan> zhuanlanUpdateWrapper = new UpdateWrapper<>();
        zhuanlanUpdateWrapper.eq("zid", zid).set("status", status);
        zhuanlanMapper.update(null, zhuanlanUpdateWrapper);
        return new CustomResponse(200, "修改成功", null);
    }



    @Override
    public CustomResponse collectVideos(String sid, String vidString) {
        String[] vidStringList = vidString.split(",");
        List<Integer> vidList = new ArrayList<>();
        System.out.println(Arrays.toString(vidStringList));
        for (String s: vidStringList) {
            vidList.add(Integer.parseInt(s));
        }
        CustomResponse customResponse = new CustomResponse();

        for(Integer vid : vidList) {
            if (!videoClient.isCollectService(sid, vid)) {
                QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sid", sid).eq("vid", vid);
                UserVideo userVideo = videoClient.getUserVideo(sid, vid);
                VideoDTO video = videoClient.getVideoWithDataByVid(vid);
                if (userVideo == null) {
                    // 记录不存在，创建新记录
                    userVideo = new UserVideo(
                            null,
                            sid,
                            vid,
                            0,
                            0,
                            0,
                            0,
                            1,
                            null,
                            null,
                            null);
                    videoClient.insertUserVideo(userVideo);

                    videoClient.updateStats(vid, "collect", true, 1);
                    userClient.updateStats(video.getSid(), "collect_video_count", true, 1);

                } else {
                    userVideo.setCollect(1);
                    videoClient.updateUserVideoById(userVideo);

                    videoClient.updateStats(vid, "collect", true, 1);
                    userClient.updateStats(video.getSid(), "collect_video_count", true, 1);
                }
            }
        }
        customResponse.setMessage("收藏成功");

        return customResponse;
    }
}
