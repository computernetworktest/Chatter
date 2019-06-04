package com.cn.utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 计网编程工具包
 * @author Sakur
 *
 */

public class ChatterUtils {

	
	/**
	 * 释放资源
	 * @param targets
	 */
    public static void close(Closeable... targets){
        for(Closeable target : targets){
            try {
                target.close();

            }catch (IOException e) {
//                e.printStackTrace();
                System.out.println("------关闭出错-------");
            }

        }
    }
    
    /**
     *  返回本机IP地址
     * @return
     */
    public static String getLocalIP() {
    	try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("-------获取IP失败--------");
		}
    	return "获取IP失败";
    }
}
