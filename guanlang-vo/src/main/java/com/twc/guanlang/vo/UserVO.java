package com.twc.guanlang.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVO extends BaseVO {

    private String userName;

    private String telPhone;

    private String nickName;

    private List<RoleVO> roles;

}
