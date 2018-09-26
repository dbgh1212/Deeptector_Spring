package com.example.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
  // public int c = 0;
   public File file;
   public boolean flag = false;
   public boolean push = false;
   
   
   public int server_port = 3003;
   public ServerSocket server;
   public Socket socket;
   public OutputStream os;
   public DataOutputStream dos;
   public InputStream is;
   public DataInputStream dis;
   
   
   public int push_server_port = 3004;
   public ServerSocket push_server;
   public Socket push_socket;
   public OutputStream push_os;
   public DataOutputStream push_dos;
   
   public FileInputStream fin;
   public long filelength;
   
   
   public PrintWriter out;
   private Thread sendThread;

   public TcpServer() {

      try {
         server = new ServerSocket(server_port);
         push_server = new ServerSocket(push_server_port);
         
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      System.out.println("Initialize complate");

      Thread t = new Thread() {
         
         public void run() {
            while (true) {
               try {
                  socket = server.accept();
                  System.out.println("sever accept");
                  push_socket = push_server.accept();
                  System.out.println("push_sever accept");
                  
                  //c++;
                  System.out.println("TcpServer client come ok");
               
                  dis = new DataInputStream(socket.getInputStream());
                  dos = new DataOutputStream(socket.getOutputStream());
                  push_dos = new DataOutputStream(push_socket.getOutputStream());
                  
               } catch (Exception e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
               }
               System.out.println("TcpServer Connection");
            }
         }
      };

      t.start();
   }
   

   
   class SendThread extends Thread {
      
      public SendThread() {
         System.out.println("Create Send Thread");
         this.start();
      }
      synchronized public void run() {
         try {
            dos.writeUTF("video");
            dos.writeInt(((int) file.length()));
            dos.writeUTF(file.getName());
            System.out.println(file.getName());
            fin = new FileInputStream(file);
            int byteSize = 1024;
            byte[] buffer = new byte[byteSize];
            int count = 0;
            int n = 0;
            int check = 0;
            System.out.println("Sender Start");
            while (count < filelength) {
               try {
                  n = fin.read(buffer);
                  dos.write(buffer);
                  count += n;
                  check++;
                  System.out.println("Sending n : " + n + " count : " +
                  count + " check : " + check);
               } catch (IOException e) {
               }
            }
            System.out.println("Sender Finish");
            dos.flush();
            this.interrupt();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            this.interrupt();
            e.printStackTrace();
         }
         this.interrupt();
      }
   }

   
   public void fileSend() {
      //sendThread = new SendThread();
      
      try {
         dos.writeUTF("video");
         dos.writeInt(((int) file.length()));
         dos.writeUTF(file.getName());
         System.out.println(file.getName());
         fin = new FileInputStream(file);
         int byteSize = 1024;
         byte[] buffer = new byte[byteSize];
         int count = 0;
         int n = 0;
         int check = 0;
         System.out.println("Sender Start");
         while (count < filelength) {
            try {
               n = fin.read(buffer);
               dos.write(buffer);
               count += n;
               check++;
               System.out.println("Sending n : " + n + " count : " + count + " check : " + check);
            } catch (Exception e) {
            	System.out.println("file_writing error");
               e.printStackTrace();
               return;
            }
         }
         
         System.out.println("Sender Finish : " + file.getName());
         
         String receive = dis.readUTF();
         System.out.println("Sender Finish_receive : " + receive);
         
         dos.flush();
      
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return ;
      }
   }
   
   public void push() {
      try {
         push_dos.writeUTF("push");
         System.out.println("tcpServer push send ok!");
         push_dos.flush();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return;
      }
   }

}