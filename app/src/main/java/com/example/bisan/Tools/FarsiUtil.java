package com.example.bisan.Tools;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahab-pc on 12/17/2017.
 */

public final class FarsiUtil {

    /*************************************
     *
     * CLASS :: Used to Convert English Strings to Persian Strings and Upside Down.
     *
     * Two Important Method :
     * -------------------
     * 1- ConvertToFarsi : that Convert English String to Persian String.
     * 2- convertFarsiToEnglish : that Convert Persian String to English String.
     *
     ************************************/

    public static String ReplaceQoute(String str){
        return str.replaceAll("^\"|\"$", "");
    }

    public static String ConvertDoubleToToman(String engNum){
        String an="0";
        try{
            int index = engNum.indexOf('.');
            if (index < 0) index = engNum.length();
            if (index < 0) index = 0;
            an=FarsiUtil.convertToFarsi(FarsiUtil.ConvetPriceToToman(engNum.substring(0, index)));

        }catch (Exception e){}
        return an;
    }

    public static String EncodeBase64(String str){
        // Sending side
        try {
            byte[] data = str.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.DEFAULT);
        }catch (Exception e){
            return "";
        }
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static byte[] SHA1_byte(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return sha1hash;
    }


    public static String ConvetPriceToToman(String num){
        if(num.length()<=3){
            return num;
        }else{
            String x=num.substring(num.length()-3,num.length());
            num=num.substring(0,num.length()-3);
            return ConvetPriceToToman(num)+","+x;
        }
    }

        public static boolean isFarsiConversionNeeded = true;

        private final static String szLamAndAlef = Character
                .toString((char) 0xfedf)
                + Character.toString((char) 0xfe8e); // Lam + Alef

        private final static String szLamStickAndAlef = Character
                .toString((char) 0xfee0)
                + Character.toString((char) 0xfe8e); // Lam (Sticky !!!)+
        // Alef

        private final static String szLa = Character.toString((char) 0xfefb); // La
        private final static String szLaStick = Character.toString((char) 0xfefc); // La
        // (Sticky!!!)

        private final static String szLamAndAlefWoosim = Character
                .toString((char) 0xe1)
                + Character.toString((char) 0xbb); // Lam + Alef

        private final static String szLamStickAndAlefWoosim = Character
                .toString((char) 0x90)
                + Character.toString((char) 0xbb); // Lam (Sticky !!!)+
        // Alef

        private final static String szLaWoosim = Character.toString((char) 0xd9); // La
        private final static String szLaStickWoosim = Character
                .toString((char) 0xd9); // La

        // (Sticky!!!)

        private static final class struc {
            public char character;
            public char endGlyph;
            public char iniGlyph;
            public char midGlyph;
            public char isoGlyph;

            public struc(char Character, char EndGlyph, char IniGlyph,
                         char MidGlyph, char IsoGlyph) {
                character = Character;
                endGlyph = EndGlyph;
                iniGlyph = IniGlyph;
                midGlyph = MidGlyph;
                isoGlyph = IsoGlyph;
            }
        }

