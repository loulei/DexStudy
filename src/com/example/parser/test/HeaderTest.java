package com.example.parser.test;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.example.parser.DexParser;
import com.example.parser.Utils;

public class HeaderTest {
	
	public static final String SAMPLE_FILE = "/home/loulei/ws_dex/DexParser/file/Hello.dex";

	@Test
	public void testMagic(){
		String header = "dex\n035\0";
		String hexStr = Utils.str2HexStr(header);
		System.out.println(hexStr);
	}
	
	@Test
	public void testCheckSum(){
		long checksum = Utils.doCheckSumAlder32(SAMPLE_FILE, DexParser.MAGIC.length+DexParser.CHECKSUM.length);
		System.out.println(checksum + " " +Long.toHexString(checksum));
		int lchecksum = (int) (0x00000000ffffffff & checksum);
		System.out.println(lchecksum);
		byte[] checksumBytes = Utils.intToByte(lchecksum);
		System.out.println(Utils.bytes2HexStr(checksumBytes));
	}
	
	@Test
	public void testULEB128(){
		byte[] data = new byte[]{(byte) 0x90, (byte)0x4E, (byte) 0x90, (byte)0x4E, (byte) 0x90, (byte)0x4E};
//		int result = Utils.decodeULEB128(data);
//		System.out.println(result);
//		System.out.println(Integer.toBinaryString(result));
		
		InputStream inputStream = Utils.byte2InputStream(data);
		try {
			int result = Utils.decodeULEB128(inputStream);
			System.out.println(result);
			System.out.println(inputStream.available());
			result = Utils.decodeULEB128(inputStream);
			System.out.println(result);
			System.out.println(inputStream.available());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}





















