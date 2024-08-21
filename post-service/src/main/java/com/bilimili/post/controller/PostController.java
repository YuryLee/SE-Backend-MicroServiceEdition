package com.bilimili.post.controller;

import com.bilimili.post.dao.Post;
import com.bilimili.post.dto.PostDTO;
import com.bilimili.post.dto.VideoDTO;
import com.bilimili.post.response.CustomResponse;
import com.bilimili.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@CrossOrigin
@RestController
@Tag(name = "动态api")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/post/add-text")
    @Operation(summary = "添加一个文字动态，type text文字video视频")
    public CustomResponse addPost(@RequestParam("sid") String sid,
                                  @RequestParam("content") String content,
                                  @RequestParam(value = "pictures", required = false) MultipartFile[] pictures) throws IOException {
        CustomResponse customResponse = new CustomResponse();
        Post post = postService.addPost(sid, content, pictures);
        customResponse.setData(post);
        return customResponse;
    }

    @Value("${ecs.bucket.path}")
    private String ECSBucketPath;

    @GetMapping("/post/picture/{name}")
    @Operation(summary = "获得动态图片")
    public void getUserAvatar(@PathVariable String name, HttpServletResponse response) {
        // ECSBucketPath = "/usr/local/springboot/";
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(ECSBucketPath + "picture/" + name));

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

    @GetMapping("post/all")
    @Operation(summary = "获取所有动态")
    public CustomResponse getAllPosts() {
        CustomResponse customResponse = new CustomResponse();
        List<PostDTO> postDTOList = postService.getAllPosts();
        customResponse.setData(postDTOList);
        return customResponse;
    }

    @GetMapping("post/sid-follow/{sid}")
    @Operation(summary = "获取sid关注的所有动态")
    public CustomResponse getFollowPostsBySid(@PathVariable String sid) {
        CustomResponse customResponse = new CustomResponse();
        List<PostDTO> postDTOList = postService.getFollowPostsBySid(sid);
        customResponse.setData(postDTOList);
        return customResponse;
    }

    // 微服务接口
    @PostMapping("post-service/sendVideoPost")
    @Operation(summary = "发送视频动态")
    public void sendVideoPost(@RequestBody VideoDTO videoDTO) {
        postService.sendVideoPost(videoDTO);
    }

}
