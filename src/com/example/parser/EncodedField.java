package com.example.parser;

public class EncodedField {

	public int fieldIndex;
	public String accessFlags;
	public String fieldName;
	
	@Override
	public String toString() {
		return "EncodedField [fieldIndex=" + fieldIndex + ", accessFlags="
				+ accessFlags + ", fieldName=" + fieldName + "]";
	}
	
	
}
