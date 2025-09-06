package io.github.wisely.starter.core.encry.helper;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * DESHelper
 */
public class DESHelper {
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "DESede";

    private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/ISO10126Padding";

    private static final int KEY_SIZE = 168;

    /**
     * 初始化密钥
     *
     * @param seed 密钥种子
     * @return byte[] 密钥
     */
    public static Key initSecretKey(String seed) throws Exception {
        //返回生成指定算法的秘密密钥的 KeyGenerator 对象
        KeyGenerator keyPairGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        //初始化此密钥生成器，使其具有确定的密钥大小
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed.getBytes());
        keyPairGen.init(KEY_SIZE, secureRandom);
        //生成一个密钥
        SecretKey secretKey = keyPairGen.generateKey();
        //实例化DES密钥规则
        DESedeKeySpec dks = new DESedeKeySpec(secretKey.getEncoded());
        //实例化密钥工厂
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        //生成密钥
        return skf.generateSecret(dks);
    }

    /**
     * 转换密钥
     *
     * @param seed 二进制密钥
     * @return Key 密钥
     */
    public static Key getSecretKeySpec(String seed) {
        byte[] keyByte = seed.getBytes();
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        return new SecretKeySpec(byteTemp, "DES");
    }


    /**
     * 获取cipher对象
     *
     * @param cipherAlgorithm 工作模式
     * @param mode            加/解密
     *                        Cipher.ENCRYPT_MODE
     *                        Cipher.DECRYPT_MODE
     * @param key             密钥
     * @return Cipher
     */
    public static Cipher getCipher(String cipherAlgorithm, int mode, Key key) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为加密模式
        cipher.init(mode, key);

        return cipher;
    }

    /**
     * 获取cipher对象
     *
     * @param cipherAlgorithm 工作模式
     * @param mode            加/解密
     *                        Cipher.ENCRYPT_MODE
     *                        Cipher.DECRYPT_MODE
     * @param key             密钥
     * @return Cipher
     */
    public static Cipher getCipher(String cipherAlgorithm, int mode, String key) throws Exception {
        return getCipher(cipherAlgorithm, mode, getSecretKeySpec(key));
    }


    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  二进制密钥
     * @return byte[]    加密数据
     */
    public static byte[] encrypt(byte[] data, Key key) throws Exception {
        return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }


    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]    加密数据
     */
    public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        Cipher cipher = getCipher(cipherAlgorithm, Cipher.ENCRYPT_MODE, key);
        //执行操作
        return cipher.doFinal(data);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]    加密数据
     */
    public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm, AlgorithmParameterSpec algorithmParameterSpec) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key, algorithmParameterSpec);
        //执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  二进制密钥
     * @return byte[]    解密数据
     */
    public static byte[] decrypt(byte[] data, Key key) throws Exception {
        return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]    解密数据
     */
    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm, AlgorithmParameterSpec algorithmParameterSpec) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameterSpec);
        //执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]    解密数据
     */
    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        Cipher cipher = getCipher(cipherAlgorithm, Cipher.DECRYPT_MODE, key);
        //执行操作
        return cipher.doFinal(data);
    }

}
