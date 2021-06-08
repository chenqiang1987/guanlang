package com.twc.guanlang.entity.maintain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 工单
 */
@Data
@Entity
@Table(name = "t_workOrder")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_workOrder")
public class WorkOrder extends BaseEntity {


    /**
     * 故障 告警id
     */
    @Column(name = "falutId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "falutId", type = MySqlTypeConstant.BIGINT)
    Long falutId;


    @Column(name = "faultCode")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "faultCode", type = MySqlTypeConstant.VARCHAR)
    String faultCode;


    /**
     * 故障类型 就是 维修名称
     */
    @Column(name = "faultName")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "faultName", type = MySqlTypeConstant.VARCHAR)
    String faultName;


    /**
     * 维修人员
     */
    @Column(name = "userId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "userId", type = MySqlTypeConstant.BIGINT)
    Long userId;

    /**
     * 执行人
     */
    @Column(name = "executerId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "executerId", type = MySqlTypeConstant.BIGINT)
    Long executerId;


    /**
     * 解决人id
     */
    @Column(name = "resolveUserId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "resolveUserId", type = MySqlTypeConstant.BIGINT)
    Long resolveUserId;


    /**
     * 维修内容
     */
    @Column(name = "content")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "content", length = 5000, type = MySqlTypeConstant.VARCHAR)
    String content;


    /**
     * 维修结果
     */
    @Column(name = "result")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "result", length = 5000, type = MySqlTypeConstant.VARCHAR)
    String result;

    /**
     * 维修开始
     */
    @Column(name = "start")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "start", type = MySqlTypeConstant.DATETIME)
    Date start;

    /**
     * 维修结束
     */
    @Column(name = "end")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "end", type = MySqlTypeConstant.DATETIME)
    Date end;


    /**
     * 维修状态
     */
    @Column(name = "status")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "status", type = MySqlTypeConstant.VARCHAR)
    String status;


    @Column(name = "statusName")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "statusName", type = MySqlTypeConstant.VARCHAR)
    String statusName;


    /**
     * 图片 多个用,隔开
     */
    @Column(name = "imgsUrl")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "imgsUrl", type = MySqlTypeConstant.VARCHAR)
    String imgsUrl;


    @Column(name = "vedioUrl")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "vedioUrl", type = MySqlTypeConstant.VARCHAR)
    String vedioUrl;


    /**
     * 是否自动工单  0 手工 1自动工单
     */
    @Column(name = "isAuto")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "isAuto", type = MySqlTypeConstant.INT)
    Integer isAuto;


//   public static  class WORK_STATUS{
//
//       /**
//        * 新建等待处理工单
//        */
//        public  static String WAIT_REPAIR="WAIT_REPAIR";
//
//       /**
//        * 维修中工单
//        */
//       public  static String REPAIRING="REPAIRING";
//
//       /**
//        * 已完成
//        */
//       public  static String COMPLETED="COMPLETED";
//
//
//
//    }

    public enum CODE1 {

        WAIT_REPAIR("WAIT_REPAIR", "等待维修"),
        REPAIRING("REPAIRING", "正在维修中"),
        FAILED("FAILED", "维修失败"),
        COMPLETED("COMPLETED", "已经完成");

        private String code;
        private String msg;

        CODE1(String s, String i) {

            this.code = s;
            this.msg = i;

        }


        public String getCode() {
            return this.code;
        }

        public String getMsg() {
            return this.msg;
        }
    }

}