        static struc[] arrStruc = {
                new struc((char) 0x630, (char) 0xfeac, (char) 0xfeab,
                        (char) 0xfeac, (char) 0xfeab),
                new struc((char) 0x62f, (char) 0xfeaa, (char) 0xfea9,
                        (char) 0xfeaa, (char) 0xfea9),
                new struc((char) 0x62c, (char) 0xfe9e, (char) 0xfe9f,
                        (char) 0xfea0, (char) 0xfe9d),
                new struc((char) 0x62d, (char) 0xfea2, (char) 0xfea3,
                        (char) 0xfea4, (char) 0xfea1),
                new struc((char) 0x62e, (char) 0xfea6, (char) 0xfea7,
                        (char) 0xfea8, (char) 0xfea5),
                new struc((char) 0x647, (char) 0xfeea, (char) 0xfeeb,
                        (char) 0xfeec, (char) 0xfee9),
                new struc((char) 0x639, (char) 0xfeca, (char) 0xfecb,
                        (char) 0xfecc, (char) 0xfec9),
                new struc((char) 0x63a, (char) 0xfece, (char) 0xfecf,
                        (char) 0xfed0, (char) 0xfecd),
                new struc((char) 0x641, (char) 0xfed2, (char) 0xfed3,
                        (char) 0xfed4, (char) 0xfed1),
                new struc((char) 0x642, (char) 0xfed6, (char) 0xfed7,
                        (char) 0xfed8, (char) 0xfed5),
                new struc((char) 0x62b, (char) 0xfe9a, (char) 0xfe9b,
                        (char) 0xfe9c, (char) 0xfe99),
                new struc((char) 0x635, (char) 0xfeba, (char) 0xfebb,
                        (char) 0xfebc, (char) 0xfeb9),
                new struc((char) 0x636, (char) 0xfebe, (char) 0xfebf,
                        (char) 0xfec0, (char) 0xfebd),
                new struc((char) 0x637, (char) 0xfec2, (char) 0xfec3,
                        (char) 0xfec4, (char) 0xfec1),
                new struc((char) 0x643, (char) 0xfeda, (char) 0xfedb,
                        (char) 0xfedc, (char) 0xfed9),
                new struc((char) 0x645, (char) 0xfee2, (char) 0xfee3,
                        (char) 0xfee4, (char) 0xfee1),
                new struc((char) 0x646, (char) 0xfee6, (char) 0xfee7,
                        (char) 0xfee8, (char) 0xfee5),
                new struc((char) 0x62a, (char) 0xfe96, (char) 0xfe97,
                        (char) 0xfe98, (char) 0xfe95),
                new struc((char) 0x627, (char) 0xfe8e, (char) 0xfe8d,
                        (char) 0xfe8e, (char) 0xfe8d),
                new struc((char) 0x644, (char) 0xfede, (char) 0xfedf,
                        (char) 0xfee0, (char) 0xfedd),
                new struc((char) 0x628, (char) 0xfe90, (char) 0xfe91,
                        (char) 0xfe92, (char) 0xfe8f),
                new struc((char) 0x64a, (char) 0xfef2, (char) 0xfef3,
                        (char) 0xfef4, (char) 0xfef1),
                new struc((char) 0x633, (char) 0xfeb2, (char) 0xfeb3,
                        (char) 0xfeb4, (char) 0xfeb1),
                new struc((char) 0x634, (char) 0xfeb6, (char) 0xfeb7,
                        (char) 0xfeb8, (char) 0xfeb5),
                new struc((char) 0x638, (char) 0xfec6, (char) 0xfec7,
                        (char) 0xfec8, (char) 0xfec5),
                new struc((char) 0x632, (char) 0xfeb0, (char) 0xfeaf,
                        (char) 0xfeb0, (char) 0xfeaf),
                new struc((char) 0x648, (char) 0xfeee, (char) 0xfeed,
                        (char) 0xfeee, (char) 0xfeed),
                new struc((char) 0x629, (char) 0xfe94, (char) 0xfe93,
                        (char) 0xfe93, (char) 0xfe93),
                new struc((char) 0x649, (char) 0xfef0, (char) 0xfeef,
                        (char) 0xfef0, (char) 0xfeef),
                new struc((char) 0x631, (char) 0xfeae, (char) 0xfead,
                        (char) 0xfeae, (char) 0xfead),
                new struc((char) 0x624, (char) 0xfe86, (char) 0xfe85,
                        (char) 0xfe86, (char) 0xfe85),
                new struc((char) 0x621, (char) 0xfe80, (char) 0xfe80,
                        (char) 0xfe80, (char) 0xfe80),
                new struc((char) 0x626, (char) 0xfe8a, (char) 0xfe8b,
                        (char) 0xfe8c, (char) 0xfe89),
                new struc((char) 0x623, (char) 0xfe84, (char) 0xfe83,
                        (char) 0xfe84, (char) 0xfe83),
                new struc((char) 0x622, (char) 0xfe82, (char) 0xfe81,
                        (char) 0xfe82, (char) 0xfe81),
                new struc((char) 0x625, (char) 0xfe88, (char) 0xfe87,
                        (char) 0xfe88, (char) 0xfe87),
                new struc((char) 0x67e, (char) 0xfb57, (char) 0xfb58,
                        (char) 0xfb59, (char) 0xfb56), // peh
                new struc((char) 0x686, (char) 0xfb7b, (char) 0xfb7c,
                        (char) 0xfb7d, (char) 0xfb7a), // cheh
                new struc((char) 0x698, (char) 0xfb8b, (char) 0xfb8a,
                        (char) 0xfb8b, (char) 0xfb8a), // jeh
                new struc((char) 0x6a9, (char) 0xfb8f, (char) 0xfb90,
                        (char) 0xfb91, (char) 0xfb8e), // keheh
                new struc((char) 0x6af, (char) 0xfb93, (char) 0xfb94,
                        (char) 0xfb95, (char) 0xfb92), // gaf
                // new struc((char) 0x6cc, (char) 0xfbfd, (char) 0xfbfe,
                // (char) 0xfbff, (char) 0xfbfc), // Farsi yeh
                new struc((char) 0x6cc, (char) 0xfbfd, (char) 0xfef3,
                        (char) 0xfef4, (char) 0xfbfc), // Arabic yeh
                new struc((char) 0x6c0, (char) 0xfba5, (char) 0xfba4,
                        (char) 0xfba5, (char) 0xfba4) // heh with yeh
        };

