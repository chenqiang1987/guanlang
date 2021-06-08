package com.twc.guanlang.entity.machine;


import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 机器人自动任务历史
 */
@Data
@Entity
@Table(name = "t_auto_mession_history")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_auto_mession_history")

public class AutoMessionHistory extends BaseEntity {


   @Column(name = "machineId")

   @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "machineId", type = MySqlTypeConstant.BIGINT)
   Integer machineId;

   @Column(name = "start")
   @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "start", type = MySqlTypeConstant.DATETIME)
   Date start;

   @Column(name = "end")
   @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "end", type = MySqlTypeConstant.DATETIME)
   Date end;


   /**
    * 任务描述
    */
   @Column(name = "content")
   @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "content", type = MySqlTypeConstant.VARCHAR)
   String content;

}
