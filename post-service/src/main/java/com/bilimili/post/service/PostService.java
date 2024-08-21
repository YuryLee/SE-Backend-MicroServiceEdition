package com.bilimili.post.service;

import com.bilimili.post.dao.Post;
import com.bilimili.post.dto.PostDTO;
import com.bilimili.post.dto.VideoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post addPost(String sid, String content, MultipartFile[] pictures) throws IOException;

    void sendVideoPost(VideoDTO videoDTO);

    List<PostDTO> getAllPosts();

    List<PostDTO> getFollowPostsBySid(String sid);
}
