package com.xxg.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JSchDemo {
	private String charset = "UTF-8"; // 设置编码格式
	private String user; // 用户名
	private String passwd; // 登录密码
	private String host; // 主机IP
	private JSch jsch;
	private Session session;
	/**
	 * 
	 * @param user用户名
	 * @param passwd密码
	 * @param host主机IP
	 */
	public JSchDemo(String user, String passwd, String host) {
		this.user = user;
		this.passwd = passwd;
		this.host = host;
	}

	/**
	 * 连接到指定的IP
	 * 
	 * @throws JSchException
	 */
	public void connect() throws JSchException {
		jsch = new JSch();
		session = jsch.getSession(user, host, 22);
		session.setPassword(passwd);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		System.out.println("连接成功！");
	}

	/**
	 * 执行相关的命令
	 */
	public void execCmd() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String command = "";
		BufferedReader reader = null;
		Channel channel = null;
		try {
			while ((command = br.readLine()) != null) {
				channel = session.openChannel("exec");
				System.out.println(command);
				((ChannelExec) channel).setCommand("source /etc/profile;source ~/.bash_profile;"+command);
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);

				channel.connect();
				InputStream in = channel.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in,
						Charset.forName(charset)));
				String buf = null;
				System.out.println("----------------");
				while ((buf = reader.readLine()) != null) {
					if(buf.length()==0){
						System.out.println("\n");
					 }else{
						 System.out.println(buf);
					 }
				}
				System.out.println("****************");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			channel.disconnect();
			session.disconnect();
		}
	}
	
	
	public void execShellCmd() {
		String command = "";
		BufferedReader reader = null;
		ChannelShell channel = null;
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			while ((command = br.readLine()) != null) {
				channel = (ChannelShell) session.openChannel("shell");
				channel.connect(1000);
				OutputStream outstream =  channel.getOutputStream();
				outstream.write(command.getBytes());
				outstream.flush();
				channel.setInputStream(null);

				channel.connect();
				InputStream in = channel.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in,
						Charset.forName(charset)));
				String buf = null;
				while ((buf = reader.readLine()) != null) {
					System.out.println(buf);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			channel.disconnect();
			session.disconnect();
		}
	}
	
	public void execCmd2() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String command = "";
		BufferedReader reader = null;
		Channel channel = null;

		try {
			while ((command = br.readLine()) != null) {
				channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand("source /etc/profile;source ~/.bash_profile;"+command);
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);

				channel.connect();
				InputStream in = channel.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in,
						Charset.forName(charset)));
				
				String buf = null;
				while ((buf = reader.readLine()) != null) {
					System.out.println(buf);
				}
				/*byte[] buffer=new byte[2048];
				int count=0;
//				count=in.read(buffer);
				if (-1!=(count=in.read(buffer))) {
					String tmp = new String(buffer,0,count);
					System.out.println(tmp);
					 
				}*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			channel.disconnect();
			session.disconnect();
		}
	}

	public static void main(String[] args) throws Exception {
		String user = "root";
		String passwd = "shaojianye";
		String host = "192.168.111.133";

		JSchDemo demo = new JSchDemo(user, passwd, host);
		demo.connect();
		//demo.execShellCmd();
		demo.execCmd();
	}
}
