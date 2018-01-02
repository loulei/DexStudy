package com.example.parser;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Demo {
	
	public static final String SAMPLE_FILE = "/home/loulei/ws_dex/DexParser/file/classes.dex";

	public static void main(String[] args) {
		try {
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
			
			DexParser dexParser = new DexParser(dexData);
			dexParser.parse();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

