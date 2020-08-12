package com.sanelee.zhiyuan.model;

import lombok.Data;

@Data
public class GaoKaoQuestion {
    private Integer id;
    private String question;
    private String answer;
    private String type;
}