        static struc[] arrStrucWoosim = {
                new struc((char) 0x630, (char) 0xb5, (char) 0x82, (char) 0xb5,
                        (char) 0x82),
                new struc((char) 0x62f, (char) 0xb4, (char) 0x81, (char) 0xb4,
                        (char) 0x81),
                new struc((char) 0x62c, (char) 0x9b, (char) 0xb1, (char) 0xf9,
                        (char) 0xbf),
                new struc((char) 0x62d, (char) 0x9c, (char) 0xb2, (char) 0xfa,
                        (char) 0xc0),
                new struc((char) 0x62e, (char) 0x9d, (char) 0xb3, (char) 0xfe,
                        (char) 0xc1),
                new struc((char) 0x647, (char) 0xac, (char) 0xe4, (char) 0x93,
                        (char) 0xd5),
                new struc((char) 0x639, (char) 0xc9, (char) 0xd3, (char) 0x8b,
                        (char) 0xa4),
                new struc((char) 0x63a, (char) 0xca, (char) 0xdd, (char) 0x8c,
                        (char) 0xa5),
                new struc((char) 0x641, (char) 0xa6, (char) 0xde, (char) 0x8d,
                        (char) 0xcc),
                new struc((char) 0x642, (char) 0xa7, (char) 0xdf, (char) 0x8e,
                        (char) 0xce),
                new struc((char) 0x62b, (char) 0xbd, (char) 0xaf, (char) 0xea,
                        (char) 0x99),
                new struc((char) 0x635, (char) 0xc4, (char) 0xc8, (char) 0x87,
                        (char) 0xa0),
                new struc((char) 0x636, (char) 0xc5, (char) 0xcb, (char) 0x88,
                        (char) 0xa1),
                new struc((char) 0x637, (char) 0xc6, (char) 0xcd, (char) 0xcd,
                        (char) 0xa2),
                new struc((char) 0x643, (char) 0xcf, (char) 0xe0, (char) 0x8f,
                        (char) 0xa8),
                new struc((char) 0x645, (char) 0xd2, (char) 0xe2, (char) 0x91,
                        (char) 0xaa),
                new struc((char) 0x646, (char) 0xd4, (char) 0xe3, (char) 0x92,
                        (char) 0xab),
                new struc((char) 0x62a, (char) 0xbd, (char) 0xaf, (char) 0xea,
                        (char) 0x99),
                new struc((char) 0x627, (char) 0xbb, (char) 0x80, (char) 0xbb,
                        (char) 0x80),
                new struc((char) 0x644, (char) 0xd1, (char) 0xe1, (char) 0x90,
                        (char) 0xa9),
                new struc((char) 0x628, (char) 0xbc, (char) 0xae, (char) 0xe9,
                        (char) 0x98),
                new struc((char) 0x64a, (char) 0xdc, (char) 0xe6, (char) 0x95,
                        (char) 0xdc),
                new struc((char) 0x633, (char) 0xc2, (char) 0xb8, (char) 0xb8,
                        (char) 0x9e),
                new struc((char) 0x634, (char) 0xc3, (char) 0xb9, (char) 0xb9,
                        (char) 0x9f),
                new struc((char) 0x638, (char) 0xc7, (char) 0xcd, (char) 0xcd,
                        (char) 0xc7),
                new struc((char) 0x632, (char) 0xb7, (char) 0xb7, (char) 0xb7,
                        (char) 0xb7),
                new struc((char) 0x648, (char) 0x94, (char) 0x94, (char) 0x94,
                        (char) 0x94),
                new struc((char) 0x629, (char) 0xda, (char) 0xda, (char) 0xda,
                        (char) 0xda),
                new struc((char) 0x649, (char) 0xdc, (char) 0xe6, (char) 0x95,
                        (char) 0xdc),
                new struc((char) 0x631, (char) 0xb6, (char) 0xb6, (char) 0xb6,
                        (char) 0xb6),
                new struc((char) 0x624, (char) 0xe7, (char) 0xe7, (char) 0xe7,
                        (char) 0xe7),
                new struc((char) 0x621, (char) 0xba, (char) 0xba, (char) 0xba,
                        (char) 0xba),
                new struc((char) 0x626, (char) 0xd7, (char) 0xe8, (char) 0x97,
                        (char) 0xd7),
                new struc((char) 0x623, (char) 0x80, (char) 0x80, (char) 0x80,
                        (char) 0x80),
                new struc((char) 0x622, (char) 0x80, (char) 0x80, (char) 0x80,
                        (char) 0x80),
                new struc((char) 0x625, (char) 0x80, (char) 0x80, (char) 0x80,
                        (char) 0x80),
                new struc((char) 0x67e, (char) 0xbc, (char) 0xae, (char) 0xe9,
                        (char) 0x98), // peh
                new struc((char) 0x686, (char) 0x9b, (char) 0xb1, (char) 0xf9,
                        (char) 0xbf), // cheh
                new struc((char) 0x698, (char) 0xb7, (char) 0xb7, (char) 0xb7,
                        (char) 0xb7), // jeh
                new struc((char) 0x6a9, (char) 0xcf, (char) 0xe0, (char) 0x8f,
                        (char) 0xa8), // keheh
                new struc((char) 0x6af, (char) 0xcf, (char) 0xe0, (char) 0x8f,
                        (char) 0xa8), // gaf
                new struc((char) 0x6cc, (char) 0xdc, (char) 0xe6, (char) 0x95,
                        (char) 0xdc), // yeh
                new struc((char) 0x6c0, (char) 0xac, (char) 0xe4, (char) 0x93,
                        (char) 0xd5) // heh with yeh
        };

