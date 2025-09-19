package io.github.wisely.core.helper;

import io.github.wisely.core.exception.SystemException;
import lombok.experimental.UtilityClass;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * XML 文件处理工具类
 * <p>
 * 提供读取 XML 文件并转换为 Document 对象的功能。
 * 支持选择是否加载 DTD 文件以防止 XXE 攻击。
 * </p>
 * <p>
 * 所有方法均为静态无状态，线程安全。
 * </p>
 */
@UtilityClass
public class XmlHelper {

    /**
     * 获取document
     *
     * @param inputStream 文件流
     * @param withDtd     是否加载DTD文件
     * @return Document对象
     */
    public static Document readFile(InputStream inputStream, boolean withDtd) {
        try {
            SAXReader reader = new SAXReader();
            if (!withDtd) {
                reader.setEntityResolver(new NullEntityResolver());
            }
            return reader.read(inputStream);
        } catch (Exception e) {
            throw SystemException.of(e, "XmlHelper.readFile_error");
        }
    }

    /**
     * 获取document
     *
     * @param inputStream 文件流
     * @return Document对象
     */
    public static Document readFile(InputStream inputStream) {
        return readFile(inputStream, true);
    }

    /**
     * 获取document
     *
     * @param resourcePath 资源路径
     * @return Document对象
     */
    public static Document readFile(String resourcePath) {
        return readFile(resourcePath, true);
    }

    /**
     * 获取document
     *
     * @param resourcePath 资源路径
     * @param withDtd      是否读取DTD文件
     * @return Document对象
     */
    public static Document readFile(String resourcePath, boolean withDtd) {
        return readFile(ResourceHelper.getInputStream(resourcePath), withDtd);
    }


    static class NullEntityResolver implements EntityResolver {
        static String emptyDtd = "";
        static ByteArrayInputStream byteIs = new ByteArrayInputStream(emptyDtd.getBytes());

        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(byteIs);
        }
    }

}
