package com.bilimili.video.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
@Component
public class UploadUtils {
    @Value("${ecs.bucket.path}")
    private String ECSBucketPath;

    @Value("${ecs.url}")
    private String ECSUrl;

    /**
     * 存储上传单张图片
     * @param file 图片文件
     * @param type 图片分类，如 cover、carousel、other等，不允许空字符串，这里没有做判断了，自己注意就好
     * @return  图片的URL地址
     * @throws IOException
     */
    public String uploadImage(@NonNull MultipartFile file, @NonNull String type, String resource) throws IOException {
        // 生成文件名
        String originalFilename = file.getOriginalFilename();   // 获取原文件名
        String ext = "." + FilenameUtils.getExtension(originalFilename);    // 获取文件后缀
        String uuid = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-", "");

        String fileName = date + uuid + ext;

        // 完整路径名
        String filePath = ECSBucketPath + type + "/";
        String filePathName = ECSBucketPath + type + "/" + fileName;
        System.out.println(filePathName);
        File file1 = new File(filePath, fileName);
        //如果目标文件夹不存在就创建
        if (!file1.exists()){
            file1.mkdirs();
            file1.createNewFile();
        }
        file.transferTo(file1);

        return ECSUrl + "/" + resource + "/" + type + "/" + fileName;
    }

    /**
     * 存储上传多张图片
     * @param files 图片文件
     * @param type 图片分类，如 cover、carousel、other等，不允许空字符串，这里没有做判断了，自己注意就好
     * @return  图片的URL地址
     * @throws IOException
     */
    public String uploadImages(@NonNull MultipartFile[] files, @NonNull String type, String resource) throws IOException {
        StringBuilder res = new StringBuilder();
        for (MultipartFile file : files) {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();   // 获取原文件名
            String ext = "." + FilenameUtils.getExtension(originalFilename);    // 获取文件后缀
            String uuid = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-", "");

            String fileName = date + uuid + ext;

            // 完整路径名
            String filePath = ECSBucketPath + type + "/";
            String filePathName = ECSBucketPath + type + "/" + fileName;
            System.out.println(filePathName);
            File file1 = new File(filePath, fileName);
            //如果目标文件夹不存在就创建
            if (!file1.exists()){
                file1.mkdirs();
                file1.createNewFile();
            }
            file.transferTo(file1);
            if (res.isEmpty()) {
                res.append(ECSUrl).append("/").append(resource).append("/").append(type).append("/").append(fileName);
            } else {
                res.append(",").append(ECSUrl).append("/").append(resource).append("/").append(type).append("/").append(fileName);
            }
        }

        return res.toString();
    }
}
