package com.xxg.websocket;  
  
import java.io.OutputStream;  
import java.io.PrintWriter;

  
/** 
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流 
 * @author shaojianye 
 * 
 */  
public class StreamOutput extends Thread { 
	OutputStream out ; 
	String type; 
	String message;
	     
	StreamOutput(OutputStream out, String type,String message) { 
		this.out = out; 
        this.type = type; 
        this.message=message;
	} 
    public void run() { 
        try { 
        	PrintWriter pw = null;
			String val=message+" \n";
			System.out.println(val);
			//判断exit是否可以退出，待测试
	        if (out!=null){
	            pw = new PrintWriter(out); 
	            pw.println(val);
	            pw.flush();
	        } 
          } catch (Exception ioe) { 
            ioe.printStackTrace();   
        } 
      } 
	} 
