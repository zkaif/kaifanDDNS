package com.zhoukaifan.ddns.domain.utils;

import com.zhoukaifan.ddns.domain.DNSHeader;
import com.zhoukaifan.ddns.domain.DNSMessage;
import com.zhoukaifan.ddns.domain.DNSQuestion;
import com.zhoukaifan.ddns.domain.DNSResourceRecords;
import java.nio.ByteBuffer;
import java.util.List;

public class DNSMsgEncode {

    public static void encode(DNSMessage dnsMessage, ByteBuffer byteBuffer) {
        DNSHeader header = dnsMessage.getHeader();
        List<DNSQuestion> question = dnsMessage.getQuestion();
        List<DNSResourceRecords> answer = dnsMessage.getAnswer();
        byteBuffer.putShort((short) header.getId());
        byteBuffer.putShort((short) header.getFlag());
        byteBuffer.putShort((short) header.getQdCount());
        byteBuffer.putShort((short) header.getAnCount());
        byteBuffer.putShort((short) header.getNsCount());
        byteBuffer.putShort((short) header.getArCount());

        for (DNSQuestion dnsQuestion : question) {
            encodeDNSQuestion(dnsQuestion, byteBuffer);
        }
        for (DNSResourceRecords resourceRecords : answer) {
            encodeDNSResourceRecords(resourceRecords, byteBuffer);
        }
    }

    public static void encodeDNSResourceRecords(DNSResourceRecords dnsResourceRecords,
            ByteBuffer byteBuffer) {
        byteBuffer.put(encodeDNSName(dnsResourceRecords.getName()));
        byteBuffer.putShort((short) dnsResourceRecords.getType());
        byteBuffer.putShort((short) dnsResourceRecords.getClazz());
        byteBuffer.putInt(dnsResourceRecords.getTtl());
        byteBuffer.putShort((short) dnsResourceRecords.getRdLength());
        if (dnsResourceRecords.getType() == 1) {
            byteBuffer.put(encodeDNSIP(dnsResourceRecords.getRData()));
        }
        if (dnsResourceRecords.getType() == 5) {
            byteBuffer.put(encodeDNSName(dnsResourceRecords.getRData()));
        }

    }

    public static void encodeDNSQuestion(DNSQuestion dnsQuestion, ByteBuffer byteBuffer) {
        byteBuffer.put(encodeDNSName(dnsQuestion.getName()));
        byteBuffer.putShort((short) dnsQuestion.getType());
        byteBuffer.putShort((short) dnsQuestion.getClazz());
    }

    public static byte[] encodeDNSName(String name) {
        byte[] bytes = new byte[name.length() + 1];
        int byteIndex = 0;
        String[] names = name.split("\\.");
        for (String s : names) {
            bytes[byteIndex] = (byte) s.length();
            byteIndex++;
            System.arraycopy(s.getBytes(), 0, bytes, byteIndex, s.length());
            byteIndex+=s.length();
        }
        return bytes;
    }

    public static byte[] encodeDNSIP(String ip) {
        byte[] bytes = new byte[4];
        String[] ips = ip.split("\\.");
        for (int i = 0; i < 4; ++i) {
            byte aByte = Integer.valueOf(ips[i]).byteValue();
            bytes[i] = aByte;
        }
        return bytes;
    }
}
