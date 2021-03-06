package com.tenray.coolmall.util;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by en on 2016/11/25.
 */
public class LogWriterUtil {
    //+ "log" + File.separator
   //private static  String path= Environment.getExternalStorageDirectory()+ File.separator +"CoolMall"+ File.separator ;

    private static LogWriterUtil mLogWriter;

    private static String mPath;

    private static Writer mWriter;

    private static SimpleDateFormat df;

    private LogWriterUtil(String file_path) {
        this.mPath = file_path;
        this.mWriter = null;
    }

    public static LogWriterUtil open(String file_path,boolean append) throws IOException {
        mLogWriter = new LogWriterUtil(file_path);

       // File mFile = new File(mPath);
        try {
            mWriter = new BufferedWriter(new FileWriter(mPath,append), 2048);
        }catch (Exception e)
        {
            String str =e.getMessage();
            Log.d("",str);
        }
        df = new SimpleDateFormat("[dd HH:mm:ss:SSS]:");


        return mLogWriter;
    }

    public void close() throws IOException {
        mWriter.close();
    }

    public void print(String log) throws IOException {
        mWriter.write(df.format(new Date()));
        mWriter.write(log);
        mWriter.write("\n");
        mWriter.flush();
    }

    public void print(Class cls, String log) throws IOException { //如果还想看是在哪个类里可以用这个方法
        mWriter.write(df.format(new Date()));
        mWriter.write(cls.getSimpleName() + " ");
        mWriter.write(log);
        mWriter.write("\n");
        mWriter.flush();
    }

}