package cn.com.benny.learn.nio.javanio.nativenio;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class MainClass {

    static String pathname = "C:\\adcfg.json";

    public static void main(String[] args) throws Exception {
        File file = new File(pathname);
        // FileInputStream fileInputStream = new FileInputStream(file);
        // FileChannel inChannel = fileInputStream.getChannel();
        // ByteBuffer allocate = ByteBuffer.allocate(1024);
        // int read = inChannel.read(allocate);

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        byte[] b = new byte[128];
        int read2 = fileChannel.read(buf);
        while (-1 != read2) {
            System.out.println("Read " + read2);
            
            int position = buf.position();
            int limit = buf.limit();
            System.out.println("position = " + position);
            System.out.println("limit = " + limit);
            
            System.out.println();
            
            buf.flip();
            System.out.println("buf.flip();");
            position = buf.position();
            limit = buf.limit();
            System.out.println("position = " + position);
            System.out.println("limit = " + limit);
            System.out.println();
            
            buf.get(b, 0, 10);
            System.out.println("buf.get(b, 0, 10);" );
            position = buf.position();
            limit = buf.limit();
            System.out.println("position = " + position);
            System.out.println("limit = " + limit);
            System.out.println();
            
            
            buf.compact();
            System.out.println("buf.compact();");
            position = buf.position();
            limit = buf.limit();
            System.out.println("position = " + position);
            System.out.println("limit = " + limit);
            System.out.println();
            
             while (buf.hasRemaining()) {
            
             System.out.print((char)buf.get());
             }
             System.out.println();
            
            buf.clear();
            read2 = fileChannel.read(buf);
        }

        randomAccessFile.close();
    }
}
