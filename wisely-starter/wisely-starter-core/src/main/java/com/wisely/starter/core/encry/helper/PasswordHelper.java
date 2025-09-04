package com.wisely.starter.core.encry.helper;

import com.wisely.starter.core.exception.SystemException;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码加密工具类
 * 使用 BCrypt 算法进行安全的密码哈希处理
 */
public final class PasswordHelper {

    private static final int BCRYPT_DEFAULT_ROUNDS = 12;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordHelper() {
        throw new UnsupportedOperationException("密码工具类不允许实例化");
    }

    // ==================== BCrypt 密码加密 ====================

    /**
     * 使用BCrypt加密密码（自动生成盐值）
     */
    public static String encrypt(String plainPassword) {
        return encrypt(plainPassword, BCRYPT_DEFAULT_ROUNDS);
    }

    /**
     * 使用BCrypt加密密码（指定计算成本）
     */
    public static String encrypt(String plainPassword, int rounds) {
        validatePassword(plainPassword);
        try {
            return BCrypt.hashpw(plainPassword, BCrypt.gensalt(rounds));
        } catch (Exception e) {
            throw SystemException.of(e, "密码加密失败");
        }
    }

    /**
     * 验证密码是否匹配BCrypt哈希值
     */
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            throw SystemException.of(e, "密码验证失败");
        }
    }

    // ==================== 密码策略验证 ====================

    /**
     * 验证密码强度
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 12) {
            return false;
        }

        // 密码强度规则
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasSpecial = password.chars()
                .anyMatch(ch -> !Character.isLetterOrDigit(ch) && !Character.isSpaceChar(ch));

        int criteriaMet = 0;
        if (hasDigit) criteriaMet++;
        if (hasLower) criteriaMet++;
        if (hasUpper) criteriaMet++;
        if (hasSpecial) criteriaMet++;

        return criteriaMet >= 3;
    }

    /**
     * 获取密码强度评分
     *
     * @param password 密码
     * @return 评分范围0-5，5为最强
     */
    public static int getPasswordStrength(String password) {
        if (password == null) return 0;

        int strength = 0;

        // 长度评分
        if (password.length() >= 12) strength += 2;
        else if (password.length() >= 8) strength += 1;

        // 字符多样性评分
        if (password.chars().anyMatch(Character::isDigit)) strength++;
        if (password.chars().anyMatch(Character::isLowerCase)) strength++;
        if (password.chars().anyMatch(Character::isUpperCase)) strength++;
        if (password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch))) strength++;

        return Math.min(strength, 5); // 最高5分
    }

    // ==================== 工具方法 ====================

    /**
     * 生成随机密码
     *
     * @param length 密码长度，至少12位
     */
    public static String generateRandomPassword(int length) {
        if (length < 12) {
            throw new IllegalArgumentException("密码长度至少12位");
        }

        String uppercase = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String lowercase = "abcdefghjkmnpqrstuvwxyz";
        String digits = "23456789";
        String specials = "!@#$%^&*";

        StringBuilder password = new StringBuilder(length);

        // 确保包含每种字符类型
        password.append(randomChar(uppercase));
        password.append(randomChar(lowercase));
        password.append(randomChar(digits));
        password.append(randomChar(specials));

        // 填充剩余长度
        String allChars = uppercase + lowercase + digits + specials;
        for (int i = password.length(); i < length; i++) {
            password.append(randomChar(allChars));
        }

        // 打乱顺序
        return shuffleString(password.toString());
    }

    /**
     * 生成随机令牌
     */
    public static String generateToken(int length) {
        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // ==================== 私有方法 ====================

    private static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("密码长度至少8位");
        }
    }

    private static char randomChar(String characters) {
        return characters.charAt(SECURE_RANDOM.nextInt(characters.length()));
    }

    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = SECURE_RANDOM.nextInt(i + 1);
            char temp = characters[index];
            characters[index] = characters[i];
            characters[i] = temp;
        }
        return new String(characters);
    }
}