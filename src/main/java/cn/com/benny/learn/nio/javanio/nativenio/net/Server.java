package cn.com.benny.learn.nio.javanio.nativenio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Server {
    // 定义实现编码、解码的字符集对象
    private static Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) throws Exception {
        
        // 1、打开channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(80));

        // 打开选择器
        Selector selector = Selector.open();
        
        // channel注册到选择器上 
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (selector.select() > 0) {

            // 依次处理selector上的每个已选择的SelectionKey
            for (SelectionKey sk : selector.selectedKeys()) {
                // 从selector上的已选择Key集中删除正在处理的SelectionKey
                selector.selectedKeys().remove(sk);
                // 如果sk对应的通信包含客户端的连接请求
                if (sk.isAcceptable()) {
                    // 调用accept方法接受连接，产生服务器端对应的SocketChannel
                    SocketChannel sc = serverSocketChannel.accept();
                    // 设置采用非阻塞模式
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                    // 将sk对应的Channel设置成准备接受其他请求
                    sk.interestOps(SelectionKey.OP_ACCEPT);
                    System.out.println("server:OP_ACCEPT");
                }
                // 如果sk对应的通道有数据需要读取
                if (sk.isReadable()) {
                    // 获取该SelectionKey对应的Channel，该Channel中有可读的数据
                    SocketChannel sc = (SocketChannel) sk.channel();
                    // 定义准备之星读取数据的ByteBuffer
                    ByteBuffer buff = ByteBuffer.allocate(2048);
                    String content = "";
                    // 开始读取数据
                    try {
                        while (sc.read(buff) > 0) {
                            buff.flip();
                            content += charset.decode(buff);
                        }
                        // 打印从该sk对应的Channel里读到的数据
                        System.out.println("==========" + content);
                        // 将sk对应的Channel设置成准备下一次读取
                        sk.interestOps(SelectionKey.OP_READ);
                        // 如果捕捉到该sk对应的channel出现异常，即表明该channel对应的client出现了
                        // 异常，所以从selector中取消sk的注册
                    } catch (IOException e) {
                        // 从Selector中删除指定的SelectionKey
                        sk.cancel();
                        if (sk.channel() != null) {
                            sk.channel().close();
                        }
                    }
                    // 如果content的长度大于0，即聊天信息不为空
                    if (content.length() > 0) {
                        // 遍历该selector里注册的所有SelectKey
                        for (SelectionKey key : selector.keys()) {
                            // 选取该key对应的Channel
                            Channel targetChannel = key.channel();
                            // 如果该channel是SocketChannel对象
                            if (targetChannel instanceof SocketChannel) {
                                // 将独到的内容写入该Channel中
                                SocketChannel dest = (SocketChannel) targetChannel;
                                dest.write(charset.encode(content));
                            }
                        }
                    }
                }
            }

        }
    }
}
