package com.twc.guanlang.version;


import lombok.Data;

import java.util.Date;

/**
 * 系统版本控制
 */
@Data
public class Version {

    private String version;

    private String release;

    private Date publishDate;

}