        private static final int N_DISTINCT_CHARACTERS = 43;

        private static final String ArabicReverse(String s) {
            try {
                String Out = "", rev;
                s = MakeReverse(s);
                char[] chs = new char[s.length()];
                chs = s.toCharArray();
                int i = 0;
                while (i < s.length()) {
                    if ((chs[i] >= '0' && chs[i] <= '9')) // isDigit(s[i]) ?
                    {
                        rev = "";
                        while (i < s.length()
                                && ((chs[i] >= '0' && chs[i] <= '9') || chs[i] == '/')) // isDigit(s[i])
                        // ?
                        {
                            rev = rev + chs[i];
                            ++i;
                        }
                        rev = MakeReverse(rev);
                        Out = Out + rev;
                    } else {
                        Out = Out + chs[i];
                        ++i;
                    }
                }
                s = Out;
            } catch (Exception ex) {
                // throw new Exception(
                // "An exception has occurred in ArabicReverse function.\\n"
                // + ex.getMessage());
            }
            return s;
        }

        private static final boolean isFromTheSet1(/* WCHAR */char ch) {
            char[] theSet1 = new char[] { (char) 0x62c, (char) 0x62d, (char) 0x62e,
                    (char) 0x647, (char) 0x639, (char) 0x63a, (char) 0x641,
                    (char) 0x642, (char) 0x62b, (char) 0x635, (char) 0x636,
                    (char) 0x637, (char) 0x643, (char) 0x645, (char) 0x646,
                    (char) 0x62a, (char) 0x644, (char) 0x628, (char) 0x64a,
                    (char) 0x633, (char) 0x634, (char) 0x638, (char) 0x67e,
                    (char) 0x686, (char) 0x6a9, (char) 0x6af, (char) 0x6cc,
                    (char) 0x626 };
            int i = 0;
            while (i < 28) {
                if (ch == theSet1[i])
                    return true;
                ++i;
            }
            return false;
        }

