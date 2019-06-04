package com.cn.test;

import java.io.IOException;

import javax.swing.JOptionPane;

public class GUITest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i = JOptionPane.showConfirmDialog(null, 2233333);
		System.out.println(i);
		if(i != 0) {
			try {
				throw new IOException();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
