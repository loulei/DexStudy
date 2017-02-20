package com.example.parser;

import java.util.List;

public class ClassDataItem {

	public int staticFieldSize;
	public int instaceFieldSize;
	public int directMethodSize;
	public int virtualMethodSize;
	
	public List<EncodedField> staticFields;
	public List<EncodedField> instanceFields;
	public List<EncodedMethod> directMethods;
	public List<EncodedMethod> virtualMethods;
	
	@Override
	public String toString() {
		return "ClassDataItem [staticFieldSize=" + staticFieldSize
				+ ", instaceFieldSize=" + instaceFieldSize
				+ ", directMethodSize=" + directMethodSize
				+ ", virtualMethodSize=" + virtualMethodSize
				+ ", staticFields=" + staticFields + ", instanceFields="
				+ instanceFields + ", directMethods=" + directMethods
				+ ", virtualMethods=" + virtualMethods + "]";
	}
}
