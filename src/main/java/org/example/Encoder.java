package org.example;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Encoder {
    PrivateKey priKey;
    PublicKey pubKey;
    public Encoder(final String _private_key_PKCS_8_base16,
                   final String _public_key_X509_base16) {
        try {
            final PKCS8EncodedKeySpec keySpecPriv = new PKCS8EncodedKeySpec(
                    base16Decoder(_private_key_PKCS_8_base16));
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.priKey = keyFactory.generatePrivate(keySpecPriv);
            final X509EncodedKeySpec keySpecPub = new X509EncodedKeySpec(
                    base16Decoder(_public_key_X509_base16));
            this.pubKey = keyFactory.generatePublic(keySpecPub);
        } catch (Exception ex) {
        }
    }
    private static void printarray(final byte[] data) {
        for (int i = 0; i < data.length; ++i) {
            //System.out.print(data[i]);
        }
        //System.out.println();
    }
    public static String base16Encoder(final byte[] digestMsgByte) {
        final StringBuffer verifyMsg = new StringBuffer();
        for (int i = 0; i < digestMsgByte.length; ++i) {
            final int hexChar = 0xFF & digestMsgByte[i];
            String hexString = Integer.toHexString(hexChar);
            if (hexString.length() == 1) {
                verifyMsg.append("0");
                verifyMsg.append(hexString);
            } else {
                verifyMsg.append(hexString);
            }
            hexString = null;
        }
        return verifyMsg.toString();
    }
    public static byte[] base16Decoder(final String hex) {
        final byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; ++i) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }

    public String getDualEncrypted(final String plaintext) throws Exception {
        byte[] msg = this.innerEncryptionV2(plaintext.getBytes("UTF-8"), this.priKey);
        msg = this.outerEncryption(msg, this.pubKey);
       //.println("EMBE=" + msg);
        String dd = new String(Base64.getEncoder().encode(msg));
        return dd;
    }
    private byte[] innerEncryptionV2(final byte[] message, final PrivateKey priKey) {
        final byte[][] sliced = this.getSlices(message);
        final byte[][] data = new byte[sliced.length][];
        //System.out.println("=======+=" + sliced.length);
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, priKey);
            for (int i = 0; i < sliced.length; ++i) {
                data[i] = cipher.doFinal(sliced[i]);
            }
            return this.combineV2(data);
        } catch (Exception e) {
// logger.error(e.getMessage(), e);
            return null;
        }
    }
    private byte[] outerEncryption(final byte[] message, final PublicKey pubKey) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, pubKey);
            final byte[][] data = this.getSlices(message);
            final byte[][] encrypted = new byte[data.length][];
            for (int i = 0; i < data.length; ++i) {
                encrypted[i] = cipher.doFinal(data[i]);
                //System.out.println("Length==" + encrypted[i].length);
            }
            return this.combine(encrypted);
        } catch (Exception e) {
// logger.error(e.getMessage(), e);
            return null;
        }
    }
    private byte[][] getSlices(final byte[] message) {
        printarray(message);
        final int full_count = message.length / 100;
        //System.out.println("Full Count =" + full_count);
        int half_count = 0;
        if (message.length % 100 > 0) {
            ++half_count;
        }
        //System.out.println("Half Count =" + half_count);
        final byte[][] data = new byte[full_count + half_count][];
        for (int i = 0; i < full_count; ++i) {
            final byte[] dest = new byte[100];
            System.arraycopy(message, i * 100, dest, 0, 100);
            data[i] = dest;
        }
        if (half_count == 1) {
            final byte[] dest2 = new byte[message.length - full_count * 100];
            System.arraycopy(message, full_count * 100, dest2, 0, dest2.length);
            data[full_count + half_count - 1] = dest2;
        }
        return data;
    }
    private byte[] combine(final byte[][] data) {
        int length = 0;
        for (int i = 0; i < data.length; ++i) {
            length += data[i].length;
        }
        final byte[] result = new byte[length];
        int currentIndex = 0;
        for (int j = 0; j < data.length; ++j) {
            System.arraycopy(data[j], 0, result, currentIndex, data[j].length);
            currentIndex += data[j].length;
        }
        return result;
    }
    private byte[] combineV2(final byte[][] data) throws Exception {

        int length = 0;
        for (int i = 0; i < data.length; ++i) {
            length += data[i].length;
        }
        //System.out.println("Length=" + length);
        final byte[] result = new byte[length + 1];
        int currentIndex = 1;
        result[0] = 42;
        //System.out.println(result[0]);
        for (int j = 0; j < data.length; ++j) {
            System.arraycopy(data[j], 0, result, currentIndex, data[j].length);
            currentIndex += data[j].length;
        }
        return result;
    }
    private String padSpacesToRight(String temp) {
        while (temp.length() < 10) {
            temp = String.valueOf(temp) + " ";
        }
        return temp;
    }
//    public String encryptText(String msg, PrivateKey key) throws NoSuchAlgorithmException,
//            NoSuchPaddingException,
//            UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException,
//            InvalidKeyException {
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
//    }
}
