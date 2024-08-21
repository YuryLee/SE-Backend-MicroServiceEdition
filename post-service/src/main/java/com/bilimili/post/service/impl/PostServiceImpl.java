package com.bilimili.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.post.clients.UserClient;
import com.bilimili.post.clients.VideoClient;
import com.bilimili.post.dao.Post;
import com.bilimili.post.dto.PostDTO;
import com.bilimili.post.dto.UserDTO;
import com.bilimili.post.dto.VideoDTO;
import com.bilimili.post.mapper.PostMapper;
import com.bilimili.post.service.PostService;
import com.bilimili.post.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UploadUtils uploadUtils;

    @Autowired
    private VideoClient videoClient;

    @Autowired
    private UserClient userClient;

    @Override
    public Post addPost(String sid, String content, MultipartFile[] pictures) throws IOException {
        String picture = uploadUtils.uploadImages(pictures, "picture", "post");
        Post post = new Post(
                null,
                "text",
                sid,
                0,
                picture,
                content,
                new Date());
        postMapper.insert(post);
        return post;
    }

    @Override
    public void sendVideoPost(VideoDTO videoDTO) {
        String content = videoDTO.getName() + "发布了一条新视频《" + videoDTO.getTitle() + "》，快来看看吧！";
        Post post = new Post(
                null,
                "video",
                videoDTO.getSid(),
                videoDTO.getVid(),
                "",
                content,
                new Date());
        postMapper.insert(post);
    }

    @Override
    public List<PostDTO> getAllPosts() {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<Post> postList = postMapper.selectList(queryWrapper);
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post post: postList) {
            PostDTO postDTO;
            UserDTO user = userClient.getUserDTOBySid(post.getSid());
            if (post.getType().equals("video")) {
                postDTO = new PostDTO(
                        post.getPid(),
                        post.getType(),
                        post.getSid(),
                        post.getVid(),
                        post.getPicture(),
                        post.getContent(),
                        user.getName(),
                        user.getSignature(),
                        user.getAvatar_url(),
                        post.getCreateTime(),
                        videoClient.getVideoWithDataByVid(post.getVid())
                );

            } else {
                postDTO = new PostDTO(
                        post.getPid(),
                        post.getType(),
                        post.getSid(),
                        post.getVid(),
                        post.getPicture(),
                        post.getContent(),
                        user.getName(),
                        user.getSignature(),
                        user.getAvatar_url(),
                        post.getCreateTime(),
                        null
                );
            }
            postDTOList.add(postDTO);
        }
        return postDTOList;
    }

    @Override
    public List<PostDTO> getFollowPostsBySid(String sid) {
        List<UserDTO> userDTOList = userClient.getFollowsService(sid);
        List<String> sidList = new ArrayList<>();
        for (UserDTO userDTO : userDTOList) {
            sidList.add(userDTO.getSid());
        }
        if (!sidList.contains(sid)) {
            sidList.add(sid);
        }
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sid", sidList).orderByDesc("create_time");
        List<Post> postList = postMapper.selectList(queryWrapper);

        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post post: postList) {
            PostDTO postDTO;
            UserDTO user = userClient.getUserDTOBySid(post.getSid());
            if (post.getType().equals("video")) {
                postDTO = new PostDTO(
                        post.getPid(),
                        post.getType(),
                        post.getSid(),
                        post.getVid(),
                        post.getPicture(),
                        post.getContent(),
                        user.getName(),
                        user.getSignature(),
                        user.getAvatar_url(),
                        post.getCreateTime(),
                        videoClient.getVideoWithDataByVid(post.getVid())
                );

            } else {
                postDTO = new PostDTO(
                        post.getPid(),
                        post.getType(),
                        post.getSid(),
                        post.getVid(),
                        post.getPicture(),
                        post.getContent(),
                        user.getName(),
                        user.getSignature(),
                        user.getAvatar_url(),
                        post.getCreateTime(),
                        null
                );
            }
            postDTOList.add(postDTO);
        }
        return postDTOList;
    }
}
