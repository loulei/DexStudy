package com.example.parser;

import java.util.Arrays;

public class ClassDefItem {

	public String className;
	public String accessFlags;
	public String superClassName;
	public String[] interfaceNames;
	public String sourceFile;
	public int annotions_off;
	public int class_data_off;
	public int static_value_off;
	@Override
	public String toString() {
		return "ClassDefItem [className=" + className + ", accessFlags="
				+ accessFlags + ", superClassName=" + superClassName
				+ ", interfaceNames=" + Arrays.toString(interfaceNames)
				+ ", sourceFile=" + sourceFile + ", annotions_off=0x"
				+ Integer.toHexString(annotions_off) + ", class_data_off=0x" + Integer.toHexString(class_data_off)
				+ ", static_value_off=0x" + Integer.toHexString(static_value_off) + "]";
	}
}
