package com.xxg.websocket;  
  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.websocket.Session;  
  
/** 
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流 
 * @author shaojianye 
 * 
 */  
public class StreamGobbler extends Thread { 
	InputStream is; 
	String type;
	Session session;
	     
	StreamGobbler(InputStream is, String type,Session session) { 
		this.is = is; 
        this.type = type; 
        this.session=session;
	} 
    public void run() { 
        try { 
            InputStreamReader isr = new InputStreamReader(is); 
            BufferedReader reader = new BufferedReader(isr); 
            String line=null; 
            if("INPUT".equals(type)){
	            while((line = reader.readLine()) != null) {
	            	 System.out.println("---------");
					 System.out.println("in:"+line);
					 if(line.length()==0){
						 session.getBasicRemote().sendText("\n"); 
					 }else{
						 session.getBasicRemote().sendText(line);
				    }
				}
            }
//            if("TitleInput".equals(type)){
//            	System.out.println("1111111111111");
//	            while((line = reader.readLine()) != null) {
//					 System.out.println("in:"+line);
//					 String termShowStr="["+line+" /]";
//					 session.getBasicRemote().sendText("startFlag="+termShowStr);
//					 }
//			}
            if("ERROR".equals(type)){
            	 while((line = reader.readLine()) != null) {
					 System.out.println("in:"+line);
					 if(line.length()==0){
						 session.getBasicRemote().sendText("\n"); 
					 }else{
						 session.getBasicRemote().sendText(line);
					 }
				}
            }
        } catch (Exception ioe) { 
            ioe.printStackTrace();   
        } 
    } 
	} 
