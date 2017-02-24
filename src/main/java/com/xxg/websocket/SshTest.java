package com.xxg.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshTest {

	/**
	 * 利用JSch包实现远程主机SHELL命令执行
	 * @param ip 主机IP
	 * @param user 主机登陆用户名
	 * @param psw  主机登陆密码
	 * @param port 主机ssh2登陆端口，如果取默认值，传-1
	 * @param privateKey 密钥文件路径
	 * @param passphrase 密钥的密码
	 */
	public static void sshShell(String ip, String user, String psw
			,int port ,String privateKey ,String passphrase) throws Exception{
		Session session = null;
		ChannelShell channel = null;
		
		JSch jsch = new JSch();

		//设置密钥和密码
		if (privateKey != null && !"".equals(privateKey)) {
            if (passphrase != null && "".equals(passphrase)) {
            	//设置带口令的密钥
                jsch.addIdentity(privateKey, passphrase);
            } else {
            	//设置不带口令的密钥
                jsch.addIdentity(privateKey);
            }
        }
		
		if(port <=0){
			//连接服务器，采用默认端口
			session = jsch.getSession(user, ip);
		}else{
			//采用指定的端口连接服务器
			session = jsch.getSession(user, ip ,port);
		}

		//如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new Exception("session is null");
		}
		//设置登陆主机的密码
		session.setPassword(psw);//设置密码   
		//设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		//设置登陆超时时间   
		session.connect(30000);
			
		try {
			//创建sftp通信通道
			channel = (ChannelShell) session.openChannel("shell");
			channel.connect(1000);

			//获取输入流和输出流
			InputStream instream = channel.getInputStream();
			OutputStream outstream = channel.getOutputStream();
			
			//发送需要执行的SHELL命令，需要用\n结尾，表示回车
			String shellCommand = "ls \n";
			outstream.write(shellCommand.getBytes());
			outstream.flush();
			
	/*		PipedInputStream pipeIn = new PipedInputStream();  
			PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);  
			channel.setInputStream(pipeIn);  */
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream,"utf-8")); 
			String[] cmdArray = {"ls -ltr","whoami","date","pwd"};
			int i = 0;
			while(true){
				if (instream.available() > 0) {
					byte[] data = new byte[instream.available()];
					int nLen = instream.read(data);
					
					if (nLen < 0) {
						throw new Exception("network error.");
					}
					
					//转换输出结果并打印出来
					String temp = new String(data, 0, nLen);
					System.out.println(new String(temp.getBytes("utf-8"),"UTF-8"));
					 
				}else {
					if(i>cmdArray.length-1){
						break;
					}
				   /*pipeOut.write(cmdArray[i].getBytes());
					pipeOut.flush();
*/					outstream.write(cmdArray[i].getBytes());
					outstream.flush();
					i++;
//					reader = new BufferedReader(new InputStreamReader(instream,"utf-8"));
					instream = channel.getInputStream();
				}
				
            	/*String buf = reader.readLine();
            	if(buf!=null){
            		buf = new String(buf.getBytes("gbk"),"UTF-8");
                	System.out.println(buf);
            	}else {
            		Scanner scan = new Scanner(System.in);
					String cmd = scan.nextLine();
					outstream.write(cmd.getBytes());
					outstream.flush();
            	}*/
            }


			/*//获取命令执行的结果
			if (instream.available() > 0) {
				byte[] data = new byte[instream.available()];
				int nLen = instream.read(data);
				
				if (nLen < 0) {
					throw new Exception("network error.");
				}
				
				//转换输出结果并打印出来
				String temp = new String(data, 0, nLen);
				System.out.println(new String(temp.getBytes("gbk"),"UTF-8"));
				 
			}
		    outstream.close();
		    instream.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}
	
	  /**
	   * 远程 执行命令并返回结果调用过程 是同步的（执行完才会返回）
	   * @param host	主机名
	   * @param user	用户名
	   * @param psw	密码
	   * @param port	端口
	   * @param command	命令
	   * @return
	 * @throws Exception 
	   */
	  public static String exec(String host,String user,String psw,int port,String command) throws Exception{
	    String result="";
	    Session session =null;
	    ChannelExec openChannel =null;
	    try {
	      JSch jsch=new JSch();
	      session = jsch.getSession(user, host, port);
	      java.util.Properties config = new java.util.Properties();
	      config.put("StrictHostKeyChecking", "no");
	      session.setConfig(config);
	      session.setPassword(psw);
	      session.connect();
	      openChannel = (ChannelExec) session.openChannel("exec");
	      openChannel.setCommand(command);
	      int exitStatus = openChannel.getExitStatus();
	      System.out.println(exitStatus);
	      openChannel.connect();  
	      
	      
	     /* InputStream instream = openChannel.getInputStream();
		  OutputStream outstream = openChannel.getOutputStream();
		  
		//发送需要执行的SHELL命令，需要用\n结尾，表示回车
			String shellCommand = "ls \n";
			outstream.write(shellCommand.getBytes());
			outstream.flush();


			//获取命令执行的结果
			if (instream.available() > 0) {
				byte[] data = new byte[instream.available()];
				int nLen = instream.read(data);
				
				if (nLen < 0) {
					throw new Exception("network error.");
				}
				
				//转换输出结果并打印出来
				String temp = new String(data, 0, nLen);
				System.out.print(new String(temp.getBytes("gbk"),"UTF-8"));
				
				if(temp.endsWith("$ ")){
					Scanner scan = new Scanner(System.in);
					String cmd = scan.nextLine();
					
					outstream.write(cmd.getBytes());
					outstream.flush();

				}
				 
			}
		    outstream.close();
		    instream.close();*/

	      
            InputStream in = openChannel.getInputStream();  
            OutputStream outstream = openChannel.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
            while(true){
            	String buf = reader.readLine();
            	if(buf!=null){
            		buf = new String(buf.getBytes("gbk"),"UTF-8");
                	System.out.println(buf);
            	}else {
            		Scanner scan = new Scanner(System.in);
					String cmd = scan.nextLine();
					openChannel.setCommand(cmd);
//					outstream.write(cmd.getBytes());
//					outstream.flush();
					  exitStatus = openChannel.getExitStatus();
				      System.out.println(exitStatus);
				      openChannel.connect();  
            	}
            }
/*            String buf = null;
            while ((buf = reader.readLine()) != null) {
            	buf = new String(buf.getBytes("gbk"),"UTF-8");
            	System.out.println(buf);
//            	result+= new String(buf.getBytes("gbk"),"UTF-8")+"    <br>\r\n";  
            }  
*/	    } catch (JSchException | IOException e) {
	      result+=e.getMessage();
	    }finally{
	      if(openChannel!=null&&!openChannel.isClosed()){
	        openChannel.disconnect();
	      }
	      if(session!=null&&session.isConnected()){
	        session.disconnect();
	      }
	    }
	    return result;
	  }
	
	 public static void main(String args[]) throws Exception{
		 String ip = "192.168.111.133";
		 String user = "root";
		 String psw = "shaojianye";
		 int port =22;
		 String privateKey = null;
		 String passphrase = null;
//		 sshShell(ip, user, psw, port, privateKey, passphrase);
		 
//		 String aa = exec(ip, user, psw, port, "ls -ltr");
//		 System.out.println(aa);
		 
		 JSch jsch=new JSch();
	     Session session = jsch.getSession(user, ip, port);
	     java.util.Properties config = new java.util.Properties();
	     config.put("StrictHostKeyChecking", "no");
	     session.setConfig(config);
	     session.setPassword(psw);
	     session.connect();
	     
//	     ChannelExec openChannel = (ChannelExec) session.openChannel("exec");;
	     ChannelExec openChannel =  null;
	     String[] cmdArray = {"echo $JAVA_HOME"};
		 
	      openChannel = (ChannelExec) session.openChannel("exec");
	      openChannel.setCommand(cmdArray[0]);
	      int exitStatus = openChannel.getExitStatus();
 	      System.out.println(exitStatus);
	      openChannel.connect();  
	      System.out.println("连接-----------");
	      InputStream in = openChannel.getInputStream();  
          BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
          String buf = null;
          while ((buf = reader.readLine()) != null) {
            buf = new String(buf.getBytes("gbk"),"UTF-8");
          	System.out.println(buf);
          }
	 }
}
