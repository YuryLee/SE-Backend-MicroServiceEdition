package com.bilimili.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.msg.clients.SpecolClient;
import com.bilimili.msg.clients.UserClient;
import com.bilimili.msg.clients.VideoClient;
import com.bilimili.msg.dao.Msg;
import com.bilimili.msg.dao.Zhuanlan;
import com.bilimili.msg.dto.UserDTO;
import com.bilimili.msg.dto.VideoDTO;
import com.bilimili.msg.mapper.MsgMapper;
import com.bilimili.msg.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@Service
public class MsgServiceImpl implements MsgService {

    @Autowired
    private MsgMapper msgMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private VideoClient videoClient;

    @Autowired
    private SpecolClient specolClient;

    @Override
    public List<Msg> getMsgBySid(String sid) {
        QueryWrapper<Msg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).orderByDesc("time");
        return msgMapper.selectList(queryWrapper);
    }

    @Override
    public void sendMsg(String sid, Integer idSend, int type) {
        String title = "";
        String content = "";

        if (type == 0) {

            UserDTO userDTO = userClient.getUserDTOBySid(sid);
            VideoDTO videoDTO = videoClient.getVideoWithDataByVid(idSend);
            title = userDTO.getName() + "更新了一个视频，快来看看吧！";
            content = userDTO.getName() + "投稿了视频《" + videoDTO.getTitle() + "》，" + "赶快点击打开看看吧！";
            List<UserDTO> userDTOList = userClient.getFansService(sid);
            for (UserDTO userDTO1 : userDTOList) {
                Msg msg = new Msg(null, userDTO1.getSid(), title, content, idSend, new Date(), type);
                msgMapper.insert(msg);
            }
        } else if (type == 1){
            UserDTO userDTO = userClient.getUserDTOBySid(sid);
            Zhuanlan zhuanlan = specolClient.getSpecolByZid(idSend);
            title = userDTO.getName() + "更新了一个专栏，快来看看吧！";
            content = userDTO.getName() + "更新了专栏《" + zhuanlan.getTitle() + "》，" + "赶快点击打开看看吧！";
            List<UserDTO> userDTOList = userClient.getFansService(sid);
            for (UserDTO userDTO1 : userDTOList) {
                Msg msg = new Msg(null, userDTO1.getSid(), title, content, idSend, new Date(), type);
                msgMapper.insert(msg);
            }
        } else if (type == 2) {
            title = "创作周报更新";
            content = "你的创作周报更新了，快来看看吧！";
            Msg msg = new Msg(null, sid, title, content, 0, new Date(), type);
            msgMapper.insert(msg);
        } else if (type == 3) {
            title = "视频周报更新";
            content = "你的视频周报更新了，快来看看吧！";
            Msg msg = new Msg(null, sid, title, content, 0, new Date(), type);
            msgMapper.insert(msg);
        } else if (type == 4) {
            VideoDTO videoDTO = videoClient.getVideoWithDataByVid(idSend);
            title = "视频删除";
            content = "你的视频《" + videoDTO.getTitle() + "》涉嫌违规，已被管理员删除，请修改后重新上传。";
            Msg msg = new Msg(null, sid, title, content, idSend, new Date(), type);
            msgMapper.insert(msg);
        } else if (type == 5) {
            Zhuanlan zhuanlan = specolClient.getSpecolByZid(idSend);
            title = "专栏隐藏";
            content = "你的专栏《" + zhuanlan.getTitle() + "》涉嫌违规，已被管理员隐藏，请进行修改。";
            Msg msg = new Msg(null, sid, title, content, idSend, new Date(), type);
            msgMapper.insert(msg);
        }

    }
}
