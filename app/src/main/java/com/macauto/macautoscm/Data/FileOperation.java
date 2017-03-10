package com.macauto.macautoscm.Data;


import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperation {
    private static final String TAG = FileOperation.class.getName();

    public static File RootDirectory = new File("/");

    public static boolean init_folder_and_files() {
        Log.i(TAG, "init_folder_and_files() --- start ---");
        boolean ret = true;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path = Environment.getExternalStorageDirectory();
            RootDirectory = Environment.getExternalStorageDirectory();
        }

        File folder_scm = new File(RootDirectory.getAbsolutePath() + "/.macautoSCM/");
        File notify_msg = new File(RootDirectory.getAbsolutePath() + "/.macautoSCM/"+ "msg");

        if(!folder_scm.exists()) {
            Log.i(TAG, "folder not exist");
            ret = folder_scm.mkdirs();
            if (!ret)
                Log.e(TAG, "init_folder_and_files: failed to mkdir hidden");
            try {
                ret = folder_scm.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!ret)
                Log.e(TAG, "init_info: failed to create hidden file");
        }

        while(true) {
            if(folder_scm.exists())
                break;
        }


        //if file is not exist, create!
        if(!notify_msg.exists()) {
            Log.i(TAG, "file not exist");
            //ret = folder.mkdirs();
            //if (!ret)
            //    Log.e(TAG, "wrire_message: failed to mkdir "+user_id);
            try {
                ret = notify_msg.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!ret)
                Log.e(TAG, "init_folder_and_files: failed to create file "+notify_msg.getAbsolutePath());

        }

        Log.i(TAG, "init_folder_and_files() ---  end  ---");
        return ret;
    }

    public static boolean remove_file(String fileName) {
        boolean ret = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path = Environment.getExternalStorageDirectory();
            RootDirectory = Environment.getExternalStorageDirectory();
        }

        File file = new File(RootDirectory.getAbsolutePath() + "/.macautoSCM/"+fileName);

        if (file.exists()) {
            ret = file.delete();
        } else {
            Log.d(TAG, "file "+file.getName()+ " is not exist");
        }

        return ret;
    }

    public static boolean check_file_exist(String fileName) {
        Log.i(TAG, "append_record --- start ---");
        boolean ret = false;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path = Environment.getExternalStorageDirectory();
            RootDirectory = Environment.getExternalStorageDirectory();
        }

        File file = new File(RootDirectory.getAbsolutePath() + "/.macautoSCM/"+fileName);

        if(file.exists()) {
            Log.i(TAG, "file exist");
            ret = true;
        }

        return ret;
    }

    public static boolean clear_record() {
        boolean ret = true;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path = Environment.getExternalStorageDirectory();
            RootDirectory = Environment.getExternalStorageDirectory();
        }

        //check folder
        File folder = new File(RootDirectory.getAbsolutePath() + "/.macautoSCM");

        if (folder.exists()) {
            File matchRecord = new File(folder+"/"+"msg");


            if (!matchRecord.exists()) {
                try {
                    ret = matchRecord.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!ret)
                    Log.e(TAG, "create "+matchRecord.getName()+" failed!");
            }

            //if exist, wrire emapt string
            try {
                FileWriter fw = new FileWriter(matchRecord.getAbsolutePath());
                fw.write("");
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
                ret = false;
            }



        } else {
            Log.e(TAG, "inside_folder not exits!");
            ret = false;
        }



        return ret;
    }

    public static boolean append_record(String message, String fileName) {
        Log.i(TAG, "append_record --- start ---");
        boolean ret = true;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path = Environment.getExternalStorageDirectory();
            RootDirectory = Environment.getExternalStorageDirectory();
        }
        //check folder
        File folder = new File(RootDirectory.getAbsolutePath() + "/.macautoSCM");

        if(!folder.exists()) {
            Log.i(TAG, "folder not exist");
            ret = folder.mkdirs();
            if (!ret)
                Log.e(TAG, "append_message: failed to mkdir ");
        }

        //File file_txt = new File(folder+"/"+date_file_name);
        File file_txt = new File(folder+"/"+fileName);
        //if file is not exist, create!
        if(!file_txt.exists()) {
            Log.i(TAG, "file not exist");

            try {
                ret = file_txt.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!ret)
                Log.e(TAG, "append_record: failed to create file "+file_txt.getAbsolutePath());

        }

        try {
            FileWriter fw = new FileWriter(file_txt.getAbsolutePath(), true);
            fw.write(message);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        }


        Log.i(TAG, "append_record --- end (success) ---");

        return ret;
    }

    public static boolean append_message(String message) {
        Log.i(TAG, "append_message --- start ---");
        boolean ret = true;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path = Environment.getExternalStorageDirectory();
            RootDirectory = Environment.getExternalStorageDirectory();
        }
        //check folder
        File folder = new File(RootDirectory.getAbsolutePath() + "/.macautoSCM/");

        if(!folder.exists()) {
            Log.i(TAG, "folder not exist");
            ret = folder.mkdirs();
            if (!ret)
                Log.e(TAG, "append_message: failed to mkdir ");
        }

        //Calendar c = Calendar.getInstance();
        //NumberFormat f = new DecimalFormat("00");

        //String date_file_name = String.valueOf(c.get(Calendar.YEAR))+f.format(c.get(Calendar.MONTH)+1)+f.format(c.get(Calendar.DAY_OF_MONTH));


        //File file_txt = new File(folder+"/"+date_file_name);
        File file_txt = new File(folder+"/msg");
        //if file is not exist, create!
        if(!file_txt.exists()) {
            Log.i(TAG, "file not exist");
            //ret = folder.mkdirs();
            //if (!ret)
            //    Log.e(TAG, "wrire_message: failed to mkdir "+user_id);
            try {
                ret = file_txt.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!ret)
                Log.e(TAG, "append_message: failed to create file "+file_txt.getAbsolutePath());

        }

        try {
            FileWriter fw = new FileWriter(file_txt.getAbsolutePath(), true);
            fw.write(message);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        }


        Log.i(TAG, "append_message --- end (success) ---");

        return ret;
    }

    public static String read_message() {


        Log.i(TAG, "read_message() --- start ---");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path = Environment.getExternalStorageDirectory();
            RootDirectory = Environment.getExternalStorageDirectory();
        }

        File file_date = new File(RootDirectory.getAbsolutePath() + "/.macautoSCM/"+"msg");
        String message = "";


        if (!file_date.exists())
        {
            Log.i(TAG, "read_message() "+file_date.getAbsolutePath()+ " not exist");
            boolean ret = false;
            try {
                ret = file_date.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!ret)
                Log.e(TAG, "read_message: failed to create file "+file_date.getAbsolutePath());

            return "";
        }
        else {
            try {

                FileReader fr = new FileReader(file_date.getAbsolutePath());
                BufferedReader br = new BufferedReader(fr);
                while (br.ready()) {

                    message = br.readLine();

                }
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i(TAG, "read_message() --- end ---");
        }


        return message;
    }

    public static String get_absolute_path(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //path = Environment.getExternalStorageDirectory();
            RootDirectory = Environment.getExternalStorageDirectory();
        }

        File file = new File(RootDirectory.getAbsolutePath() + "/.macautoSCM/"+fileName);

        return file.getAbsolutePath();
    }
}
