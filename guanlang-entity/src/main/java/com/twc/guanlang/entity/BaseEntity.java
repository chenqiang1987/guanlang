package com.twc.guanlang.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 * 实体对象基类
 *
 * @author chenqiang
 */
@Data
public class BaseEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "id", type = MySqlTypeConstant.INT, length = 11, isKey = true, isAutoIncrement = true)
    private Long id;

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

    public static class ENABLE {
        public static String ENABLE = "1";
        public static String DISABLE = "0";
    }

}

