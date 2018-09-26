package com.example.demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

public class CppServer {
	
	public DataInputStream dis;
	public DataOutputStream dos;
	private ServerSocket server;
	private TcpServer tcpServer;
	private Connection conn;
	private PreparedStatement pstmt;
	public CppServer(TcpServer tcpServer) {
		this.tcpServer = tcpServer;
		
	try {
		//port 8086
		server = new ServerSocket(8086);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("Initialize complate");

	Thread t = new Thread() {
		public void run() {
			while (true) {
				try {
					Socket client = server.accept();
					System.out.println("cppServer client come ok");
					// BufferedOutputStream bos = new
					// BufferedOutputStream(client.getOutputStream());
					// out = new PrintWriter(new BufferedWriter(new
					// OutputStreamWriter(client.getOutputStream())), true);
					// out.flush();
					dos = new DataOutputStream(client.getOutputStream());
					dis = new DataInputStream(client.getInputStream());
					new pushThread(dis, tcpServer);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("cppServer Connection");
			}
		}
	};

	t.start();
}
	
	
	class pushThread extends Thread{
		
		DataInputStream dis = null;
		TcpServer tcpServer = null;
		byte [] b1 = new byte[4];
		byte [] b2 = new byte[20];
		public pushThread(DataInputStream dis, TcpServer tcpServer) {
			this.dis = dis;
			this.tcpServer = tcpServer;					
			this.start();
			System.out.println("pushThread create ok!");
		}
		
		public void run() {
			while(true) {
				try {
					dis.read(b1, 0, 4);
					String data = new String(b1);
					System.out.println("Read Line Data = " + data);
					String filename;
					if(data.equals("push")) {
						System.out.println("call push at cpp file");
					dis.read(b2);
					filename = new String(b2); 
						//DB insert
						conn = null;
						pstmt = null;
						String jdbc_driver = "com.mysql.jdbc.Driver";
						String jdbc_url = "jdbc:mysql://localhost:3306/video"; 
						try {
							Class.forName(jdbc_driver);
							conn = DriverManager.getConnection(jdbc_url,"root","1234");
						} catch (Exception e) {
							e.printStackTrace();
						}
						String sql ="insert into list(date) values(?)";
						try {
							pstmt =  conn.prepareStatement(sql);
							pstmt.setString(1,filename);
							//pstmt.setString(2, "11115396");
							pstmt.executeUpdate();
							System.out.println("Mysql insert OK");
						} catch (SQLException e) {
							e.printStackTrace();
						}
						finally {
							disconnect();
						}
						//tcpServer's push
						tcpServer.push();
						System.out.println("cppServer -> tcpServer.push() OK");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	void disconnect() {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void pushThreadStart() {
		//new pushThread(dis, tcpServer);
	}
}

