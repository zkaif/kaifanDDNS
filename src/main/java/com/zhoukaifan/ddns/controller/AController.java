package com.zhoukaifan.ddns.controller;


import com.zhoukaifan.ddns.domain.DNSResourceRecords;
import com.zhoukaifan.ddns.service.DNSResourceRecordsService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AController {

    @Autowired
    private DNSResourceRecordsService dnsResourceRecordsService;

    @RequestMapping("uploadA/{domainName}")
    public String uploadA(
            @PathVariable("domainName") String domainName,
            @RequestParam("ttl") Integer ttl,
            HttpServletRequest request){
        String remoteAddress = request.getRemoteAddr();
        DNSResourceRecords resourceRecords = new DNSResourceRecords();
        resourceRecords.setName(domainName);
        resourceRecords.setType(1);
        resourceRecords.setClazz(1);
        resourceRecords.setTtl(ttl);
        resourceRecords.setRData(remoteAddress);
        resourceRecords.setRdLength(4);
        dnsResourceRecordsService.saveAndUpdate(resourceRecords);
        return "ok";
    }
}
