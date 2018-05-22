package com.example.parser;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Demo {
	
	public static final String SAMPLE_FILE = "file/classes2.dex";

	public static void main(String[] args) {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream("file/result.txt"));
			FileInputStream fis = new FileInputStream(SAMPLE_FILE);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while((len=fis.read(buffer)) != -1){
				bos.write(buffer, 0, len);
			}
			byte[] dexData = bos.toByteArray();
			bos.close();
			fis.close();
			
			DexParser dexParser = new DexParser(dexData, writer);
			dexParser.parse();
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

