package com.example.project1.objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyDB {

    private ArrayList<Record> records;
    private static MyDB myDB;

    public MyDB() {
        this.records = new ArrayList<>();
    }

    public ArrayList<Record> getRecords() {
        return records;
    }


    public static MyDB initMyDB() {
        if (myDB == null) {
            myDB = new MyDB();
        }
        return myDB;
    }

    public Record getRecord(int place) {
        return records.get(place);
    }

    public MyDB setRecords(ArrayList<Record> records) {
        this.records = records;
        return this;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortByScore() {
        records.sort(new SortByScore());
    }

    public void addRecord(Record record) {
        records.add(record);
    }


    public static class SortByScore implements Comparator<Record> {

        @Override
        public int compare(Record rec1, Record rec2) {

            return rec1.getScore() - rec2.getScore();
        }

    }
}
