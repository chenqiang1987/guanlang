package com.twc.guanlang.vo;

import lombok.Data;

import java.util.List;


/**
 * 在线专家
 */
@Data
public class ExpertVO extends BaseVO {

    private String userName;

    private String telPhone;

    private String nickName;

    /**
     * 是否在线
     */
    private Integer isOnline;



}
