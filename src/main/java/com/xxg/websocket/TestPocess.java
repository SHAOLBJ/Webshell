package com.xxg.websocket;


public class TestPocess {
	
}

//	public static void main(String[] args) throws Exception {
//		String[] commandArray = {"/bin/sh","-c","docker exec -i b2873e708667 /bin/bash"};
//		Process process =Runtime.getRuntime().exec(commandArray);
//		OutputStream out = process.getOutputStream();
//		
//		
//		StreamGobbler infoGobbler = new StreamGobbler(process.getInputStream(), "INFO");  
//		infoGobbler.start();
//		
//		StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");  
//		errorGobbler.start();
//		
//		
//		PrintWriter pw = null; 
//        if (out!=null) 
//            pw = new PrintWriter(out); 
//		
//		Scanner input = new Scanner(System.in);
//		String val = null;       // 记录输入的字符串
//        do{
//            System.out.print("请输入：");
//            val = input.next();       // 等待输入值
//            pw.println(val);
//            pw.flush();
//            System.out.println("您输入的是："+val);
//        }while(!val.equals("#"));   // 如果输入的值不是#就继续输入
//        System.out.println("你输入了\"#\"，程序已经退出！");
//        input.close(); // 关闭资源
// 		
//		/*String type = "wangcc";
//		StreamGobbler2 errorGobbler = new StreamGobbler2(type);
//		errorGobbler.start();
//		System.out.println(type);*/
//		
//		String comd = "cd /dmcs";
//		System.out.println(comd.split(" ")[0]);
//		
//	}
//}
//
//
//class StreamGobbler extends Thread { 
//	InputStream is; 
//	String type; 
//	     
//	StreamGobbler(InputStream is, String type) { 
//		this.is = is; 
//        this.type = type; 
//	} 
//	     
//	    public void run() { 
//	        try { 
//	            InputStreamReader isr = new InputStreamReader(is); 
//	            BufferedReader br = new BufferedReader(isr); 
//	            String line=null; 
//	            /*while ( (line = br.readLine()) != null) { 
//	                System.out.println("["+this.type+"]"+line);  
//	            }*/
//	            while(true){
//	            	line = br.readLine();
//	            	if(line!=null){
//	            		System.out.println("["+this.type+"]"+line);  
//	            	}else {
//	            		Thread.sleep(5000);
//	            	}
//	            }
//	        } catch (Exception ioe) { 
//	            ioe.printStackTrace();   
//	        } 
//	    } 
//	} 
//
//
//class StreamGobbler2 extends Thread { 
//	String type2; 
//	     
//	StreamGobbler2( String type) { 
//		type = "wangccbak";
//        this.type2 = type; 
//	} 
//	     
//	    public void run() { 
//	        type2 = "ddd";
//	    } 
//	} 
