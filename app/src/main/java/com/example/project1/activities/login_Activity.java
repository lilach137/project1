package com.example.project1.activities;

import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.objects.SoundsUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class login_Activity extends AppCompatActivity {

    private MaterialButton login_BTN_letsGo;
    private TextInputEditText login_LBL_name;
    private Bundle bundle;
    private Intent homePage_activity;
    private String playerName;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findView();
        bundle = new Bundle();
        homePage_activity = new Intent(this,homePage_Activity.class);
        clickOnButton();
    }

    private void findView() {
        login_LBL_name = findViewById(R.id.login_LBL_name);
        login_BTN_letsGo = findViewById(R.id.login_BTN_letsGo);
    }

    private void clickOnButton() {
        login_BTN_letsGo.setOnClickListener(view -> {
                checkName();

        });

    }

    private void checkName(){
    if(login_LBL_name.getText().toString().equals("")){
        new AlertDialog.Builder(this)
                .setTitle("WARNING")
                .setMessage("PLEASE ENTER YOUR WOLF NAME")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
        else {

        playerName = login_LBL_name.getText().toString();
        bundle.putString(homePage_Activity.TEMP_PLAYER, playerName);
        homePage_activity.putExtras(bundle);
        startActivity(homePage_activity);
    }

    }
}
