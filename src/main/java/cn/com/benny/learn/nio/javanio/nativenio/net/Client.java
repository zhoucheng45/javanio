package cn.com.benny.learn.nio.javanio.nativenio.net;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Client {

    public static void main(String[] args) throws Exception{
        
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("192.168.1.68", 80));
        
        Selector selector = Selector.open();
        SelectionKey selectionKey= socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int interestOps = selectionKey.interestOps();
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            ByteBuffer allocate = ByteBuffer.allocate(48); 
            
            if((interestOps & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT){
                System.out.println("OP_CONNECT");
            }else if((interestOps & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
                System.out.println("OP_ACCEPT");
            }else if((interestOps & SelectionKey.OP_READ) == SelectionKey.OP_READ){
                System.out.println("OP_READ");
                
                
                
            }else if((interestOps & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE){
                System.out.println("OP_WRITE");
            }
            
        }
    }
}
