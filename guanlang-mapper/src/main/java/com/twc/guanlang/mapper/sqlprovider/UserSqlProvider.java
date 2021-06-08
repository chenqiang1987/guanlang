package com.twc.guanlang.mapper.sqlprovider;


import com.twc.guanlang.common.str.TwcStringUtil;
import com.twc.guanlang.param.user.UserPageParam;
import tk.mybatis.mapper.util.StringUtil;

public class UserSqlProvider {


    public String selectUserPage(UserPageParam userPageParam) {


        String sql= " select distinct user.id id, user.user_name user_name, user.tel_phone tel_phone ,user.nick_name nick_name ,user.enable `enable` " +
                "  , user.create_time create_time,user.update_time update_time  from t_system_user user , t_role_user role  where   user.id=role.user_id " +
                " and user.enable=1  " ;


//        if (TwcStringUtil.isNotEmpty(userPageParam.getTelPhone())) {
//
//            sql+=" and ( user.tel_phone like '%" + userPageParam.getTelPhone()+"%' ";
//        }
//        if (TwcStringUtil.isNotEmpty(userPageParam.getUserName())) {
//            sql+=" and user.user_name like '%" + userPageParam.getUserName()+"%' ";
//        }
//        if (TwcStringUtil.isNotEmpty(userPageParam.getNickName())) {
//            sql+=" and user.nick_name like '%" + userPageParam.getNickName()+"%' ";
//        }


        String username="";
        String nickname="";
        String tel="";
        if(StringUtil.isNotEmpty(userPageParam.getUserName())) tel=userPageParam.getUserName();
        if(StringUtil.isNotEmpty(userPageParam.getUserName())) nickname=userPageParam.getUserName();
        if(StringUtil.isNotEmpty(userPageParam.getUserName())) username=userPageParam.getUserName();
        sql+=" and ( user.tel_phone like '%" + tel+"%' "+
         "  or user.user_name like '%" + username+"%' " +
         " or user.nick_name like '%" + nickname+"%' ) ";

        if (TwcStringUtil.isNotEmpty(userPageParam.getRoleId())) {
            sql+=" and role.role_id= " + userPageParam.getRoleId()+" ";
        }



        return sql;
    }


}

