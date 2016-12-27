package com.example.parser;


public class StringItem {

	public int string_data_off;
	public int len;
	public byte[] data;
	
	
	@Override
	public String toString() {
		return "StringItem [ string_data_off=0x" + Integer.toHexString(string_data_off) + ", len="
				+ len + ", str=" + new String(data) + "]";
	}
	
	
	
}
