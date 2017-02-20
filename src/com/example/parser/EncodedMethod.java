package com.example.parser;

public class EncodedMethod {

	public int methodIndex;
	public String accessFlags;
	public int codeOffset;
	public String methodName;
	
	@Override
	public String toString() {
		return "EncodedMethod [methodIndex=" + methodIndex + ", accessFlags="
				+ accessFlags + ", codeOffset=" + codeOffset + ", methodName="
				+ methodName + "]";
	}
}
