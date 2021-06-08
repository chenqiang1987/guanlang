package com.twc.guanlang.mapper.entity;


import com.twc.guanlang.entity.user.Permit;
import com.twc.guanlang.mapper.TwcBaseMapper;
import com.twc.guanlang.mapper.sqlprovider.PermitSqlProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface PermitMapper extends TwcBaseMapper<Permit> {


    @SelectProvider(type = PermitSqlProvider.class, method = "selectPermitsByUserId")
    @Results({
            @Result(column = "id", jdbcType = JdbcType.INTEGER, property = "id"),
            @Result(column = "name", jdbcType = JdbcType.VARCHAR, property = "name"),
            @Result(column = "path", jdbcType = JdbcType.VARCHAR, property = "path"),
            @Result(column = "parent_id", jdbcType = JdbcType.INTEGER, property = "parentId"),

    })
    List<Permit> selectPermitsByUserId(String id);


    @SelectProvider(type = PermitSqlProvider.class, method = "selectPermitsByRoleId")
    @Results({
            @Result(column = "id", jdbcType = JdbcType.INTEGER, property = "id"),
            @Result(column = "name", jdbcType = JdbcType.VARCHAR, property = "name"),
            @Result(column = "path", jdbcType = JdbcType.VARCHAR, property = "path"),
            @Result(column = "parent_id", jdbcType = JdbcType.INTEGER, property = "parentId"),

    })
    List<Permit> selectPermitsByRoleId(String id);



    @SelectProvider(type = PermitSqlProvider.class, method = "selectPermitsByUserIdNoTree")
    @Results({
            @Result(column = "id", jdbcType = JdbcType.INTEGER, property = "id"),
            @Result(column = "name", jdbcType = JdbcType.VARCHAR, property = "name"),
            @Result(column = "path", jdbcType = JdbcType.VARCHAR, property = "path"),
            @Result(column = "parent_id", jdbcType = JdbcType.INTEGER, property = "parentId"),

    })
    List<Permit> selectPermitsByUserIdNoTree(String id);



    @SelectProvider(type = PermitSqlProvider.class, method = "selectPermitsByRoleIdNoTree")
    @Results({
            @Result(column = "id", jdbcType = JdbcType.INTEGER, property = "id"),
            @Result(column = "name", jdbcType = JdbcType.VARCHAR, property = "name"),
            @Result(column = "path", jdbcType = JdbcType.VARCHAR, property = "path"),
            @Result(column = "parent_id", jdbcType = JdbcType.INTEGER, property = "parentId"),

    })
    List<Permit> selectPermitsByRoleIdNoTree(String id);
}

