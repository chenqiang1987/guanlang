package com.twc.guanlang.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 *
 * @MapperScan(value = "com.twc.guanlang.mapper.entity")
 *
 *
 * TwcBaseMapper需要放在不能被mybatis扫描的路径下
 * TwcBaseMapper<T> </T>由于用的泛型T，会报异常
 * sun.reflect.generics.reflectiveObjects.TypeVariableImpl cannot be cast to java.lang.Class
 */
public interface TwcBaseMapper<T> extends Mapper<T>, MySqlMapper<T> {


}
