package com.wisely.starter.core.helper;

import com.wisely.starter.core.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * ResourceHelper
 */
@Slf4j
public class ResourceHelper {

    private ResourceHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private final static ResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

    /**
     * 读取文件资源
     *
     * @param path 资源路径, 支持通配符
     * @return Resource对象数组
     */
    public static Resource[] getResources(String path) {
        try {
            return RESOURCE_RESOLVER.getResources(path);
        } catch (IOException e) {
            // ignore it
            log.warn("ResourceHelper.getResources no resources found", e);
        }
        return new Resource[]{};
    }

    /**
     * 读取文件资源
     *
     * @param path 资源路径, 支持通配符
     * @return Resource对象
     */
    public static Resource getResource(String path) {

        // 处理ResourceHelper多级 "//" 时无法正确获取文件的问题
        path = StringHelper.replaceEach(path, new String[]{"//"}, new String[]{"/"});

        Resource[] resources = getResources(path);
        if (ValidHelper.isNotEmpty(resources)) {
            return resources[0];
        }
        return null;
    }


    /**
     * 读取资源文件
     *
     * @param path 资源路径, 支持通配符
     * @return InputStream对象
     */
    public static InputStream getInputStream(String path) {
        Resource resource = getResource(path);
        try {
            return resource == null ? null : resource.getInputStream();
        } catch (IOException e) {
            throw SystemException.of(e, "ResourceHelper.getInputStream error");
        }
    }


    /**
     * 返回字节数组
     *
     * @param path 资源路径
     * @return 文件资源的字节数组
     */
    public static byte[] getBytes(Resource path) {

        if (path == null || !path.exists()) {
            throw SystemException.of("ResourceHelper.getBytes resource not found!!!");
        }

        try (
                InputStream in = path.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int num;
            while ((num = in.read(buffer)) != -1) {
                baos.write(buffer, 0, num);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw SystemException.of(e, "ResourceHelper.getBytes error");
        }
    }

    /**
     * 返回字节数组
     *
     * @param path 资源路径
     * @return 文件资源的字节数组
     */
    public static byte[] getBytes(String path) {
        return getBytes(getResource(path));
    }

}
