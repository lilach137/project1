package com.example.project1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project1.objects.MSPV;
import com.example.project1.objects.Record;
import com.google.gson.Gson;
import com.example.project1.objects.MyDB;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.project1.R;

import com.example.project1.callBack.CallBack_ListPlayerClicked;
import com.google.android.material.button.MaterialButton;

public class Fragment_List extends Fragment {

    private MaterialButton[] winners;
    private CallBack_ListPlayerClicked callBack_listPlayerClicked;



    public void setActivity(AppCompatActivity activity) {
    }

    public void setCallBack_listPlayerClicked(CallBack_ListPlayerClicked callBack_listPlayerClicked) {
        this.callBack_listPlayerClicked = callBack_listPlayerClicked;
    }
    private void initViews() {
        String js = MSPV.getMe().getString("MY_DB", "");
        MyDB myDB = new Gson().fromJson(js, MyDB.class);
        myDB.sortByScore();
        for (int i = 0; i < myDB.getRecords().size(); i++) {
            Record record = myDB.getRecord(i);
            winners[i].setVisibility(View.VISIBLE);
            winners[i].setText(i+1+". "+record.getName()+"  Score: "+record.getScore());
            winners[i].setOnClickListener(v -> callBack_listPlayerClicked.playerClicked(record.getLat(),record.getLon(), record.getName()));

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }



    private void findViews(View view) {
        winners= new MaterialButton[]{
                view.findViewById(R.id.fList_BTN_place1),
                view.findViewById(R.id.fList_BTN_place2),
                view.findViewById(R.id.fList_BTN_place3),
                view.findViewById(R.id.fList_BTN_place4),
                view.findViewById(R.id.fList_BTN_place5),
                view.findViewById(R.id.fList_BTN_place6),
                view.findViewById(R.id.fList_BTN_place7),
                view.findViewById(R.id.fList_BTN_place8),
                view.findViewById(R.id.fList_BTN_place9),
                view.findViewById(R.id.fList_BTN_place10),
        };
    }
}