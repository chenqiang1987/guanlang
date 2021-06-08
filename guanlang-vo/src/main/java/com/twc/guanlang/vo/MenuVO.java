package com.twc.guanlang.vo;


import lombok.Data;

import java.util.List;

/**
 * 菜单
 */

@Data
public class MenuVO extends  BaseVO {



    public int parentId;

    public String path;

    public List<MenuVO> childs;


}
