package com.twc.guanlang.entity.machine;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 机器人任务
 *
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_auto_mession")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_auto_mession")

public class AutoMession extends BaseEntity {

    @Column(name = "machineId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "machineId", type = MySqlTypeConstant.BIGINT)
    Long machineId;


    @Column(name = "start")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "start", type = MySqlTypeConstant.VARCHAR)
    String start;

    @Column(name = "end")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "end", type = MySqlTypeConstant.DATETIME)
    Date end;

    /**
     * 任务描述
     */
    @Column(name = "content")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "content", type = MySqlTypeConstant.VARCHAR)
    String content;

    /**
     * 状态
     */
    @Column(name = "status")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "status", type = MySqlTypeConstant.VARCHAR)
    String status;

    /**
     * 实际发生时间
     */
    @Column(name = "startedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "startedAt", type = MySqlTypeConstant.DATETIME)
    Date startedAt;


    /**
     * 结束时间
     */
    @Column(name = "endAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "endAt", type = MySqlTypeConstant.DATETIME)
    Date endAt;

    @Column(name = "type")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "type", type = MySqlTypeConstant.VARCHAR)
    String type;

    /**
     * 执行机器编号
     */
    @Column(name = "executerId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "executerId", type = MySqlTypeConstant.BIGINT)
    Long executerId;

    /**
     * 半自动目的地
     */
    @Column(name = "halfTarget")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "halfTarget", type = MySqlTypeConstant.VARCHAR)
    String halfTarget;
    /**
     * 半自动目的地x
     */
    @Column(name = "halfTargetX")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "halfTargetX", type = MySqlTypeConstant.VARCHAR)
    float halfTargetX;

    /**
     * 半自动目的地Y
     */
    @Column(name = "halfTargetY")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "halfTargetY", type = MySqlTypeConstant.VARCHAR)
    float halfTargetY;

    /**
     * 任务开始位置
     */
    @Column(name = "startPostion")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "startPostion", type = MySqlTypeConstant.VARCHAR)
    float startPostion;

    /**
     * 开始位置x
     */
    @Column(name = "startPostionX")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "startPostionX", type = MySqlTypeConstant.VARCHAR)
    float startPostionX;

    /**
     * 开始位置y
     */
    @Column(name = "startPostionY")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "startPostionY", type = MySqlTypeConstant.VARCHAR)
    float startPostionY;

    /**
     * 结束位置
     */
    @Column(name = "endPostion")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "endPostion", type = MySqlTypeConstant.VARCHAR)
    float endPostion;

    /**
     * 结束位置X
     */
    @Column(name = "endPostionX")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "endPostionX", type = MySqlTypeConstant.VARCHAR)
    float endPostionX;

    /**
     * 结束位置Y
     */
    @Column(name = "endPostionY")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "endPostionY", type = MySqlTypeConstant.VARCHAR)
    float endPostionY;

    /**
     * 失败原因
     */
    @Column(name = "reason")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "reason", type = MySqlTypeConstant.VARCHAR)
    String reason;

    /**
     * 是否达到头
     */
    @Column(name = "hasReachHead")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "hasReachHead", type = MySqlTypeConstant.INT)
    Integer hasReachHead;

    /**
     * 是否达到尾
     */
    @Column(name = "hasReachTail")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "hasReachTail", type = MySqlTypeConstant.INT)
    Integer hasReachTail;

    /**
     * 任务类型
     */
    public enum TYPE_ENUM {
        /**
         *
         */
        AUTO("AUTO", "自动模式"),
        HALF("HALF", "半自动模式"),
        HAND("HAND", "手动模式");

        /**
         * 对应udp消息里面的code
         */
        private String code;
        private String msg;

        TYPE_ENUM(String s, String i) {
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

    /**
     * 任务状态
     */
    public enum STATUS_ENUM {
        /**
         *
         */
        WAIT_EXECUTE("WAIT_EXECUTE", "等待执行"),
        EXECUTING("EXECUTING", "执行中"),
        FAILED("FAILED", "执行失败"),
        COMPLETE("COMPLETE", "执行完毕");

        /**
         * 对应udp消息里面的code
         */
        private String code;
        private String msg;

        STATUS_ENUM(String s, String i) {
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
