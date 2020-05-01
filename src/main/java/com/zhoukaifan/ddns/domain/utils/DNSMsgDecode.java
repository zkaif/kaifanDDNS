package com.zhoukaifan.ddns.domain.utils;

import com.zhoukaifan.ddns.domain.DNSHeader;
import com.zhoukaifan.ddns.domain.DNSMessage;
import com.zhoukaifan.ddns.domain.DNSQuestion;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DNSMsgDecode {

    public static DNSMessage decode(ByteBuffer byteBuffer) {
        DNSMessage dnsMessage = new DNSMessage();
        DNSHeader dnsHeader = new DNSHeader();
        dnsMessage.setHeader(dnsHeader);
        dnsHeader.setId(byteBuffer.getShort());
        dnsHeader.setFlag(byteBuffer.getShort());
        dnsHeader.setQdCount(byteBuffer.getShort());
        dnsHeader.setAnCount(byteBuffer.getShort());
        dnsHeader.setNsCount(byteBuffer.getShort());
        dnsHeader.setArCount(byteBuffer.getShort());

        List<DNSQuestion> questions = decodeDNSQuestion(byteBuffer, dnsHeader.getQdCount());
        dnsMessage.setQuestion(questions);

        return dnsMessage;
    }

    public static List<DNSQuestion> decodeDNSQuestion(ByteBuffer byteBuffer, int count) {
        List<DNSQuestion> questions = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            DNSQuestion dnsQuestion = new DNSQuestion();
            questions.add(dnsQuestion);
            dnsQuestion.setName(decodeDNSName(byteBuffer));
            dnsQuestion.setType(byteBuffer.getShort());
            dnsQuestion.setClazz(byteBuffer.getShort());
        }
        return questions;
    }

    public static String decodeDNSName(ByteBuffer byteBuffer) {
        String domainName = "";
        String s;
        do {
            s = decodeDNSNameStr(byteBuffer, byteBuffer.get());
            if (s == null){
                break;
            }
            domainName = domainName + s + ".";
        } while (true);
        return domainName;
    }

    public static String decodeDNSNameStr(ByteBuffer byteBuffer, byte b) {
        if (b <= 0) {
            return null;
        }
        byte[] bytes = new byte[b];
        byteBuffer.get(bytes);
        return new String(bytes);
    }
}
