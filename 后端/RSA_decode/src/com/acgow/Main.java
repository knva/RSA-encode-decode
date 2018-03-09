package com.acgow;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * RSA算法，实现数据的加密解密。
 */
public class Main {
    private static Cipher cipher;

    static{
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成密钥对
     * @param filePath 生成密钥的路径
     * @return
     */
    public static Map<String,String> generateKeyPair(String filePath){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 密钥位数
            keyPairGen.initialize(2048);
            // 密钥对
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 公钥
            PublicKey publicKey = keyPair.getPublic();
            // 私钥
            PrivateKey privateKey = keyPair.getPrivate();
            //得到公钥字符串
            String publicKeyString = getKeyString(publicKey);
            //得到私钥字符串
            String privateKeyString = getKeyString(privateKey);
            //将密钥对写入到文件
            FileWriter pubfw = new FileWriter(filePath + "/publicKey.keystore");
            FileWriter prifw = new FileWriter(filePath + "/privateKey.keystore");
            BufferedWriter pubbw = new BufferedWriter(pubfw);
            BufferedWriter pribw = new BufferedWriter(prifw);
            pubbw.write(publicKeyString);
            pribw.write(privateKeyString);
            pubbw.flush();
            pubbw.close();
            pubfw.close();
            pribw.flush();
            pribw.close();
            prifw.close();
            //将生成的密钥对返回
            Map<String,String> map = new HashMap<String,String>();
            map.put("publicKey", publicKeyString);
            map.put("privateKey", privateKeyString);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到公钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到私钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 得到密钥字符串（经过base64编码）
     *
     * @return
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = (new BASE64Encoder()).encode(keyBytes);
        return s;
    }

    /**
     * 使用公钥对明文进行加密，返回BASE64编码的字符串
     * @param publicKey
     * @param plainText
     * @return
     */
    public static String encrypt(PublicKey publicKey, String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return (new BASE64Encoder()).encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用keystore对明文进行加密
     * @param publicKeystore 公钥文件路径
     * @param plainText      明文
     * @return
     */
    public static String fileEncrypt(String publicKeystore, String plainText){
        try {
            FileReader fr = new FileReader(publicKeystore);
            BufferedReader br = new BufferedReader(fr);
            String publicKeyString="";
            String str;
            while((str=br.readLine())!=null){
                publicKeyString+=str;
            }
            br.close();
            fr.close();
            cipher.init(Cipher.ENCRYPT_MODE,getPublicKey(publicKeyString));
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return (new BASE64Encoder()).encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用公钥对明文进行加密
     * @param publicKey      公钥
     * @param plainText      明文
     * @return
     */
    public static String encrypt(String publicKey, String plainText){
        try {
            cipher.init(Cipher.ENCRYPT_MODE,getPublicKey(publicKey));
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return (new BASE64Encoder()).encode(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥对明文密文进行解密
     * @param privateKey
     * @param enStr
     * @return
     */
    public static String decrypt(PrivateKey privateKey, String enStr){
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥对密文进行解密
     * @param privateKey       私钥
     * @param enStr            密文
     * @return
     */
    public static String decrypt(String privateKey, String enStr){
        try {
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用keystore对密文进行解密
     * @param privateKeystore  私钥路径
     * @param enStr            密文
     * @return
     */
    public static String fileDecrypt(String privateKeystore, String enStr){
        try {
            FileReader fr = new FileReader(privateKeystore);
            BufferedReader br = new BufferedReader(fr);
            String privateKeyString="";
            String str;
            while((str=br.readLine())!=null){
                privateKeyString+=str;
            }
            br.close();
            fr.close();
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyString));
            byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(deBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        String publicKey;
        String privateKey;

        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUcqmgU6L7hANx5vc4t92h/57JWY9s161XuNqgj0JkgdnXh2frZsymaYHfEHcglmXTSoIywRfJqjXRRj1/hMz/l1n22I7bfHEdygPPQWtqQilrV852V5GXPCnc6b25EQnRVYg9IrS00QKajWvdz6jyHsHxGbRlENUassYM5+9mWwIDAQAB";
        privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANRyqaBTovuEA3Hm9zi33aH/nslZj2zXrVe42qCPQmSB2deHZ+tmzKZpgd8QdyCWZdNKgjLBF8mqNdFGPX+EzP+XWfbYjtt8cR3KA89Ba2pCKWtXznZXkZc8KdzpvbkRCdFViD0itLTRApqNa93PqPIewfEZtGUQ1Rqyxgzn72ZbAgMBAAECgYA1WQnBad8ue4sF6jLAemNcT71G4IeElHBB6/hygybv6C+U0LrGwQy46RuksRTJCRtOwJILPrPDf0t+Xr4IrIdxXfgD/WjPtDfX3Re7qtMItJPkvvJXKbx7RFwom5I1jb+BYfVxWXMmRfn8U91Lx9Pt2iq+JmtjcQ2PlF5NmqtrUQJBAPa0Nz6Sut6d2D6JO/tv/UBPobh54/tzRCvr6VssL1CjanLSW84Yl4w67KvoBXR0JPLCgnJYqr8rXkB5vniPi+kCQQDcdACCP/7AVbhss0uJk4Vnk1yoQgsOJK/LGxtuKznzl8YMTyFME9nNOwEfKwcVaZJCzwiXIBbmTDCeEtgWeSmjAkEAna1bbcvcUfJyxq1xv+e45oS+6ShGtWzbknLqqBIaf6CipZabhKMlIUR8Bfd6nQ6qmtoFA851+09doznsqiOdGQJAdr+tvnuGWEhUbYku+U6Tn7VLRf89QUMVxow1fGSgdMyei+bcAsT2n4xXEFj3GduiQ4aOFAnfC/KihaOU7pYZjQJAI50P1Up4At2a3KYAmlb+MLymTwPlXaHZzXMfENSKaBd9OAwHvVl49XpX8K2aqDuqgQaw7HInMLxf7xYuV1GR2Q==";

        System.err.println("公钥加密——私钥解密");
        String source = "helloworld";
        System.out.println("\r加密前文字：\r\n" + source);
        String aData = Main.encrypt(publicKey, source);
        System.out.println("加密后文字：\r\n" + aData);
        String dData = Main.decrypt(privateKey, aData);
        System.out.println("解密后文字: \r\n" + dData);
    }
}