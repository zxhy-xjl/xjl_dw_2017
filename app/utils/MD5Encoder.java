package utils;

import java.security.MessageDigest;

public class MD5Encoder {
	
	private MD5Encoder() {
		
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
	
	public static String encode(String origin) {
		String resultString = null;
		byte[] originBytes = origin.getBytes();
		
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(originBytes));
		} catch (Exception ex) {

		}
		return resultString;
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static void main(String[] args) {
		String md5 = encode("123456");
		System.out.println(md5);
		play.Logger.debug(md5);
	}
	
}
