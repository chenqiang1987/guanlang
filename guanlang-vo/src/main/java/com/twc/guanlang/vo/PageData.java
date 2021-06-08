package com.twc.guanlang.vo;

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.List;

@Data

public class PageData {

    private long total;

    private int pageNum;

    private int pageSize;

    private List data;

    private int pages;

    public PageData(Page page) {
        this.total = page.getTotal();
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.data = page.getResult();
        this.pages = page.getPages();
    }
}



