package com.zhoukaifan.ddns.domain;

import lombok.Data;

@Data
public class DNSHeader {
    private int id;
//    private boolean qr;
//    private int opCode;
//    private boolean aa;
//    private boolean tc;
//    private boolean rd;
//    private boolean ra;
//    private int rCode;
    private int flag;
    private int qdCount;
    private int anCount;
    private int nsCount;
    private int arCount;

    public boolean getQr(){
        return (flag&0x8000)>0;
    }
    public void setQr(boolean data){
        if (data){
            flag = flag|0x8000;
        }else {
            flag = flag&~0x8000;
        }
    }
    public boolean getAa(){
        return (flag&0x400)>0;
    }
    public void setAa(boolean data){
        if (data){
            flag = flag|0x400;
        }else {
            flag = flag&~0x400;
        }
    }
    public boolean getTc(){
        return (flag&0x200)>0;
    }
    public void setTc(boolean data){
        if (data){
            flag = flag|0x200;
        }else {
            flag = flag&~0x200;
        }
    }
    public boolean getRd(){
        return (flag&0x100)>0;
    }
    public void setRd(boolean data){
        if (data){
            flag = flag|0x100;
        }else {
            flag = flag&~0x100;
        }
    }
    public boolean getRa(){
        return (flag&0x100)>0;
    }
    public void setRa(boolean data){
        if (data){
            flag = flag|0x80;
        }else {
            flag = flag&~0x80;
        }
    }
    public int getOpcode(){
        return (flag&0x7800)>>11;
    }
    public void setOpcode(int data){
        if (data > 15) {
            throw new RuntimeException("超过最大值");
        }
        data = data<<11;
        flag = flag&~0x7800;
        flag = flag|data;
    }

    public int getRcode(){
        return (flag&0xf)>>11;
    }
    public void setRcode(int data){
        if (data > 15) {
            throw new RuntimeException("超过最大值");
        }
        flag = flag&~0xf;
        flag = flag|data;
    }
}
