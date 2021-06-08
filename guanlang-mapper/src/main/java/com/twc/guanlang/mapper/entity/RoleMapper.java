package com.twc.guanlang.mapper.entity;


import com.twc.guanlang.entity.user.Role;
import com.twc.guanlang.mapper.TwcBaseMapper;
import com.twc.guanlang.mapper.sqlprovider.RoleSqlProvider;
import com.twc.guanlang.vo.RoleVO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface RoleMapper extends TwcBaseMapper<Role> {


    @SelectProvider(type = RoleSqlProvider.class, method = "selectRolesByRoleIds")
    @Results({
            @Result(column = "id", jdbcType = JdbcType.INTEGER, property = "id"),
            @Result(column = "name", jdbcType = JdbcType.VARCHAR, property = "name"),
            @Result(column = "nick_name", jdbcType = JdbcType.VARCHAR, property = "nickName"),
            @Result(column = "create_time", jdbcType = JdbcType.VARCHAR, property = "createTime"),
            @Result(column = "update_time", jdbcType = JdbcType.VARCHAR, property = "updateTime")
    })
    List<RoleVO> selectRolesByRoleIds(String roleIds);


}

