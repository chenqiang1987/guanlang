package com.twc.guanlang.mapper.entity;


import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.mapper.TwcBaseMapper;
import com.twc.guanlang.mapper.sqlprovider.UserSqlProvider;
import com.twc.guanlang.param.user.UserPageParam;
import com.twc.guanlang.vo.RoleVO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface UserMapper extends TwcBaseMapper<SystemUser> {



    @SelectProvider(type = UserSqlProvider.class, method = "selectUserPage")
    @Results({
            @Result(column = "id", jdbcType = JdbcType.INTEGER, property = "id"),
            @Result(column = "nick_name", jdbcType = JdbcType.VARCHAR, property = "nickName"),
            @Result(column = "tel_phone", jdbcType = JdbcType.VARCHAR, property = "telPhone"),
            @Result(column = "user_name", jdbcType = JdbcType.VARCHAR, property = "userName"),
            @Result(column = "create_time", jdbcType = JdbcType.DATE, property = "createTime"),
            @Result(column = "update_time", jdbcType = JdbcType.DATE, property = "updateTime"),
            @Result(column = "enable", jdbcType = JdbcType.VARCHAR, property = "enable")
    })
    List<SystemUser> selectUserPage(UserPageParam userPageParam);

}

