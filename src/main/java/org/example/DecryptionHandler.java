package org.example;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Logger;

public class DecryptionHandler {

    private static Logger logger = Logger.getLogger("DecryptionHandler");
    PrivateKey priKey;
    PublicKey pubKey;

    public DecryptionHandler(final String privateKey, final String publicKey) {
        try {
            final PKCS8EncodedKeySpec keySpecPriv = new
                    PKCS8EncodedKeySpec(base16Decoder(privateKey));
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.priKey = keyFactory.generatePrivate(keySpecPriv);
            final X509EncodedKeySpec keySpecPub = new
                    X509EncodedKeySpec(base16Decoder(publicKey));
            this.pubKey = keyFactory.generatePublic(keySpecPub);
        } catch (Exception e) {
            // logger.error("SSO Security Facade Exception");
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
            String
                    hexString = Integer.toHexString(hexChar);
            if
            (hexString.length() == 1) {
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

    public String validate(String inerOuter_encrypted_message_base64) throws Exception {
        inerOuter_encrypted_message_base64 = inerOuter_encrypted_message_base64.trim();
        //System.out.println("uu=" + inerOuter_encrypted_message_base64);
        byte[] data = Base64.getDecoder().decode(inerOuter_encrypted_message_base64);
        //System.out.println("uuD=" + data);
        final byte[] data2 = this.openOuterEncryption(data, this.priKey);
        final byte[] data3 = this.openInnerEncryption(data2, this.pubKey);
        final String message = new String(data3, "UTF-8");
       // logger.info("Message===" + message);
        return message;
    }

    private byte[][] getSlices(final byte[] message) {
        final int full_count = message.length / 128;
        //System.out.println("Full Count =" + full_count);
        int half_count = 0;
        if (message.length % 128 > 0) {
            ++half_count;
        }
        //System.out.println("Half Count =" + half_count);
        final byte[][] data = new byte[full_count + half_count][];
        for (int i = 0; i < full_count; ++i) {
            final byte[] dest = new byte[128];
            System.arraycopy(message, i * 128, dest, 0, 128);
            data[i] = dest;
        }
        if (half_count == 1) {
            final byte[] dest2 = new byte[message.length - full_count * 128];
            System.arraycopy(message, full_count * 128, dest2, 0, dest2.length);
            data[full_count + half_count - 1] = dest2;

        }
        return data;
    }

    private byte[] openOuterEncryption(final byte[] message, final PrivateKey priKey) {
        final byte[][] data = this.getSlices(message);
        final byte[][] decrypted = new byte[data.length][];
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, priKey);
            for (int i = 0; i < data.length; ++i) {
                decrypted[i] = cipher.doFinal(data[i]);
            }
            return this.combine(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("Open Outer Encryption Exception");
            return null;
        }
    }

    private byte[] openInnerEncryption(final byte[] message, final PublicKey pubKey) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, pubKey);
            //System.out.println("====" + new String(message, "US-ASCII").substring(0, 0));
            //System.out.println("----" + message[0]);
            if (message[0] == 42) {
                final byte[] message2 = new byte[message.length - 1];
                System.arraycopy(message, 1, message2, 0, message2.length);
                final byte[][] data = this.getSlices(message2);
                final byte[][] decrypted = new byte[data.length][];
                for (int i = 0; i < data.length; ++i) {
                    decrypted[i] = cipher.doFinal(data[i]);
                }
                return this.combine(decrypted);
            }
            return cipher.doFinal(message);
        } catch (Exception e) {
            // logger.error("Open Inner Encryption Exception",e);
            return null;
        }
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
}