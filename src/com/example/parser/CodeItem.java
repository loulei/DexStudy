package com.example.parser;

import java.util.Arrays;

public class CodeItem {

	public short registerSize;
	public short inSize;
	public short outSize;
	public short trySize;
	public int debugInfoOffset;
	public int insnsSize;
	public byte[] insns;
	@Override
	public String toString() {
		return "CodeItem [registerSize=" + registerSize + ", inSize=" + inSize
				+ ", outSize=" + outSize + ", trySize=" + trySize
				+ ", debugInfoOffset=" + debugInfoOffset + ", insnsSize="
				+ insnsSize + ", insns=" + Utils.bytes2HexStr(insns) + "]";
	}
	
	
	
}
