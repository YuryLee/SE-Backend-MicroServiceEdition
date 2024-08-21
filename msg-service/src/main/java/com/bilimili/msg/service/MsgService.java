package com.bilimili.msg.service;

import com.bilimili.msg.dao.Msg;

import java.util.List;

public interface MsgService {
    List<Msg> getMsgBySid(String sid);

    void sendMsg(String sid, Integer idSend, int type);
}
