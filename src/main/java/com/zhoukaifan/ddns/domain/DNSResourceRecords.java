package com.zhoukaifan.ddns.domain;

import lombok.Data;

@Data
public class DNSResourceRecords {
    private String name;
    private int type;
    private int clazz;
    private int ttl;
    private int rdLength;
    private String rData;

}
