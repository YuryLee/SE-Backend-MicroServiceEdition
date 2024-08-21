package com.bilimili.video.service;

import com.bilimili.video.dao.Danmu;
import com.bilimili.video.response.CustomResponse;

import java.util.List;

public interface DanmuService {
    /**
     * 根据弹幕ID集合查询弹幕列表
     * @param vid 弹幕ID集合
     * @return  弹幕列表
     */
    List<Danmu> getDanmuListByVid(Integer vid);

    /**
     * 删除弹幕
     * @param id    弹幕id
     * @param sid   操作用户
     * @return  响应对象
     */
    CustomResponse deleteDanmu(Integer id, String sid);

    CustomResponse sendDanmu(Danmu danmu);
}
