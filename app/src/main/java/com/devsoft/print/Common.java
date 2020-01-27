package com.devsoft.print;

import android.content.Context;

import java.io.File;

class Common {
    public static String getAppPath(Context conext){
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator
                + conext.getResources().getString(R.string.app_name)
                + File.separator);

        if(!dir.exists()){
            dir.mkdir();
        }

        return dir.getPath() + File.separator;
    }
}
