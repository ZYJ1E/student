package security;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
public final class AES {
    private static final String PSD = "OhiJBVd9Y4O58eoQwgHvCA=="; //����ʹ��create-Key������
    public static String encode(String s) {
        final var byteData = encrypt(s, stringToKey(PSD));
        return Base64.getEncoder().encodeToString(byteData);
    }

    public static String decode(String s) {
        final var byteData = Base64.getDecoder().decode(s);
        return decrypt(byteData, stringToKey(PSD));
    }
    /*private static Key createKey() {
        try {
            //������Կ��������ָ��ΪAES�㷨,�����ִ�Сд
            var keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); //����һ��128λ�����Դ,���ݴ�����ֽ�����

            final var secretKey = keyGenerator.generateKey(); //����ԭʼ�Գ���Կ
            final byte[] keyBytes = secretKey.getEncoded(); //���ԭʼ�Գ���Կ���ֽ�����

            return new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }*/
    private static byte[] encrypt(String context, Key key) {
        try {
            var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(context.getBytes());
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String decrypt(byte[] result, Key key) {
        try {
            var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // ��ʼ������������һ������Ϊ����(Encrypt_mode)���߽���(Decrypt_mode)�������ڶ�������Ϊʹ�õ�KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            result = cipher.doFinal(result);

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException e) {
            e.printStackTrace();
        }
        return new String(result);
    }
    public static Key stringToKey(String stringInBase64) {
        final var byteCode = Base64.getDecoder().decode(stringInBase64);
        return new SecretKeySpec(byteCode, 0, byteCode.length, "AES");
    }
   // public static String keyToString(Key key) {return Base64.getEncoder().encodeToString(key.getEncoded());}
}
