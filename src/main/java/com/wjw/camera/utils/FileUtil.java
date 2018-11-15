package com.wjw.camera.utils;

import java.io.*;

/**
 * ===================================
 * Created With IntelliJ IDEA
 *
 * @author Waein :)
 * @version method: FileUtil, v 0.1
 * @CreateDate 2018/11/6
 * @CreateTime 10:04
 * @GitHub https://github.com/Waein
 * ===================================
 */
public class FileUtil {
    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            //使用字节数组输出
            ByteArrayOutputStream out = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file)); //缓冲流
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    out.write(buffer, 0, len1);
                }

                byte[] result = out.toByteArray();
                return result;
            } finally {
                //是否处理完成也将流关闭,避免撑爆内存
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                out.close();
            }
        }
    }

}
