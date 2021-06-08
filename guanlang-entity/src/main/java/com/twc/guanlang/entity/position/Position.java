package com.twc.guanlang.entity.position;

import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 位置
 *
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_position")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_position")
public class Position extends BaseEntity {

    @Column(name = "x")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "x", type = MySqlTypeConstant.DOUBLE)
    float x;

    @Column(name = "y")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "y", type = MySqlTypeConstant.DOUBLE)
    float y;

    @Column(name = "name")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "name", type = MySqlTypeConstant.VARCHAR)
    String name;

    /**
     * 任务状态
     */
    public enum POSITION_NAME_ENUM {
        /**
         *
         */
        HEAD("HEAD", "管廊头"),
        TAIL("TAIL", "管廊尾");

        /**
         * 对应udp消息里面的code
         */
        private String code;
        private String msg;

        POSITION_NAME_ENUM(String s, String i) {
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
