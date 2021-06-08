package com.twc.guanlang.mapper.entity;


import com.twc.guanlang.entity.machine.MonitorData;
import com.twc.guanlang.mapper.TwcBaseMapper;
import com.twc.guanlang.mapper.sqlprovider.MonitorDataSqlProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;


public interface MonitorDataMapper extends TwcBaseMapper<MonitorData> {


    /**
     * 查询各机器人最新的报文数据时间戳，非创建时间
     *
     * @return
     */
    @SelectProvider(type = MonitorDataSqlProvider.class, method = "selectLatestDataTimeStamp")
    @Results({
            @Result(column = "maxTime", jdbcType = JdbcType.BIGINT, property = "maxTime"),
            @Result(column = "serialNumber", jdbcType = JdbcType.VARCHAR, property = "serialNumber")
    })
    public List<Data> selectLatestDataTimeStamp();


    @lombok.Data
    public static class Data {

        private Long maxTime;
        private String serialNumber;

    }

}

