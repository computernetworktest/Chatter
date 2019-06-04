package com.cn.test;


import com.cn.utils.ChatterUtils;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
    public static void main(String[] args) throws Exception {
        System.out.println("-----------Server-------------");

        //创建服务器, 并指定端口
        ServerSocket serverSocket = new ServerSocket(8888);

        //等待连接
        Socket socket = serverSocket.accept();

        //获取输入
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        String data = dataInputStream.readUTF();
        System.out.println(data);

        //释放资源
        ChatterUtils.close(socket, dataInputStream, serverSocket);


    }
}
