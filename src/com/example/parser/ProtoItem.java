package com.example.parser;

import java.util.Arrays;

public class ProtoItem {

	public String methodSignature;
	public String returnType;
	public String[] paramTypes;
	public String fullMethodSignature;
	@Override
	public String toString() {
		return "ProtoItem [methodSignature=" + methodSignature
				+ ", returnType=" + returnType + ", paramTypes="
				+ Arrays.toString(paramTypes) + ", fullMethodSignature="
				+ fullMethodSignature + "]";
	}
	
}
