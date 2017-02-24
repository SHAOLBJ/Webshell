package com.xxg.websocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.Callable;

import javax.websocket.Session;
/**
 * 防止进程被占用并且可以返回结果（Runnable不返回结果）
 * @author shaojianye
 *
 */
public class WebCrtExecCommandCallable implements Callable<String>{
	private Process process;
	private Session session;
	
	public WebCrtExecCommandCallable(Process process, Session session) {
		super();
		this.process = process;
		this.session = session;
	}

	@Override
	public String call() throws Exception {
		System.out.println("测试执行call()方法");
		String pwd = "";
		String line;
		try {
			InputStream in = process.getInputStream();
			InputStream err = process.getErrorStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			System.out.println(reader);
			while((line = reader.readLine()) != null) {
				if(line.startsWith("pwd=")){
					pwd = line.substring(4);
					continue;
				}
				 System.out.println("in:"+line);
				 if(line.length()==0){
					 session.getBasicRemote().sendText("\n"); 
				 }else{
					 session.getBasicRemote().sendText(line);
				 }
				 
			}
			reader = new BufferedReader(new InputStreamReader(err));
			System.out.println("reader_err:"+reader.readLine());
			while((line = reader.readLine()) != null) {
				if(line.startsWith("pwd=")){
					pwd = line.substring(4);
					continue;
				}
				session.getBasicRemote().sendText(line);
				System.out.println("err:"+line);
			}
			if(reader!=null){
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("pwd="+pwd);
		return pwd;
	}
	
	public void testReader() throws IOException, FileNotFoundException{
		System.out.println("test----------");
		String line="";
		File file=new File("/home/centos/tomcat/apache-tomcat-7.0.70/webapps/text.txt");
		 InputStreamReader read = new InputStreamReader(
                 new FileInputStream(file),"utf-8");
		BufferedReader reader = new BufferedReader(read);
		while((line = reader.readLine()) != null) {
			System.out.println("test:"+line);
		}
		
		reader.close();
	}
	
}
