package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by dengchanglu on 15-11-23.
 */
public class GetResources {
    public static String getProperty(String filePath, String fileName, String propertyName){
        try{
            Properties p = loadPropertyInstance(filePath, fileName);
            return p.getProperty(propertyName);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static Properties loadPropertyInstance(String filePath, String fileName){
        try{
            File d = new File(filePath);
            if(!d.exists()){
                d.mkdirs();
            }
            File f = new File(d, fileName);
            if(!f.exists()){
                f.createNewFile();
            }
            Properties p = new Properties();
            InputStream is = new FileInputStream(f);
            p.load(is);
            is.close();
            return p;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
