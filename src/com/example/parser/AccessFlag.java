package com.example.parser;

public enum AccessFlag {
	ACC_PUBLIC(0x01, "public"), ACC_PRIVATE(0x02, "private"), ACC_PROTECTED(0x04, "protected"),
	ACC_STATIC(0x08, "static"), ACC_FINAL(0x10, "final");
	
	private int index;
	private String name;
	
	private AccessFlag(int index, String name){
		this.index = index;
		this.name = name;
	}
	
	public static String getName(int index){
		for(AccessFlag flag : AccessFlag.values()){
			if(flag.getIndex() == index){
				return flag.getName();
			}
		}
		return null;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
