package com.example.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DexParser {

	public static byte[] DEX_DATA;

	public static final int LEN_HEADER = 0x70;

	public static final byte[] MAGIC = new byte[] { 0x64, 0x65, 0x78, 0x0a,
			0x30, 0x33, 0x35, 0x00 };
	
	public static final int MAP_ITEM_LEN = 12;

	public static byte[] CHECKSUM = new byte[4];

	public static byte[] SIGNATURE = new byte[20];

	public static byte[] FILE_SIZE = new byte[4];

	public static byte[] HEADER_SIZE = new byte[4];

	public static byte[] ENDIAN_TAG = new byte[] { 0x78, 0x56, 0x34, 0x12 };

	public static byte[] MAP_OFFSET = new byte[4];
	
	public static byte[] MAP_DATA;
	
	public static int map_size;
	
	public static int file_size;
	
	public static int map_offset;
	
	public static int map_item_count;
	
	public List<MapItem> mapItems;
	
	public StringItem[] stringItems;
	
	public TypeItem[] typeItems;
	
	public ProtoItem[] protoItems;
	
	public FieldItem[] fieldItems;
	
	public MethodItem[] methodItems;
	
	public ClassDefItem[] classDefItems;
	
	private PrintWriter writer;
	
	public DexParser(byte[] data, PrintWriter writer){
		this.writer = writer;
		if (data == null || data.length <= 0x70) {
			writer.println("invalid dex file");
			return;
		}
		DEX_DATA = new byte[data.length];
		System.arraycopy(data, 0, DEX_DATA, 0, data.length);
	}
	
	public void parse() {
		// TODO Auto-generated constructor stub
		boolean isValid = verifyMagic();
		writer.println("verify magic : " + isValid);
		if (!isValid)
			return;

		isValid = verifyCheckSum();
		writer.println("verify checksum : " + isValid);
		if (!isValid)
			return;

		isValid = verifySignature();
		writer.println("verify signature : " + isValid);
		if (!isValid)
			return;

		isValid = verifyFileSize();
		writer.println("verify filesize : " + isValid);
		writer.println("file size : " + file_size);
		if (!isValid)
			return;

		isValid = verifyHeaderSize();
		writer.println("verify headersize : " + isValid);
		if (!isValid)
			return;

		isValid = verifyEndianTag();
		writer.println("verify endian tag : " + isValid);
		if (!isValid)
			return;
		
		isValid = verifyMapOffset();
		writer.println("verify map offset : " + isValid);
		writer.println("map offset : 0x" + Integer.toHexString(map_offset));
		writer.println("map size : " + map_size);
		
		parseMapData();
		
		try {
			parseIds();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean verifyMagic() {
		byte[] magic = new byte[MAGIC.length];
		System.arraycopy(DEX_DATA, 0, magic, 0, MAGIC.length);
		return Arrays.equals(MAGIC, magic);
	}

	public boolean verifyCheckSum() {
		System.arraycopy(DEX_DATA, 8, CHECKSUM, 0, CHECKSUM.length);
		byte[] checkSumBytes = Utils.doCheckSumAlder32(DEX_DATA, MAGIC.length
				+ CHECKSUM.length);
		return Arrays.equals(CHECKSUM, checkSumBytes);
	}

	public boolean verifySignature() {
		System.arraycopy(DEX_DATA, 12, SIGNATURE, 0, SIGNATURE.length);
		byte[] signatureBytes = Utils.doCheckSha1(DEX_DATA, MAGIC.length
				+ CHECKSUM.length + SIGNATURE.length);
		String str1 = Utils.bytes2HexStr(SIGNATURE);
		String str2 = Utils.bytes2HexStr(signatureBytes);
		writer.println("str1:"+str1);
		writer.println("str2:"+str2);
		return Arrays.equals(SIGNATURE, signatureBytes);
	}

	public boolean verifyFileSize() {
		System.arraycopy(DEX_DATA, 32, FILE_SIZE, 0, FILE_SIZE.length);
		file_size = Utils.byteToInt(FILE_SIZE);
		return file_size == DEX_DATA.length;
	}

	public boolean verifyHeaderSize() {
		System.arraycopy(DEX_DATA, 36, HEADER_SIZE, 0, HEADER_SIZE.length);
		int headerSize = Utils.byteToInt(HEADER_SIZE);
		return headerSize == LEN_HEADER;
	}

	public boolean verifyEndianTag() {
		byte[] endianTagBytes = new byte[ENDIAN_TAG.length];
		System.arraycopy(DEX_DATA, 40, endianTagBytes, 0, endianTagBytes.length);
		return Arrays.equals(endianTagBytes, ENDIAN_TAG);
	}

	public boolean verifyMapOffset() {
		System.arraycopy(DEX_DATA, 52, MAP_OFFSET, 0, MAP_OFFSET.length);
		map_offset = Utils.byteToInt(MAP_OFFSET);
		byte[] mapSizeBytes = new byte[4];
		System.arraycopy(DEX_DATA, map_offset, mapSizeBytes, 0, 4);
		map_item_count = Utils.byteToInt(mapSizeBytes);
		map_size = map_item_count * MAP_ITEM_LEN + 4;
		return (map_offset + map_size) == file_size;
	}
	
	public void parseMapData(){
		MAP_DATA = new byte[map_size-4];
		System.arraycopy(DEX_DATA, map_offset+4, MAP_DATA, 0, MAP_DATA.length);
		mapItems = new ArrayList<MapItem>();
		byte[] mapItemBytes = new byte[MAP_ITEM_LEN];
		for(int i=0; i<map_item_count; i++){
			System.arraycopy(MAP_DATA, i*MAP_ITEM_LEN, mapItemBytes, 0, MAP_ITEM_LEN);
			MapItem mapItem = new MapItem(mapItemBytes);
			writer.println(mapItem.toString());
			mapItems.add(mapItem);
		}
	}
	
	public void parseIds() throws IOException{
		if(mapItems != null && mapItems.size() > 0){
			for (MapItem mapItem : mapItems) {
				switch (mapItem.type) {
				case MapItem.TYPE_STRING_ID_ITEM:
					parseStringData(mapItem);
					break;
				case MapItem.TYPE_TYPE_ID_ITEM:
					parseTypeData(mapItem);
					break;
				case MapItem.TYPE_PROTO_ID_ITEM:
					parseProtoData(mapItem);
					break;
				case MapItem.TYPE_FIELD_ID_ITEM:
					parseFieldData(mapItem);
					break;
				case MapItem.TYPE_METHOD_ID_ITEM:
					parseMethodData(mapItem);
					break;
				case MapItem.TYPE_CLASS_DEF_ITEM:
					parseClassDefData(mapItem);
					break;
				default:
					break;
				}
			}
		}
	}

	private void parseStringData(MapItem mapItem) throws IOException {
		stringItems = new StringItem[mapItem.size];
		for(int i=0; i<stringItems.length; i++){
			InputStream inputStream = Utils.byte2InputStream(DEX_DATA);
			inputStream.mark(file_size);
			int string_id_offset = mapItem.offset+i*MapItem.TYPE_STRING_ID_ITEM_LEN;
			inputStream.skip(string_id_offset);
			byte[] string_data_offset_bytes = new byte[4];
			inputStream.read(string_data_offset_bytes);
			int string_data_offset = Utils.byteToInt(string_data_offset_bytes);
			inputStream.reset();
			inputStream.skip(string_data_offset);
			int strlen = Utils.decodeULEB128(inputStream);
			
			
			byte[] strBytes = new byte[strlen];
			inputStream.read(strBytes);
			
			StringItem stringItem = new StringItem();
			stringItem.string_data_off = string_data_offset;
			stringItem.len = strlen;
			stringItem.data = strBytes;
			inputStream.close();
			
			writer.println(stringItem.toString());
			stringItems[i] = stringItem;
		}
	}
	
	private void parseTypeData(MapItem mapItem) throws IOException{
		typeItems = new TypeItem[mapItem.size];
		for(int i=0; i<typeItems.length; i++){
			InputStream inputStream = Utils.byte2InputStream(DEX_DATA);
			int type_id_offset = mapItem.offset + i*MapItem.TYPE_TYPE_ID_ITEM_LEN;
			inputStream.skip(type_id_offset);
			byte[] type_id_bytes = new byte[4];
			inputStream.read(type_id_bytes);
			int type_id = Utils.byteToInt(type_id_bytes);
			
			TypeItem typeItem = new TypeItem();
			typeItem.value = type_id;
			typeItem.type = new String(stringItems[type_id].data);
			inputStream.close();
			
			writer.println(typeItem);
			typeItems[i] = typeItem;
		}
	}
	
	private void parseProtoData(MapItem mapItem) throws IOException{
		protoItems = new ProtoItem[mapItem.size];
		for(int i=0; i<protoItems.length; i++){
			InputStream inputStream = Utils.byte2InputStream(DEX_DATA);
			inputStream.mark(file_size);
			int proto_id_offset = mapItem.offset + i*MapItem.TYPE_PROTO_ID_ITEM_LEN;
			inputStream.skip(proto_id_offset);
			
			ProtoItem protoItem = new ProtoItem();
			
			byte[] method_sig_bytes = new byte[4];
			inputStream.read(method_sig_bytes);
			int method_sig_id = Utils.byteToInt(method_sig_bytes);
			protoItem.methodSignature = new String(stringItems[method_sig_id].data);
			
			byte[] return_type_bytes = new byte[4];
			inputStream.read(return_type_bytes);
			int return_type_id = Utils.byteToInt(return_type_bytes);
			protoItem.returnType = typeItems[return_type_id].type;
			
			byte[] param_offset_bytes = new byte[4];
			inputStream.read(param_offset_bytes);
			int param_offset = Utils.byteToInt(param_offset_bytes);
			if(param_offset == 0){
				//no parameter
				protoItem.paramTypes = null;
				protoItem.fullMethodSignature = "()"+protoItem.returnType;
			}else{
				inputStream.reset();
				inputStream.skip(param_offset);
				
				byte[] param_size_bytes = new byte[4];
				inputStream.read(param_size_bytes);
				int param_size = Utils.byteToInt(param_size_bytes);
				String[] paramTypes = new String[param_size];
				byte[] param = new byte[2];
				StringBuilder stringBuilder = new StringBuilder();
				for(int j=0; j<param_size; j++){
					inputStream.read(param);
					paramTypes[j] = typeItems[Utils.byteToShort(param)].type;
					stringBuilder.append(paramTypes[j]);
				}
				protoItem.paramTypes = paramTypes;
				
				protoItem.fullMethodSignature = "("+ stringBuilder.toString() +")"+protoItem.returnType;
			}
			inputStream.close();
			
			writer.println(protoItem);
			protoItems[i] = protoItem;
		}
	}
	
	private void parseFieldData(MapItem mapItem) throws IOException{
		fieldItems = new FieldItem[mapItem.size];
		for(int i=0; i<fieldItems.length; i++){
			InputStream inputStream = Utils.byte2InputStream(DEX_DATA);
			int field_id_offset = mapItem.offset + i*MapItem.TYPE_FIELD_ID_ITEM_LEN;
			inputStream.skip(field_id_offset);
			
			FieldItem fieldItem = new FieldItem();
			
			byte[] class_id_bytes = new byte[2];
			inputStream.read(class_id_bytes);
			fieldItem.belongClass = typeItems[Utils.byteToShort(class_id_bytes)].type;
			
			byte[] type_id_bytes = new byte[2];
			inputStream.read(type_id_bytes);
			fieldItem.typeName = typeItems[Utils.byteToShort(type_id_bytes)].type;
			
			byte[] field_name_bytes = new byte[4];
			inputStream.read(field_name_bytes);
			fieldItem.fieldname = new String(stringItems[Utils.byteToInt(field_name_bytes)].data);
			
			inputStream.close();
			
			writer.println(fieldItem.toString());
			fieldItems[i] = fieldItem;
		}
	}
	
	private void parseMethodData(MapItem mapItem) throws IOException{
		methodItems = new MethodItem[mapItem.size];
		for(int i=0; i<methodItems.length; i++){
			InputStream inputStream = Utils.byte2InputStream(DEX_DATA);
			int method_id_offset = mapItem.offset + i * MapItem.TYPE_METHOD_ID_ITEM_LEN;
			inputStream.skip(method_id_offset);
			
			MethodItem methodItem = new MethodItem();
			
			byte[] class_id_bytes = new byte[2];
			inputStream.read(class_id_bytes);
			methodItem.belongClass = typeItems[Utils.byteToShort(class_id_bytes)].type;
			
			byte[] proto_id_bytes = new byte[2];
			inputStream.read(proto_id_bytes);
			methodItem.protoName = protoItems[Utils.byteToShort(proto_id_bytes)].fullMethodSignature;
			
			byte[] name_id_bytes = new byte[4];
			inputStream.read(name_id_bytes);
			methodItem.methodName = new String(stringItems[Utils.byteToInt(name_id_bytes)].data);
			
			inputStream.close();
			writer.println(methodItem);
			methodItems[i] = methodItem;
		}
	}
	
	private void parseClassDefData(MapItem mapItem) throws IOException{
		writer.println("mapItem size:"+mapItem.size);
		classDefItems = new ClassDefItem[mapItem.size];
		for(int i=0; i<mapItem.size; i++){
			InputStream inputStream = Utils.byte2InputStream(DEX_DATA);
			int classDefItemOffset = mapItem.offset + i * MapItem.TYPE_CLASS_DEF_ITEM_LEN;
			inputStream.skip(classDefItemOffset);
			
			ClassDefItem classDefItem = new ClassDefItem();
			
			byte[] class_id_bytes = new byte[4];
			inputStream.read(class_id_bytes);
			classDefItem.className = typeItems[Utils.byteToInt(class_id_bytes)].type;
			
			byte[] access_flag_bytes = new byte[4];
			inputStream.read(access_flag_bytes);
			classDefItem.accessFlags = AccessFlag.getName(Utils.byteToInt(access_flag_bytes));
			
			byte[] super_class_bytes = new byte[4];
			inputStream.read(super_class_bytes);
			classDefItem.superClassName = typeItems[Utils.byteToInt(super_class_bytes)].type;
			
			byte[] interfaces_offset_bytes = new byte[4];
			inputStream.read(interfaces_offset_bytes);
			int interfacesOffset = Utils.byteToInt(interfaces_offset_bytes);
			if(interfacesOffset == 0){
				//no implement interface
			}
			
			byte[] source_file_offset_bytes = new byte[4];
			inputStream.read(source_file_offset_bytes);
			int sourceFileOffset = Utils.byteToInt(source_file_offset_bytes);
			if((sourceFileOffset & 0xffffffff) == 0xffffffff){
				//no index
				classDefItem.sourceFile = "no index";
			}else{
				classDefItem.sourceFile = new String(stringItems[sourceFileOffset].data);
			}
			
			byte[] annotions_off_bytes = new byte[4];
			inputStream.read(annotions_off_bytes);
			classDefItem.annotions_off = Utils.byteToInt(annotions_off_bytes);
			
			byte[] class_data_off = new byte[4];
			inputStream.read(class_data_off);
			classDefItem.class_data_off = Utils.byteToInt(class_data_off);
			
			if(classDefItem.class_data_off != 0){
				parseClassData(classDefItem.class_data_off);
			}
			
			byte[] static_value_off = new byte[4];
			inputStream.read(static_value_off);
			classDefItem.static_value_off = Utils.byteToInt(static_value_off);
			
			writer.println(classDefItem.toString());
			classDefItems[i] = classDefItem;
		}
			
	}
	
	private void parseClassData(int offset) throws IOException{
		ClassDataItem classDataItem = new ClassDataItem();
		
		InputStream inputStream = Utils.byte2InputStream(DEX_DATA);
		inputStream.skip(offset);
		
		classDataItem.staticFieldSize = Utils.decodeULEB128(inputStream);
		classDataItem.instaceFieldSize = Utils.decodeULEB128(inputStream);
		classDataItem.directMethodSize = Utils.decodeULEB128(inputStream);
		classDataItem.virtualMethodSize = Utils.decodeULEB128(inputStream);
		
		classDataItem.staticFields = new ArrayList<EncodedField>();
		if(classDataItem.staticFieldSize > 0){
			for(int i=0; i<classDataItem.staticFieldSize; i++){
				EncodedField encodedField = new EncodedField();
				encodedField.fieldIndex = Utils.decodeULEB128(inputStream);
				encodedField.accessFlags = AccessFlag.getName(Utils.decodeULEB128(inputStream));
				encodedField.fieldName = fieldItems[encodedField.fieldIndex].fieldname;
				classDataItem.staticFields.add(i, encodedField);
			}
		}
		
		classDataItem.instanceFields = new ArrayList<EncodedField>();
		if(classDataItem.instaceFieldSize > 0){
			for(int i=0; i<classDataItem.instaceFieldSize; i++){
				EncodedField encodedField = new EncodedField();
				encodedField.fieldIndex = Utils.decodeULEB128(inputStream);
				encodedField.accessFlags = AccessFlag.getName(Utils.decodeULEB128(inputStream));
				encodedField.fieldName = fieldItems[encodedField.fieldIndex].fieldname;
				classDataItem.instanceFields.add(i, encodedField);
			}
		}
		
		classDataItem.directMethods = new ArrayList<EncodedMethod>();
		if(classDataItem.directMethodSize > 0){
			for(int i=0; i<classDataItem.directMethodSize; i++){
				EncodedMethod encodedMethod = new EncodedMethod();
				encodedMethod.methodIndex = Utils.decodeULEB128(inputStream);
				encodedMethod.accessFlags = AccessFlag.getName(Utils.decodeULEB128(inputStream));
				encodedMethod.codeOffset = Utils.decodeULEB128(inputStream);
				encodedMethod.methodName = methodItems[encodedMethod.methodIndex].methodName;
				classDataItem.directMethods.add(i, encodedMethod);
				parseCodeItem(encodedMethod, encodedMethod.codeOffset);
			}
		}
		
		classDataItem.virtualMethods = new ArrayList<EncodedMethod>();
		if(classDataItem.virtualMethodSize > 0){
			for(int i=0; i<classDataItem.virtualMethodSize; i++){
				EncodedMethod encodedMethod = new EncodedMethod();
				encodedMethod.methodIndex = Utils.decodeULEB128(inputStream);
				encodedMethod.accessFlags = AccessFlag.getName(Utils.decodeULEB128(inputStream));
				encodedMethod.codeOffset = Utils.decodeULEB128(inputStream);
				encodedMethod.methodName = methodItems[encodedMethod.methodIndex].methodName;
				classDataItem.virtualMethods.add(i, encodedMethod);
				parseCodeItem(encodedMethod, encodedMethod.codeOffset);
			}
		}
		
		writer.println(classDataItem.toString());
	}
	
	private void parseCodeItem(EncodedMethod encodedMethod, int offset) throws IOException{
		CodeItem codeItem = new CodeItem();
		
		InputStream inputStream = Utils.byte2InputStream(DEX_DATA);
		inputStream.skip(offset);
		
		byte[] ushort_data = new byte[2];
		byte[] uint_data = new byte[4];
		
		inputStream.read(ushort_data);
		codeItem.registerSize = Utils.byteToShort(ushort_data);
		inputStream.read(ushort_data);
		codeItem.inSize = Utils.byteToShort(ushort_data);
		inputStream.read(ushort_data);
		codeItem.outSize = Utils.byteToShort(ushort_data);
		inputStream.read(ushort_data);
		codeItem.trySize = Utils.byteToShort(ushort_data);
		inputStream.read(uint_data);
		codeItem.debugInfoOffset = Utils.byteToInt(uint_data);
		inputStream.read(uint_data);
		codeItem.insnsSize = Utils.byteToInt(uint_data);
		
		if(codeItem.insnsSize < 0 || codeItem.insnsSize > file_size){
			return;
		}
		
		byte[] code_op_data = new byte[codeItem.insnsSize * 2];
		inputStream.read(code_op_data);
		codeItem.insns = code_op_data;
		
		writer.println(encodedMethod.methodName+" : "+codeItem.toString());
	}
	
}













