package com.twc.guanlang.entity.maintain;

import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 故障类型和维修人员对应表
 *
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_repair_user")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_repair_user")
public class Repair2User extends BaseEntity {

    /**
     * 专家id
     */
    @Column(name = "userId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "userId", type = MySqlTypeConstant.INT)
    Long userId;

    /**
     * 维修类型code
     */
    @Column(name = "repairCode")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "repairCode", type = MySqlTypeConstant.VARCHAR)
    String repairCode;
}
