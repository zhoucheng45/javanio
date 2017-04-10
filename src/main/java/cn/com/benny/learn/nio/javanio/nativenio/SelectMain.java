package cn.com.benny.learn.nio.javanio.nativenio;

import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SelectMain {

    
    public static void main(String[] args) throws Exception {
        
        //Selector的创建
        Socket socket = new Socket("192.168.1.68", 80);
        SocketChannel channel = socket.getChannel();
        channel.configureBlocking(false);   // 配置channel为非阻塞的
        
        Selector selector = Selector.open();
        
        SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);   // 将channel注册到selector上
        
        int interestOps = selectionKey.interestOps();
        
        int readyOps = selectionKey.readyOps();
        
    }
}
