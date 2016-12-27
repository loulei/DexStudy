package com.example.parser;

public class MapItem {

	public short type;
	public int size;
	public int offset;
	
	public static final short TYPE_STRING_ID_ITEM = 0x0001;
	public static final short TYPE_TYPE_ID_ITEM = 0x0002;
	public static final short TYPE_PROTO_ID_ITEM = 0x0003;
	public static final short TYPE_FIELD_ID_ITEM = 0x0004;
	public static final short TYPE_METHOD_ID_ITEM = 0x0005;
	public static final short TYPE_CLASS_DEF_ITEM = 0x0006;
	
	public static final int TYPE_STRING_ID_ITEM_LEN = 4;
	public static final int TYPE_TYPE_ID_ITEM_LEN = 4;
	public static final int TYPE_PROTO_ID_ITEM_LEN = 12;
	public static final int TYPE_FIELD_ID_ITEM_LEN = 8;
	public static final int TYPE_METHOD_ID_ITEM_LEN = 8;
	public static final int TYPE_CLASS_DEF_ITEM_LEN = 32;
	
	public MapItem(byte[] mapItemBytes) {
		// TODO Auto-generated constructor stub
		byte[] typeBytes = new byte[2];
		byte[] sizeBytes = new byte[4];
		byte[] offsetBytes = new byte[4];
		
		System.arraycopy(mapItemBytes, 0, typeBytes, 0, typeBytes.length);
		System.arraycopy(mapItemBytes, 4, sizeBytes, 0, sizeBytes.length);
		System.arraycopy(mapItemBytes, 8, offsetBytes, 0, offsetBytes.length);
		
		this.type = Utils.byteToShort(typeBytes);
		this.size = Utils.byteToInt(sizeBytes);
		this.offset = Utils.byteToInt(offsetBytes);
	}

	@Override
	public String toString() {
		return "MapItem [type=0x" + Integer.toHexString(0xffff & type) + ", size=" + size + ", offset=0x"
				+ Integer.toHexString(offset) + "]";
	}
}
