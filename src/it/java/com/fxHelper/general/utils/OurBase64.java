package com.fxHelper.general.utils;

import com.google.common.io.BaseEncoding;

public class OurBase64 {
	
	// TODO: we'll eventually use Base64 in the JDK, in MIME mode.
	private static final String BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	// do NOT use this for encoding, it'll look very very ugly!
	private static final BaseEncoding DECODER;

	static {
		final StringBuilder sb = new StringBuilder();
		// basically, like commons codec base64 encoding, we'll skip anything that's
		// not in the alphabet as well as blanks.
		sb.append('\n');
		sb.append('\r');
		sb.append('\t');
		for(int ch = ' '; ch < 0x7f; ++ch) {
			if(BASE64_ALPHABET.indexOf(ch) != -1)
				continue;
			sb.append((char) ch);
		}
		DECODER = BaseEncoding.base64().withSeparator(sb.toString(), 80);
	}
	
	private OurBase64() {}
	
	public static byte[] sloppyDecode(CharSequence toDecode) {
		return DECODER.decode(toDecode);
	}
	
	public static byte[] decode(CharSequence toDecode) {
		return BaseEncoding.base64().decode(toDecode);
	}
	
	public static String encode(byte[] data) {
		return BaseEncoding.base64().encode(data);
	}
	
	public static boolean isBase64(CharSequence toDecode) {
		try {
			BaseEncoding.base64().decode(toDecode);
			return true;
		} catch(IllegalArgumentException e) {
			return false;
		}
	}
	
}
