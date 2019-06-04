package com.cn.test;

import com.cn.utils.ChatterUtils;

import java.io.*;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws Exception {

        System.out.println("---------client--------");

        //建立连接
        Socket client = new Socket("192.168.160.130", 8888);

        //输入
        DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
        String data = new BufferedReader(new InputStreamReader(System.in)).readLine();

        dataOutputStream.writeUTF(data);
        dataOutputStream.flush();

        ChatterUtils.close(dataOutputStream, client);
    }
}
