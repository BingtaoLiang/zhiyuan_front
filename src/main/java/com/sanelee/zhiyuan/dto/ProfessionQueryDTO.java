package com.sanelee.zhiyuan.dto;

import lombok.Data;

@Data
public class ProfessionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
    private String major;
    private String subject;
}
