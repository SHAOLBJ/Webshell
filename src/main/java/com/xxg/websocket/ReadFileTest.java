package com.xxg.websocket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadFileTest {

	public static void main(String[] args) throws IOException {
		String line="";
		File file=new File("F:\\webshell\\text.txt");
		 InputStreamReader read = new InputStreamReader(
                 new FileInputStream(file),"utf-8");
		BufferedReader reader = new BufferedReader(read);
		while((line = reader.readLine()) != null) {
			System.out.println("in:"+line);
			System.out.println(line=="");
			System.out.println(line=="\n\r");
			System.out.println(line==null);
			System.out.println(line.length()==0);
		}
		
		reader.close();
	}
}
