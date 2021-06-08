package com.twc.guanlang.entity.recharge;

import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data

public class RechargeConstants extends BaseEntity {


    public static String STATUS_CLOSE = "close";

    public static String STATUS_OPEN = "open";

    public static String STATUS_FAULT = "fault";


}