        private static final boolean isFromTheSet2(/* WCHAR */char ch) {
            char[] theSet2 = new char[] { (char) 0x627, (char) 0x623, (char) 0x625,
                    (char) 0x622, (char) 0x62f, (char) 0x630, (char) 0x631,
                    (char) 0x632, (char) 0x648, (char) 0x624, (char) 0x629,
                    (char) 0x649, (char) 0x698, (char) 0x6c0 };
            int i = 0;
            while (i < 14) {
                if (ch == theSet2[i])
                    return true;
                ++i;
            }
            return false;
        }

        private static final String MakeReverse(String text) {
            String Result = "";
            char[] Ctext = new char[text.length()];
            Ctext = text.toCharArray();
            for (int i = (text.length()) - 1; i >= 0; i--) {
                Result += Ctext[i];
            }
            return Result;
        }

        public static final String ConvertBackToRealFarsi(String In) {

            if (!isFarsiConversionNeeded) {
                return In;
            }

            String strOut = "";
            StringBuilder strBuilder = new StringBuilder("");
            int i = 0;
            int j = 0;
            char[] chIn = new char[In.length()];
            chIn = In.toCharArray();

            for (i = 0; i < In.length(); i++) {
                boolean found = false;
                for (j = 0; j < arrStruc.length; j++) {
                    if (chIn[i] == arrStruc[j].midGlyph ||
                            chIn[i] == arrStruc[j].iniGlyph ||
                            chIn[i] == arrStruc[j].endGlyph ||
                            chIn[i] == arrStruc[j].isoGlyph) {
                        strBuilder.append(arrStruc[j].character);
                        found = true;
                        break;
                    }
                }
                if (!found)
                    strBuilder.append(chIn[i]);
            }

            strOut = strBuilder.toString();
            strOut = strOut.replace(szLa, "لا");
            strOut = strOut.replace(szLaStick, "لا");

            return strOut;
        }

        public static final String Convert(String In) {

            if (!isFarsiConversionNeeded) {
                return In;
            }

            if (In == null) {
                return "";
            }

            boolean linkBefore, linkAfter;
            String Out = In;
            char[] chOut = new char[Out.length()];
            chOut = Out.toCharArray();
            char[] chIn = new char[In.length()];
            chIn = In.toCharArray();

            for (int i = 0; i < In.length(); i++) {
            /* WCHAR */
                char ch = chIn[i];
                if ((ch >= (char) 0x0621 && ch <= (char) 0x064a)
                        || (ch == (char) 0x067e) || (ch == (char) 0x0686)
                        || (ch == (char) 0x0698) || (ch == (char) 0x06a9)
                        || (ch == (char) 0x06af) || (ch == (char) 0x06cc)
                        || (ch == (char) 0x06c0)) // is a Farsi character?
                {
                    int idx = 0;
                    while (idx < N_DISTINCT_CHARACTERS) {
                        if (arrStruc[idx].character == chIn[i])
                            break;
                        ++idx;
                    }

                    if (i == In.length() - 1)
                        linkAfter = false;
                    else
                        linkAfter = (isFromTheSet1(chIn[i + 1]) || isFromTheSet2(chIn[i + 1]));
                    if (i == 0)
                        linkBefore = false;
                    else
                        linkBefore = isFromTheSet1(chIn[i - 1]);
                    if (idx < N_DISTINCT_CHARACTERS) {
                        if (linkBefore && linkAfter)
                            chOut[i] = arrStruc[idx].midGlyph;
                        if (linkBefore && !linkAfter)
                            chOut[i] = arrStruc[idx].endGlyph;
                        if (!linkBefore && linkAfter)
                            chOut[i] = arrStruc[idx].iniGlyph;
                        if (!linkBefore && !linkAfter)
                            chOut[i] = arrStruc[idx].isoGlyph;
                    } else {
                        chOut[i] = chIn[i];
                    }
                } else {
                    chOut[i] = chIn[i];
                }
            }
            Out = "";
            for (int j = 0; j < chOut.length; j++)
                Out += chOut[j];
            // Out = ArabicReverse(Out);

            Out = Out.replace((char) 0x200c, ' '); // Change NO SPACE to SPACE

            Out = Out.replace(szLamAndAlef, szLa); // Join 'Lam' and 'Alef' and
            // make 'La'
            Out = Out.replace(szLamStickAndAlef, szLaStick); // Join 'Lam Stick'
            // and 'Alef'
            // and make 'La
            // Stick'

            return reorderWords(Out);

        }

