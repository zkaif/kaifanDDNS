package com.zhoukaifan.ddns.service;

import com.zhoukaifan.ddns.domain.DNSHeader;
import com.zhoukaifan.ddns.domain.DNSMessage;
import com.zhoukaifan.ddns.domain.DNSQuestion;
import com.zhoukaifan.ddns.domain.DNSResourceRecords;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * 目前只实现A记录 其他先不管了
 */
@Service
public class DNSResourceRecordsService {
    public Map<String, DNSResourceRecords> domainNameAndRRMap = new ConcurrentHashMap<>();

    public DNSMessage handle(DNSMessage dnsMessage) {
        DNSHeader header = dnsMessage.getHeader();
        List<DNSQuestion> question = dnsMessage.getQuestion();
        dnsMessage.setAnswer(new ArrayList<>());
        if (!header.getQr()&&header.getOpcode()==0&&!header.getTc()){
            header.setQr(true);
            header.setAa(true);
            header.setRa(false);
            header.setRcode(0);
            for (DNSQuestion dnsQuestion:question){
                if (dnsQuestion.getType()==1){
                    String name = dnsQuestion.getName();
                    DNSResourceRecords resourceRecords = domainNameAndRRMap.get(name);
                    if (resourceRecords!=null){
                        dnsMessage.getAnswer().add(resourceRecords);
                    }
                }
            }
            if (dnsMessage.getAnswer().isEmpty()){
                header.setRcode(3);
            }
            header.setAnCount(dnsMessage.getAnswer().size());
        }
        return dnsMessage;
    }

    public void saveAndUpdate(DNSResourceRecords dnsResourceRecords) {
        domainNameAndRRMap.put(dnsResourceRecords.getName(),dnsResourceRecords);
    }
}
