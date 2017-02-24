package com.xxg.websocket;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/webSecureCRT")
public class WebSecureCRT {
	protected Runtime runtime = Runtime.getRuntime();
	protected Process process;
	protected InputStream inputStream;
	protected InputStream errStream;
	public static String termStr = "pp=`whoami`'@'`hostname`':'`pwd`'>' ";
	public String pwd = "";
	protected String termShowStr = "";
	public static String pwdCmdStr = "tt='pwd='`pwd` && echo $tt";
	public static String hostType = "/bin/sh -c"; //unix /usr/bin/ksh
	public static String hhStr = "\n";
	protected Map<String,String> envMap = System.getenv();
	protected String[] envStrArray;
	
	public static List<String> cmdList = new ArrayList<String>();

	static {
		cmdList.add("tail");
		cmdList.add("more");
	}
	
	public WebSecureCRT() {
		super();
		System.out.println("创建WebSecureCRT对象");
	}

//	private void initEnv(){
//		List<String> list = new ArrayList<String>();
//		String HOME = "";
//		for (Map.Entry<String, String> entry : envMap.entrySet()) {
//			System.out.println("env:"+entry.getKey() + " : " + entry.getValue());
//			if("HOME".equals(entry.getKey())){
//				HOME = entry.getValue();
//			}else {
//				list.add(entry.getKey()+"="+ entry.getValue());
//			}
//		}
//		//System.out.println(HOME);
//		list.add("PWD="+ HOME);
//		envStrArray = new String[list.size()];
//		for(int i=0;i<list.size();i++){
//			envStrArray[i] = list.get(i);
//			System.out.println("xxxx:"+envStrArray[i]);
//		}
//	}
//	
//	private String execSimpleShell(String command){
//		String result = "";
//		try {
////			String[] commandArray = {"/bin/sh","-c",command};
////			process = runtime.exec(commandArray,envStrArray);
//			inputStream = process.getInputStream();
//			errStream = process.getErrorStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//			String line = "";
//			System.out.println("read");
//			while((line = reader.readLine()) != null) {
//				result += line;
//				System.out.println("执行结果="+line);
//			}
//			reader = new BufferedReader(new InputStreamReader(errStream));
//			while((line = reader.readLine()) != null) {
//				result += line;
//				System.out.println("执行结果1="+line);
//			}
//			if(reader!=null){
//				reader.close();
//			}
//			onClose();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * 新的WebSocket请求开启
	 */
	@OnOpen
	public void onOpen(Session session) {
		String message="pp=`whoami`'@'`hostname` && echo $pp ";
		try {
			System.out.println("connect to server start...");
			String command = "docker exec -i b2873e708667 /bin/bash ";
			String[] commandArray = {"/bin/sh","-c",command};
		    process =Runtime.getRuntime().exec(commandArray,null);
		    System.out.println(process);
			onMessage(message, session);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//process.waitFor();
       // onClose();
	}
	
//	private void initTermShell(Session session){
//		try {
//			
//			String command = "pp=`whoami`'@'`hostname` && echo $pp ";
//			this.termShowStr = execSimpleShell(command) +":"+this.pwd+"]#";
//// 			System.out.println("************************************");
////			System.out.println("pwd="+this.pwd);
////			System.out.println("termShowStr="+this.termShowStr);
////			System.out.println("************************************"); 
//			session.getBasicRemote().sendText("startFlag="+termShowStr);
//			/*envMap.put("PWD", this.pwd);
//			initEnv();*/
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
	/**
	 * WebSocket请求关闭
	 */
	@OnClose
	public void onClose() {
		try {
			if(inputStream != null)
				inputStream.close();
			if(errStream != null)
				errStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(process != null)
			process.destroy();
	}
	
	@OnError
	public void onError(Throwable thr) {
		thr.printStackTrace();
	}
	
	/**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws Exception 
     */
    @OnMessage
    public void onMessage(String message, Session session) throws InterruptedException, ExecutionException, Exception {
    	System.out.println("from clinet message start [" + message+"]");
    	System.out.println(process);
    	StreamOutput outGobbler = new StreamOutput(process.getOutputStream(), "OUTPUT",message);  
		outGobbler.start();
//		String inputType="INPUT";
//		if("pp=`whoami`'@'`hostname` && echo $pp ".equals(message)){
//			inputType="TitleInput";
//		}
//		System.out.println(inputType);
		StreamGobbler infoGobbler = new StreamGobbler(process.getInputStream(), "INPUT",session);  
		infoGobbler.start();
		
		StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR",session);  
		errorGobbler.start();
	    System.out.println("from clinet message end...[" + message+"]");
    }
    
}
