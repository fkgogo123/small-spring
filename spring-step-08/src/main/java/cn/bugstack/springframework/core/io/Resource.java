package cn.bugstack.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;


/**
 * Resource资源加载器
 */
public interface Resource {

    InputStream getInputStream() throws IOException;

}
