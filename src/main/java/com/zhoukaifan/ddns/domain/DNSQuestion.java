package com.zhoukaifan.ddns.domain;

import lombok.Data;

@Data
public class DNSQuestion {
    private String name;
    private int type;
    private int clazz;

}
