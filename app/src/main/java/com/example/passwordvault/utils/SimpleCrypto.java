package com.example.passwordvault.utils;
import android.util.Base64;

public class SimpleCrypto {
    private static final String KEY = "student-demo-key";
    public static String encrypt(String text) {
        if (text == null) return "";
        char[] out = new char[text.length()];
        for (int i=0;i<text.length();i++) out[i]=(char)(text.charAt(i)^KEY.charAt(i%KEY.length()));
        return Base64.encodeToString(new String(out).getBytes(), Base64.NO_WRAP);
    }
    public static String decrypt(String data) {
        try {
            String text = new String(Base64.decode(data, Base64.NO_WRAP));
            char[] out = new char[text.length()];
            for (int i=0;i<text.length();i++) out[i]=(char)(text.charAt(i)^KEY.charAt(i%KEY.length()));
            return new String(out);
        } catch(Exception e){ return ""; }
    }
}
