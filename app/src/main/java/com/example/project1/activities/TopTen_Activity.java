package com.example.project1.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.Fragments.Fragment_List;
import com.example.project1.Fragments.Fragment_Map;
import com.example.project1.R;
import com.example.project1.callBack.CallBack_ListPlayerClicked;
import com.google.android.material.button.MaterialButton;

public class TopTen_Activity extends AppCompatActivity {

    Fragment_List fragment_list;
    Fragment_Map fragment_map;
    MaterialButton topTen_BTN_homePage;
    MaterialButton topTen_BTN_replay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_ten);
        findView();
        initButtons();

        fragment_map = new Fragment_Map();
        fragment_list = new Fragment_List();

        getSupportFragmentManager().beginTransaction().add(R.id.topTen_frame2, fragment_map).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.topTen_frame1, fragment_list).commit();

        fragment_list.setActivity(this);

        fragment_list.setCallBack_listPlayerClicked((lat, lon, name) -> fragment_map.pointOnMap(lat, lon));


    }

    private void initButtons() {
        topTen_BTN_replay.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restart();
                    }
        }));



        topTen_BTN_homePage.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));
    }


    private void findView() {
        topTen_BTN_homePage = findViewById(R.id.topTen_BTN_homePage);
        topTen_BTN_replay = findViewById(R.id.topTen_BTN_replay);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    private void restart() {
        Bundle bundle = new Bundle();
        bundle.putInt(Main_Activity.GAME_TYPE, 0);
        Intent gameIntent = new Intent(this, Main_Activity.class);
        gameIntent.putExtras(bundle);
        startActivity(gameIntent);
        this.finish();
    }

}