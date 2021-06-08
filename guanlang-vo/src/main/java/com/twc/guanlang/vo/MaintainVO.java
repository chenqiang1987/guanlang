package com.twc.guanlang.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * 菜单
 */

@Data
public class MaintainVO {


    Integer id;
    String type;
    String repairName;
    String exeName;
    String status;
    String x;
    String y;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    String start;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    String end;
    String content;

    String imgsUrl;
    String vedioUrl;

    String result;


}
