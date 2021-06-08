package com.twc.guanlang.mapper.sqlprovider;


public class PermitSqlProvider {


    public String selectPermitsByIds(String ids) {

        return "select  *  from  t_permit where id in(" + ids + ")";
    }


    public String selectPermitsByUserId(String userId) {

        return "SELECT\n" +
                "\tpt.id id,\n" +
                "\tpt.name name,\n" +
                "\tpt.path path,\n" +
                "\tpt.parent_id parent_id\n" +
                "FROM\n" +
                "\tt_permit pt,\n" +
                "\tt_system_user usr,\n" +
                "\tt_role_user tru,\n" +
                "\tt_permit_role pr\n" +
                "WHERE\n" +
                "\tpt.id = pr.permit_id\n" +
                "AND usr.id = tru.user_id\n" +
                "AND pr.role_id = tru.role_id " +
                " and pt.is_leaf=1   and  pt.enable=1  and usr.enable =1 " +
                "and usr.id=#{userId}\n";
    }


    public String selectPermitsByUserIdNoTree(String userId) {

        return "";
    }

    public String selectPermitsByRoleId(String roleId) {

        return "SELECT\n" +
                "\tpt.id id,\n" +
                "\tpt.name name,\n" +
                "\tpt.path path,\n" +
                "\tpt.parent_id parent_id\n" +
                "FROM\n" +
                "\tt_permit pt,\n" +
                "\tt_system_user usr,\n" +
                "\tt_role_user tru,\n" +
                "\tt_permit_role pr\n" +
                "WHERE\n" +
                "\tpt.id = pr.permit_id\n" +
                "AND usr.id = tru.user_id\n" +
                "AND pr.role_id = tru.role_id " +
                " and pt.is_leaf=1   and  pt.enable=1  and usr.enable =1 " +
                "and tru.role_id=#{roleId}\n";
    }



    public String selectPermitsByRoleIdNoTree(String roleId) {

        return " select  p.name ,p.id,p.code  from t_role role ,t_permit_role pr ,t_permit p  where role.id =pr.role_id   and pr.permit_id=p.id and   p.`enable`=1 " +
                "and role.id=#{roleId} ";
    }

}

