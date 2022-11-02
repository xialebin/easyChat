package com.app.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {



    public static boolean writeFile(String path,String fileName,String contents){


        FileWriter writer = null;

        try{

            File dir = new File(path);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(path+"/"+fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new FileWriter(file,true);
            writer.append(contents+"\n");
            writer.flush();
            return true;
        }catch (Exception e){
            return false;
        }finally {

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
    }


}
