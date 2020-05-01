package com.zhoukaifan.ddns.network;

import com.zhoukaifan.ddns.domain.DNSMessage;
import com.zhoukaifan.ddns.domain.utils.DNSMsgDecode;
import com.zhoukaifan.ddns.domain.utils.DNSMsgEncode;
import com.zhoukaifan.ddns.service.DNSResourceRecordsService;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NetWorkController implements Runnable {
    private Selector selector;
    private Thread thread;

    @Autowired
    private DNSResourceRecordsService dnsResourceRecordsService;

    @PostConstruct
    public void init() throws IOException {
        Selector selector = Selector.open();
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(53));
        channel.configureBlocking(false);
        channel.register (selector, SelectionKey.OP_READ);
        this.selector = selector;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                int count = selector.select();
                if (count > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isReadable()) {
                            SelectableChannel channel = selectionKey.channel();
                            if (channel instanceof DatagramChannel){
                                DatagramChannel datagramChannel = (DatagramChannel) channel;
                                while (true) {
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    SocketAddress receive = datagramChannel.receive(byteBuffer);
                                    if (receive==null){
                                        break;
                                    }
                                    byteBuffer.flip();
                                    DNSMessage dnsMessage = DNSMsgDecode.decode(byteBuffer);
                                    DNSMessage messageRespones = dnsResourceRecordsService.handle(dnsMessage);
                                    byteBuffer.clear();
                                    DNSMsgEncode.encode(messageRespones,byteBuffer);
                                    byteBuffer.flip();
                                    datagramChannel.send(byteBuffer, receive);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
