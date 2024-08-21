package com.bilimili.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bilimili.user.dao.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