        private final static String reorderWords(String strIn)
        {

            final int ST_RTL = 0;
            final int ST_LTR = 1;

            String strOut = "";
            String prevWord = "";
            int state = ST_RTL;
            char[] arr = strIn.toCharArray();
            int i = 0;
            while (i < arr.length) {
                if (charIsLTR(arr[i]) && state != ST_LTR)
                {
                    // state changed to LTR
                    state = ST_LTR;
                    strOut = prevWord + strOut;
                    prevWord = "";
                    prevWord += arr[i];
                }
                else if (charIsRTL(arr[i]) && state != ST_RTL)
                {
                    // state changed to RTL
                    state = ST_RTL;
                    strOut = prevWord + strOut;
                    prevWord = "";
                    prevWord += arr[i];
                }
                else
                {
                    // state is not changed
                    prevWord += arr[i];
                }
                i++;
            }

            strOut = prevWord + strOut;

            return strOut;

        }

        private final static boolean charIsLTR(char ch)
        {
            return (ch >= (char) 65 & ch <= (char) 122)
                    |
                    Character.isDigit(ch);
        }

        private final static boolean charIsRTL(char ch)
        {
            return ch >= (char) 0x0621;
        }

        private static Typeface typeface;

        public static final Typeface GetFarsiFont(Context context) {
            if (typeface == null) {
                typeface = Typeface.createFromAsset(context.getAssets(),
                        // "DroidSansFallback.ttf");
                        "fonts/IRANSans.ttf");
            }
            return typeface;
        }

    public static String convertToFarsi(String num){
        try{
            char[] arabicChars = {'٠','١','٢','٣','٤','٥','٦','٧','٨','٩'};
            StringBuilder builder = new StringBuilder();
            String str=num;
            for(int i =0;i<str.length();i++)
            {
                if(Character.isDigit(str.charAt(i)))
                {
                    int index=(int)(str.charAt(i))-48;
                    if(index>11){
                        builder.append(str.charAt(i));
                    }else {
                        builder.append(arabicChars[(int) (str.charAt(i)) - 48]);
                    }
                }
                else
                {
                    builder.append(str.charAt(i));
                }
            }
            return builder.toString();
        }catch (Exception e){
            Log.d("err",e.toString());
            return num;
        }
    }

    public static String convertFarsiToEnglish(String num){
        try{
            String newValue =   (((((((((((num).replaceAll( "١","1")).replaceAll( "٢","2")).replaceAll( "٣","3")).replaceAll( "٤","4")).replaceAll( "٥","5")).replaceAll( "٦","6")).replaceAll( "٧","7")).replaceAll( "٨","8")).replaceAll( "٩","9")).replaceAll( "٠","0"));
            return newValue;
        }catch (Exception e){
            Log.d("err",e.toString());
            return num;
        }
    }


    public static boolean isNetworkConnected(Context context) { // check internet connection
        boolean ans=false;
        ans=isConnected(context);
        if(!ans){ans=isConnectedWifi(context);}
        if(!ans){ans=isConnectedMobile(context);}
        if(!ans){ans=isConnectedFast(context);}
        return ans;
    }

    /**
     * Get the network info
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
			/*
			 * Above API level 7, make sure to set android:targetSdkVersion
			 * to appropriate level to use these
			 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }

    public static boolean StringContains(String basetxt,String searchtxt){
        boolean answer=false;
        try{
            List<String> matches = new ArrayList<>();
            int index=-1;
            int cnt=0;
            String temp=searchtxt;
            index=temp.indexOf(" ");
            while(index>=0){
                String part=temp.substring(0,index);
                temp=temp.substring(index+1);
                matches.add(part);
                cnt++;
                index=temp.indexOf(" ");
            }
            if(temp.length()>0)matches.add(temp);

            for (String s : matches)
            {
                if (basetxt.toLowerCase().contains(s))
                {
                    answer=true;
                    break;
                }
            }
        }catch (Exception e){}
        return answer;
    }



}


