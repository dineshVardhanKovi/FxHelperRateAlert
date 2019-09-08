package com.fxHelper.general.utils;

import java.util.*;

public class Base64File {
	public static void main(String[] args) {

		// create a sample String to encode
		String sample = "1234";

		// print actual String
		System.out.println("Sample String:\n" + sample);

		// Encode into Base64 format
		String BasicBase64format = Base64.getEncoder().encodeToString(sample.getBytes());

		// print encoded String
		System.out.println("Encoded String:\n" + BasicBase64format);
	}

	public static String getDecodedKey(String encodedValue) {
		return new String(Base64.getDecoder().decode(encodedValue));
	}
}