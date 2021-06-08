package com.twc.guanlang.param.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.param.BasePageParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;


/**
 * 工单创建param
 */
@Data

@ApiModel("WorkOrderCreteParam")
public class WorkOrderCreteParam   {

//
//    /**
//     * 故障 告警id
//     */
//    @Column(name = "falutId")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "falutId", type = MySqlTypeConstant.BIGINT)
//    Long falutId;


    @Column(name = "faultCode")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "faultCode", type = MySqlTypeConstant.VARCHAR)
    String faultCode;



    /**
     * 维修人员
     */
    @Column(name = "userId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "userId", type = MySqlTypeConstant.BIGINT)
    Long userId;

//    /**
//     * 执行人
//     */
//    @Column(name = "executerId")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "executerId", type = MySqlTypeConstant.BIGINT)
//    Long  executerId;




    /**
     * 维修内容
     */
    @Column(name = "content")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "content",length = 5000,type = MySqlTypeConstant.VARCHAR)
    String  content;
//
//    /**
//     * 维修开始
//     */
//    @Column(name = "start")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "start", type = MySqlTypeConstant.DATETIME)
//    Date start;

//    /**
//     * 维修结束
//     */
//    @Column(name = "end")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "end", type = MySqlTypeConstant.DATETIME)
//    Date end;


    /**
     * 维修状态
//     */
//    @Column(name = "status")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "status", type = MySqlTypeConstant.VARCHAR)
//    String status;


//    @Column(name = "statusName")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "statusName", type = MySqlTypeConstant.VARCHAR)
//    String statusName;


    /**
     * 图片 多个用,隔开
     */
    @Column(name = "imgsUrl")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "imgsUrl", type = MySqlTypeConstant.VARCHAR)
    String  imgsUrl;


    @Column(name = "vedioUrl")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "vedioUrl", type = MySqlTypeConstant.VARCHAR)
    String  vedioUrl;

}