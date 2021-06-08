package com.twc.guanlang.mapper.sqlprovider;


import com.twc.guanlang.common.str.TwcStringUtil;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.param.user.MaintainParam;
import tk.mybatis.mapper.util.StringUtil;


public class MaintainSqlProvider {


    public String selectPage(MaintainParam maintainParam, SystemUser systemUser) {


        String sql = " SELECT\n" +
                "\tword.id,\n" +
                "\tword.faultName type ,\n" +
                "\trepairUser.nick_name repairName,\n" +
                "\texeUser.nick_name exeName,\n" +
                "\tword. statusName `status`,  \t" +
                "  word.content `content`,\n" +
                "  word.result `result`,\n" +
                "\tfault.x x,\n" +
                "\tfault.y y ,\n" +
                "\tword.imgsUrl imgsUrl,\n" +
                "\tword.vedioUrl vedioUrl\n" +
                "FROM\n" +
                "\tt_workorder word\n" +

                " JOIN t_system_user exeUser ON word.executerId = exeUser.id " +
                " LEFT JOIN t_warning fault ON word.falutId = fault.id\n" +
                " LEFT JOIN t_system_user repairUser ON word.userId = repairUser.id " +
                " where  1 =1 ";

        if (maintainParam.getIsByMe() != null && maintainParam.getIsByMe() == 1) {
            sql += " and word.executerId= " + systemUser.getId() + " ";
        }

        if (maintainParam.getIsToMe() != null && maintainParam.getIsToMe() == 1) {
            sql += " and word.userId= " + systemUser.getId() + " ";
        }

        if (maintainParam.getIsResolveByMe() != null && maintainParam.getIsResolveByMe() == 1) {
            sql += " and word.resolveUserId= " + systemUser.getId() + " ";
        }

        String exeName = "";
        String userName = "";
        String desc = "";
        if (StringUtil.isNotEmpty(maintainParam.getUserName())) exeName = maintainParam.getUserName();
        if (StringUtil.isNotEmpty(maintainParam.getUserName())) userName = maintainParam.getUserName();
        if (StringUtil.isNotEmpty(maintainParam.getUserName())) desc = maintainParam.getUserName();
        sql += " and ( exeUser.nick_name like '%" + userName + "%' " +
                "  or repairUser.nick_name like '%" + userName + "%' " +
                " or word.content like '%" + userName + "%' ) ";

        if (TwcStringUtil.isNotEmpty(maintainParam.getStatus())) {
            sql += " and word.status= '" + maintainParam.getStatus() + "' ";
        }


        if (TwcStringUtil.isNotEmpty(maintainParam.getRepairId())) {
            sql += " and fault.`type`= '" + maintainParam.getRepairId() + "' ";
        }

        if (TwcStringUtil.isNotEmpty(maintainParam.getStart())) {
            sql += " and word.`start` >= '" + maintainParam.getStart() + "' ";
        }

        if (TwcStringUtil.isNotEmpty(maintainParam.getEnd())) {
            sql += " and word.`start`  <='" + maintainParam.getEnd() + "' ";
        }


//        System.out.println(sql);
        return sql;
    }


}

