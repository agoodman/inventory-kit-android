/*
 * Created on Dec 4, 2011
 *
 */
package com.migrant.ik;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public final class SHA1 {

	public static final String hexDigest(String src) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			Log.e("SHA1",e.getLocalizedMessage());
		}
		byte[] sha1hash = new byte[40];
		try {
			md.update(src.getBytes("UTF-8"), 0, src.length());
		} catch (UnsupportedEncodingException e) {
			Log.e("SHA1",e.getLocalizedMessage());
		}
		sha1hash = md.digest();

		StringBuilder tDigest = new StringBuilder();
		for (int i = 0; i < sha1hash.length; i++) {
			String hex = Integer.toHexString(sha1hash[i]);
			if (hex.length() == 1)
				hex = "0" + hex;
			hex = hex.substring(hex.length() - 2);
			tDigest.append(hex);
		}

		return tDigest.toString();
//		return convertToHex(sha1hash);
	}

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

}
