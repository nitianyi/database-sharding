package com.opensource.orm.sharding.utils;

public abstract class HashUtils {

	public static long hash(Object ...value){
		String key=toString(value);
		long hash;
		int  i;
		for (hash = key.length(), i = 0; i < key.length(); ++i)
			hash = (hash << 4) ^ (hash >> 28) ^ key.charAt(i);
		return hash;
	}
	private static String toString(Object ...values){
		StringBuilder sb=new StringBuilder();
		for(Object value:values){
			sb.append(value.toString());
		}
		return sb.toString();
	}
}
