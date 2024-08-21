package com.bilimili.specol.service;

import com.bilimili.specol.dao.Zhuanlan;
import com.bilimili.specol.dto.VideoDTO;
import com.bilimili.specol.dto.ZhuanlanDTO;
import com.bilimili.specol.response.CustomResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface ZhuanlanService {
    List<Zhuanlan> getZhuanlans(String sid, boolean isOwner);

    CustomResponse addZhuanlan(String sid, String title, String desc, Integer visible, MultipartFile cover) throws IOException;

    Zhuanlan updateZhuanlan(Integer zid, String sid, String title, String desc, Integer visible);

    CustomResponse delZhuanlan(Integer zid);

    List<VideoDTO> getAllVideosByZid(Integer zid);

    ZhuanlanDTO getAllDataByZid(Integer zid);

    CustomResponse addVideosToZhuanlan(Integer zid, List<Integer> vidList);

    CustomResponse updateVideosFromZhuanlan(Integer zid, List<Integer> vidList);

    List<Zhuanlan> getAllZhuanlans();

    Zhuanlan updateZhuanlanVisible(Integer zid, Integer visible);

    List<VideoDTO> getVideosNotInZid(String sid, Integer zid);

    CustomResponse updateZhuanlanStatus(Integer zid, Integer status);

    CustomResponse collectVideos(String sid, String vidString);
}
