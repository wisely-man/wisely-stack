package io.github.wisely.starter.encry.helper;

import io.github.wisely.starter.core.exception.SystemException;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 签名工具类
 * 提供文件和数据内容的哈希签名功能
 */
@UtilityClass
public final class SignHelper {

    private static final char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private static final int BUFFER_SIZE = 8192;

    // ==================== MD5 相关方法 ====================

    /**
     * 计算文件的MD5哈希值
     */
    public static String md5(File file) {
        return hashFile(file, "MD5");
    }

    /**
     * 计算输入流的MD5哈希值
     */
    public static String md5(InputStream inputStream) {
        return hashStream(inputStream, "MD5");
    }

    /**
     * 计算字符串的MD5哈希值（UTF-8编码）
     */
    public static String md5(String content) {
        return hashString(content, "MD5");
    }

    /**
     * 计算字节数组的MD5哈希值
     */
    public static String md5(byte[] bytes) {
        return hashBytes(bytes, "MD5");
    }

    // ==================== SHA-256 相关方法 ====================

    /**
     * 计算文件的SHA-256哈希值
     */
    public static String sha256(File file) {
        return hashFile(file, "SHA-256");
    }

    /**
     * 计算输入流的SHA-256哈希值
     */
    public static String sha256(InputStream inputStream) {
        return hashStream(inputStream, "SHA-256");
    }

    /**
     * 计算字符串的SHA-256哈希值（UTF-8编码）
     */
    public static String sha256(String content) {
        return hashString(content, "SHA-256");
    }

    /**
     * 计算字节数组的SHA-256哈希值
     */
    public static String sha256(byte[] bytes) {
        return hashBytes(bytes, "SHA-256");
    }

    // ==================== 通用哈希方法 ====================

    /**
     * 计算文件的哈希值
     */
    public static String hashFile(File file, String algorithm) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return hashStream(fis, algorithm);
        } catch (Exception e) {
            throw SystemException.of(e, "计算文件哈希值失败: {}", algorithm);
        }
    }

    /**
     * 计算输入流的哈希值
     */
    public static String hashStream(InputStream inputStream, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            return bytesToHex(digest.digest());
        } catch (Exception e) {
            throw SystemException.of(e, "计算流哈希值失败: {}", algorithm);
        }
    }

    /**
     * 计算字符串的哈希值
     */
    public static String hashString(String content, String algorithm) {
        return hashBytes(content.getBytes(StandardCharsets.UTF_8), algorithm);
    }

    /**
     * 计算字节数组的哈希值
     */
    public static String hashBytes(byte[] bytes, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hash = digest.digest(bytes);
            return bytesToHex(hash);
        } catch (Exception e) {
            throw SystemException.of(e, "计算哈希值失败: {}", algorithm);
        }
    }

    // ==================== 验证方法 ====================

    /**
     * 验证MD5哈希值是否匹配
     */
    public static boolean verifyMd5(String content, String expectedHash) {
        return secureCompare(md5(content), expectedHash);
    }

    /**
     * 验证SHA-256哈希值是否匹配
     */
    public static boolean verifySha256(String content, String expectedHash) {
        return secureCompare(sha256(content), expectedHash);
    }

    /**
     * 安全比较两个哈希值（防止时序攻击）
     */
    public static boolean secureCompare(String hash1, String hash2) {
        if (hash1 == null || hash2 == null) {
            return false;
        }
        return MessageDigest.isEqual(
                hash1.getBytes(StandardCharsets.UTF_8),
                hash2.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ==================== 工具方法 ====================

    /**
     * 字节数组转换为十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hexString.append(HEX_DIGITS[(b & 0xF0) >> 4]);
            hexString.append(HEX_DIGITS[b & 0x0F]);
        }
        return hexString.toString();
    }
    
}