package com.twc.guanlang.entity.machine;


import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * udp原始数据
 *
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_machine_udp_data")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_machine_udp_data")
public class MachineUdpData extends BaseEntity {

    @Column(name = "data")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "data", length = 500, type = MySqlTypeConstant.TEXT)
    private String data;


    @Column(name = "ip")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "ip", type = MySqlTypeConstant.VARCHAR)
    private String ip;

}



