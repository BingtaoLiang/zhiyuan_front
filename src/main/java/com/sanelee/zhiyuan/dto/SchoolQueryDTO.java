package com.sanelee.zhiyuan.dto;

import lombok.Data;

@Data
public class SchoolQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
    private Integer areaid;
    private Integer rank;
    private String type;
    private Integer is985;
    private Integer is211;
    private Integer isdoublefirstclass;
}
