package com.twc.guanlang.common;

import tk.mybatis.mapper.entity.Example;

import java.util.Map;

public class MybatisExampleUtil {

    public static Example setExampleForObject(Class T, Map condition) {

        Example example = new Example(T);
        Example.Criteria criteria = example.createCriteria();
        condition.forEach((k, v) -> criteria.andEqualTo(k + "", v));

        return example;
    }


}
