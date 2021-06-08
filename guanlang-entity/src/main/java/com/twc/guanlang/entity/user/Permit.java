package com.twc.guanlang.entity.user;


import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


/**
 * 系统资源
 */
@Data
@Entity
@Table(name = "t_permit")

//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_permit")
public class Permit extends BaseEntity {


    /**
     * 资源描述
     */
    @Column(name = "name")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "name", type = MySqlTypeConstant.VARCHAR)
    private String name;

    /**
     * 资源路径
     */
    @Column(name = "path")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "path", type = MySqlTypeConstant.VARCHAR)
    private String path;


    /**
     * 上级
     */
    @Column(name = "parent_id")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "parent_id", type = MySqlTypeConstant.INT)
    private Integer parentId;


    /**
     * 资源类型
     */
    @Column(name = "type")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "type", type = MySqlTypeConstant.VARCHAR)
    private String type;

    public enum Constant {

        API, URI

    }

    @Transient
    private List<Permit> childs;


    @Column(name = "is_leaf")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "is_leaf", type = MySqlTypeConstant.INT)

    private Integer isLeaf;


    /**
     * 不映射数据库字段  使用@Transient
     */
    @Transient
    private Integer forUser;


    @Column(name = "code")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "code", type = MySqlTypeConstant.VARCHAR)
    String code;


}
