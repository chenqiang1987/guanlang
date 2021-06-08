package com.twc.guanlang.entity.machine;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 机器人故障上报信息
 *
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_reportFault")
public class ReportFault {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "id", type = MySqlTypeConstant.INT, length = 11, isKey = true, isAutoIncrement = true)
    private Integer id;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "create_time", type = MySqlTypeConstant.DATETIME)
    private Date createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "update_time", type = MySqlTypeConstant.DATETIME)

    private Date updateTime;

    /**
     * 是否可用
     */
    @Column(name = "enable")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "enable", type = MySqlTypeConstant.VARCHAR)
    private String enable;

    /**
     * 机器人编号
     */
    @Column(name = "serial_number")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "enable", type = MySqlTypeConstant.VARCHAR)
    private String serialNumber;

    /**
     * 故障信息
     */
    @Column(name = "msg")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "msg", type = MySqlTypeConstant.VARCHAR)
    private String msg;

    /**
     * 信息唯一标识
     */
    @Column(name = "msgId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "msgId", type = MySqlTypeConstant.VARCHAR)
    private String msgId;

    /**
     * 故障时间戳
     */
    @Column(name = "timeStamp")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "timeStamp", type = MySqlTypeConstant.BIGINT)
    private Long timeStamp;

}
