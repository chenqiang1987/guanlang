package com.twc.guanlang.param.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.param.BasePageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;


/**
 * 机器人分页列表查询
 */
@Data

@ApiModel("maintainParam")
public class MaintainParam extends BasePageParam {

//
//    /**
//     * 执行人
//     */
//    String executerName;


    /**
     * 维修人
     */
    String userName;

    /**
     * 维修状态
     */
    String status;

    /**
     * 维修开始
     */
    String start;


    /**
     * 维修结束
     */
    String end;


    /**
     * 维修类型
     */
    String repairId;


    /**
     * 由我创建和指派给我
     * byMe/toMe
     */
    Integer isByMe;


    Integer isToMe;

    /**
     * 是否由我解决
     */
    Integer isResolveByMe;

//
//
//    /**
//     * 描述
//     */
//    String desc;


//    @ParamCheck(required = true, length_min = 6, length_max = 15)
//    private String confirmPassword;
//
//    @ParamCheck(required = true, length_min = 11, length_max = 11)
//    private String telPhone;
//
//    /**
//     * 用户新增时候的角色  [1,2,3]
//     */
//    @ParamCheck(required = true)
//    private int[] roleIds;


}