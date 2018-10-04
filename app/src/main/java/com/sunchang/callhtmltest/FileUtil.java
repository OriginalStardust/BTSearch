package com.sunchang.callhtmltest;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Sun Chang on 2018/6/17.
 */

public class FileUtil {

    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";

    public static void writeToFile(String strContent, String filePath, String fileName) {
        File subfile = new File(filePath + fileName);

        try {
            if (!subfile.exists()) {
                subfile.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(subfile), "GBK");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(strContent);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteSDFile(String filePath, String fileName) {
        File file = new File(filePath + fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
