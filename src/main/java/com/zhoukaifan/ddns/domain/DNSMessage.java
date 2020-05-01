package com.zhoukaifan.ddns.domain;

import java.util.List;
import lombok.Data;

@Data
public class DNSMessage{
    private DNSHeader header;
    private List<DNSQuestion> question;
    private List<DNSResourceRecords> answer;
    private List<DNSResourceRecords> authority;
    private List<DNSResourceRecords> additional;

}
