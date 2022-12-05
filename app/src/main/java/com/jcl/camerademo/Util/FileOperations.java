package com.jcl.camerademo.Util;

import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by yangwenlong on 2020/5/16.
 */

public class FileOperations {


    public static void writeData(String url, String name, String content) {
        String filePath = url;
        String fileName = name + ".txt";
        writeTxtToFile(content, filePath, fileName);
    }

    // 将字符串写入到文本文件中
    private static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    //生成文件
    private static File makeFilePath(String filePath, String fileName) {
        File file = null;
       // makeRootDirectory(filePath);
        String s = filePath + fileName;
        System.out.println("文件路径"+s);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    //生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            System.out.println("新建文件夹："+filePath);
            file = new File(filePath);
            //不存在就新建
            if (!file.exists()) {
                file.mkdir();
                System.out.println("新建成功！！");
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

}