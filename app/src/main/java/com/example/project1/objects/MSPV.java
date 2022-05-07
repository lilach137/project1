package com.example.project1.objects;


import android.content.Context;
import android.content.SharedPreferences;

    public class MSPV {

        private final String MY_SP_FILE = "MY_SP_FILE";
        private static MSPV me;
        private SharedPreferences sharedPreferences;

        public static MSPV getMe() {
            return me;
        }

        private MSPV(Context context) {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(MY_SP_FILE, Context.MODE_PRIVATE);
        }

        public static MSPV initHelper(Context context) {
            if (me == null) {
                me = new MSPV(context);
            }
            return me;
        }


        public void putInt(String KEY, int value) {
            sharedPreferences.edit().putInt(KEY, value).apply();
        }
        public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
        }

        public int getInt(String KEY, int defValue) {
            return sharedPreferences.getInt(KEY,0);
        }

        public String getString(String KEY, String defValue) {
            return sharedPreferences.getString(KEY, defValue);
        }



    }

