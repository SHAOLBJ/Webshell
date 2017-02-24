package com.xxg.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import javax.websocket.Session;

public class WebCrtExecCommandCall implements Callable<String> {
	
	private BufferedReader reader;
	private Session session;
	
	public WebCrtExecCommandCall(InputStream in,Session session) {
		this.reader = new BufferedReader(new InputStreamReader(in));
		this.session = session;
		
	}

	@Override
	public String call() throws Exception {
		System.out.println("start exec command thread");
		String pwd = "";
		String line;
		try {
			while((line = reader.readLine()) != null) {
				if(line.startsWith("pwd=")){
					pwd = line.substring(4);
					continue;
				}
				// 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
				session.getBasicRemote().sendText(line);
				System.out.println("命令返回="+line);
			}
			System.out.println("command exec over");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pwd;
	}

}
