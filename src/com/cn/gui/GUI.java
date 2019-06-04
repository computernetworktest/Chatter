package com.cn.gui;

import java.awt.EventQueue;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.cn.utils.ChatterUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class GUI {
	//123456
	/**
	 * 组件
	 */
	private JFrame frame;
	private JTextField tfLocalIP;
	private JLabel lblLocalIP;
	private JTextField tfTargetIP;
	private JLabel lblTargetIP;
	private JButton btnConnect;
	private JButton btnSend;
	private JTextArea txtChatArea;
	private JScrollPane scrollPaneChat;
	private JTextArea txtMsg;
	private JScrollPane scrollPaneMsg;
	/**
	 * 网络工具
	 */
	private ServerSocket server;
	private Socket connector;
	private String localIP;
	private String targetIP;
	private boolean isConnected;
	private boolean isConnecting;
	private DataOutputStream outputStream;
	private DataInputStream dataInputStream;
	private Socket receiver;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		/**
		 * 初始化界面
		 */
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("聊天工具");
		frame.setBounds(100, 100, 435, 558);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		/**
		 * 本机IP地址
		 */
		tfLocalIP = new JTextField();
		tfLocalIP.setEditable(false);
		tfLocalIP.setBounds(98, 13, 143, 24);
		frame.getContentPane().add(tfLocalIP);
		localIP = ChatterUtils.getLocalIP();
		tfLocalIP.setText(localIP);

		lblLocalIP = new JLabel("本机IP:");
		lblLocalIP.setBounds(14, 16, 72, 18);
		frame.getContentPane().add(lblLocalIP);

		/**
		 * 目标IP
		 */
		tfTargetIP = new JTextField();
		tfTargetIP.setBounds(98, 50, 143, 24);
		frame.getContentPane().add(tfTargetIP);
		tfTargetIP.setColumns(10);

		lblTargetIP = new JLabel("目标IP:");
		lblTargetIP.setBounds(14, 53, 72, 18);
		frame.getContentPane().add(lblTargetIP);

		/**
		 * 连接目标主机
		 */
		btnConnect = new JButton("连接");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetIP = tfTargetIP.getText();
				connect();
			}
		});
		btnConnect.setBounds(255, 49, 113, 27);
		frame.getContentPane().add(btnConnect);

		scrollPaneMsg = new JScrollPane();
		scrollPaneMsg.setBounds(14, 369, 388, 70);
		frame.getContentPane().add(scrollPaneMsg);

		txtMsg = new JTextArea();
		txtMsg.setLineWrap(true);
		scrollPaneMsg.setViewportView(txtMsg);

		btnSend = new JButton("发送");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = txtMsg.getText();
				SendMessage(msg);
			}
		});
		btnSend.setBounds(289, 471, 113, 27);
		frame.getContentPane().add(btnSend);

		scrollPaneChat = new JScrollPane();
		scrollPaneChat.setBounds(14, 84, 388, 272);
		frame.getContentPane().add(scrollPaneChat);

		txtChatArea = new JTextArea();
		txtChatArea.setEditable(false);
		scrollPaneChat.setViewportView(txtChatArea);
		txtChatArea.setLineWrap(true);

//		JButton btnTest = new JButton("test");
//		btnTest.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				addMessage(tfTargetIP.getText(), txtMsg.getText());
//			}
//		});
//		btnTest.setBounds(162, 471, 113, 27);
//		frame.getContentPane().add(btnTest);
		/**
		 * 初始化连接状态
		 */
		isConnected = false;
		isConnecting = false;
		createServer();
	}

	/**
	 * 连接目标主机
	 */
	private void connect() {
		try {
			if (!targetIP.equals("")) {
				btnConnect.setEnabled(false);
				btnConnect.setText("连接中...");
				connector = new Socket(targetIP, 8888);
				outputStream = new DataOutputStream(connector.getOutputStream());
				isConnecting = true;
//				outputStream.flush();
//				System.out.println("-------连接成功----------");
			} else {
				System.out.println("-----地址不能为空-----");
				JOptionPane.showMessageDialog(frame, "目标IP不能为空");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("找不到服务器");
			btnConnect.setEnabled(true);
			JOptionPane.showMessageDialog(frame, "找不到服务器");
			isConnected = false;
		}
	}

	/**
	 * 
	 */

	private void createServer() {
		new Thread(() -> {
			while (true) {
				if (!isConnected) {
					try {
						server = new ServerSocket(8888);
						System.out.println("----等待连接----");
						receiver = server.accept();
//						System.out.println("获取许可");
						
						//建瓯阿瑟东
						targetIP = receiver.getInetAddress().getHostAddress();
//					System.out.println(receiver.getInetAddress().getHostAddress());

						dataInputStream = new DataInputStream(receiver.getInputStream());
						
						if(!isConnecting) {
							System.out.println("获取许可...");
							connect();
							int decision = JOptionPane.showConfirmDialog(frame, targetIP + "请求连接");
	//						System.out.println(decision);
							if (decision != 0) {
//								System.out.println("-----拒绝访问-----");
								outputStream.writeUTF("REFUSE");
								outputStream.flush();
//								Thread.sleep(300);
								throw new IOException("拒绝对方访问");
							}
							else {
								outputStream.writeUTF("ACCEPT");
								outputStream.flush();
							}
	//						connect();
							isConnected = true;
							System.out.println("----连接成功----");
							btnConnect.setText("已连接");
							JOptionPane.showMessageDialog(frame, "连接成功");
						}
						else {
							System.out.println("------获取许可----");
							String acc = dataInputStream.readUTF();
							System.out.println(acc);
							if(acc.equals("REFUSE")) {
								System.out.println("拒绝访问");
								JOptionPane.showMessageDialog(frame, "拒绝访问");
								throw new IOException();
							}
							isConnected = true;
						}
						tfTargetIP.setText(targetIP);
						// 客户端已连接

						while (true) {
							String data = dataInputStream.readUTF();
							System.out.println(data);
							addMessage(targetIP, data);
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
//					e.printStackTrace();
						// 服务器创建失败
						System.out.println("----服务创建失败------");
						ChatterUtils.close(dataInputStream, receiver, server);
						// 重新创建服务
						isConnecting = false;
						isConnected = false;
						btnConnect.setEnabled(true);
//					createServer();
//					System.out.println(Thread.interrupted());
					}
				}
			}

		}).start();
	}

	/**
	 * 发送消息
	 * 
	 * @param msg the message will be sent
	 */
	private void SendMessage(String msg) {
		if (isConnected) {
			try {
				outputStream.writeUTF(msg);
				outputStream.flush();
				addMessage(localIP, msg);
				txtMsg.setText("");
			} catch (IOException e) {
//				e.printStackTrace();
				if (connector.isConnected())
					System.out.println("-----连接已断开---------");
//				ChatterUtils.close(outputStream, connector, dataInputStream, receiver, server);
				isConnected = false;
				btnConnect.setEnabled(true);
//				System.out.println("-----发送失败---------");
			}

		} else {
			System.out.println("-----尚未连接-----");
			JOptionPane.showMessageDialog(frame, "尚未连接");
		}
	}

	private void addMessage(String host, String msg) {
		String date = new Date().toString();
//		txtChatArea.insert(host+" "+date+":\n"+msg+"\n", txtChatArea.getRows());
		txtChatArea.append(host + " " + date + ":\r\n" + msg + "\r\n\r\n");
	}
}
