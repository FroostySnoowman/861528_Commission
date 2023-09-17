package org.frostyservices.Utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public void createFile(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkFileExist(String fileName) {
        File f = new File(fileName);
        if(f.exists() && !f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }
}