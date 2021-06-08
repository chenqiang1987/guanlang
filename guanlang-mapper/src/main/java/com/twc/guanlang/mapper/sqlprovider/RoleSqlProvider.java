package com.twc.guanlang.mapper.sqlprovider;


public class RoleSqlProvider {


    public String selectRolesByRoleIds(String roleIds) {

        return "select  *  from  t_role where id in(" + roleIds + ")  and enable =1 ";
    }


}

