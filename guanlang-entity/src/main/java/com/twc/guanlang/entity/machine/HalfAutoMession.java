package com.twc.guanlang.entity.machine;


import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 机器人半自动任务
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_half_auto_mession")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_half_auto_mession")

public class HalfAutoMession extends BaseEntity {


   @Column(name = "machineId")
   @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "machineId", type = MySqlTypeConstant.BIGINT)
   Integer machineId;


   /**
    * 巡检任务点
    */
   @Column(name = "messionPosition")
   @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "messionPosition", type = MySqlTypeConstant.VARCHAR)
   String start;



}
