package org.quickjava.core.utils;

import javax.imageio.IIOException;
import java.io.*;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 22:54
 * @ProjectName quickjava
 */
public class QFileUtils {

    public static String getFileContents(String name)
            throws Exception
    {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(name);
            StringBuffer stringBuffer = new StringBuffer();
            int num = 0;
            byte[] buf = new byte[512];
            while((num = fileInputStream.read(buf))!= -1) {
                stringBuffer.append(new String(buf,0,num));
            }

            return stringBuffer.toString();

        }catch (Throwable tr){
            tr.printStackTrace();
        }finally {
            if (fileInputStream != null) {
                try{
                    fileInputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
