package com.example.project1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.objects.GameManager;
import com.example.project1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.bumptech.glide.Glide;



public class homePage_Activity extends AppCompatActivity {

    private MaterialButton homePage_BTN_newGameButtons;
    private MaterialButton homePage_BTN_newGameSensors;
    private MaterialButton homePage_BTN_topTen;
    private TextView homePage_TXT_name;
    public static final String TEMP_PLAYER = "TEMP_PLAYER";

    private Bundle bundle;
    private Intent main_activity;
    String tempPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        findView();
        bundle = new Bundle();
        main_activity = new Intent(this , Main_Activity.class);
        sendPlayerData();
        homePage_TXT_name.setText("hello "+tempPlayer);
        clickOnButton();

    }

    private void sendPlayerData() {
        Bundle bundle2 = getIntent().getExtras();
        tempPlayer = bundle2.getString(TEMP_PLAYER);
        bundle.putString(Main_Activity.GAME_PLAYER,tempPlayer);
        main_activity.putExtras(bundle);
    }



    private void clickOnButton(){

        homePage_BTN_topTen.setOnClickListener(view -> startActivity(new Intent(this, TopTen_Activity.class)));
        homePage_BTN_newGameButtons.setOnClickListener(view -> {
            bundle.putInt(Main_Activity.GAME_TYPE,0);
            sendPlayerData();
            startActivity(main_activity);
        });

        homePage_BTN_newGameSensors.setOnClickListener(view -> {
            bundle.putInt(Main_Activity.GAME_TYPE,1);
            sendPlayerData();
            startActivity(main_activity);
        });
    }
    private void findView() {
        homePage_BTN_newGameButtons = findViewById(R.id.homePage_BTN_newGameButtons);
        homePage_BTN_newGameSensors = findViewById(R.id.homePage_BTN_newGameSensors);
        homePage_BTN_topTen = findViewById(R.id.homePage_BTN_topTen);
        homePage_TXT_name = findViewById(R.id.homePage_TXT_name);


        ImageView homePage_IMG_background;
        homePage_IMG_background = findViewById(R.id.homePage_IMG_background);

        Glide
                .with(this)
                .load(R.drawable.bc_home_page)
                .into(homePage_IMG_background);
    }
}