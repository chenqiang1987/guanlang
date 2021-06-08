package com.twc.guanlang.common.annotation;


import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;
import net.sf.json.JSONObject;
import tk.mybatis.mapper.util.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


public class TwcAnnotationParser {

    public static void parseParamCheck(Object registerParam) throws CustomException {

        JSONObject jsonObject = JSONObject.fromObject(registerParam);
        //获取类的域
        Field[] fieldArr = registerParam.getClass().getDeclaredFields();
        for (Field field : fieldArr) {
            //获取类的域注解数组
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {

                if (annotation instanceof TwcParamCheck) {
                    TwcParamCheck twcParamCheck = ((TwcParamCheck) annotation);

                    String value = jsonObject.get(field.getName()).toString();

                    //第一道检测,经过第一道检测，所有的参数还会进入第二道检测，第一道和第二道顺序不能弄反
                    if (StringUtil.isNotEmpty(value)) {

                        //长度区间检测
                        if (value.length() < twcParamCheck.length_min() || value.length() > twcParamCheck.length_max()) {
                            String msg = ApiResultBean.CODE1.PARAM_LENGTH.getMsg();
                            msg = msg.replace("start", twcParamCheck.length_min() + "");
                            msg = msg.replace("end", twcParamCheck.length_max() + "");
                            throw new CustomException(ApiResultBean.CODE1.PARAM_LENGTH.getCode(), field.getName() + ":" + msg);
                            //throw new CustomException(field.getName() + ":" + msg);
                        }

                    }

                    //第二道检测
                    if (twcParamCheck.required()) {
                        //空字符换
                        if (StringUtil.isEmpty(value))
                            throw new CustomException(ApiResultBean.CODE1.PARAM_NULL.getCode(), field.getName() + ":" + ApiResultBean.CODE1.PARAM_NULL.getMsg());
                            //空数组
                        else if (value.equals("[]"))
                            throw new CustomException(ApiResultBean.CODE1.PARAM_NULL.getCode(), field.getName() + ":" + ApiResultBean.CODE1.PARAM_NULL.getMsg());
                    }
                }
            }
        }
    }

    public static void main(String s[]) throws Exception {


    }


}
