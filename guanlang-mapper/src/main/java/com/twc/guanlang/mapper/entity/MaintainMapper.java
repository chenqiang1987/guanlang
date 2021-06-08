package com.twc.guanlang.mapper.entity;


import com.twc.guanlang.entity.machine.Warning;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.mapper.TwcBaseMapper;
import com.twc.guanlang.mapper.sqlprovider.MaintainSqlProvider;
import com.twc.guanlang.param.user.MaintainParam;
import com.twc.guanlang.vo.MaintainVO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface MaintainMapper extends TwcBaseMapper<Warning> {



    @SelectProvider(type = MaintainSqlProvider.class, method = "selectPage")
    @Results({
            @Result(column = "id", jdbcType = JdbcType.INTEGER, property = "id"),
            @Result(column = "repairName", jdbcType = JdbcType.VARCHAR, property = "repairName"),
            @Result(column = "exeName", jdbcType = JdbcType.VARCHAR, property = "exeName"),
            @Result(column = "type", jdbcType = JdbcType.VARCHAR, property = "type"),
            @Result(column = "status", jdbcType = JdbcType.VARCHAR, property = "status"),
            @Result(column = "start", jdbcType = JdbcType.DATE, property = "start"),
            @Result(column = "end", jdbcType = JdbcType.DATE , property = "end"),
            @Result(column = "content", jdbcType = JdbcType.VARCHAR , property = "content"),
            @Result(column = "result", jdbcType = JdbcType.VARCHAR , property = "result"),
            @Result(column = "x", jdbcType = JdbcType.VARCHAR , property = "x"),
            @Result(column = "y", jdbcType = JdbcType.VARCHAR , property = "y"),
            @Result(column = "imgsUrl", jdbcType = JdbcType.VARCHAR , property = "imgsUrl"),
            @Result(column = "vedioUrl", jdbcType = JdbcType.VARCHAR , property = "vedioUrl")
    })
    List<MaintainVO> selectPage(MaintainParam maintainParam,SystemUser systemUser);

}

