import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPTest {

    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(53));

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while(true) {
            byteBuffer.clear();
            channel.receive(byteBuffer);
            byteBuffer.flip();
            System.out.println(byteBuffer);
        }
    }
}
