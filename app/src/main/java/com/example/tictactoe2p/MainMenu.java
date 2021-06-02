package com.example.tictactoe2p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    public void multiPlayerGame(View view){
        Intent mpg = new Intent(this,hostJoinMenu.class);
        startActivity(mpg);
    }
    public void singlePlayerGame(View view){
        Intent spg= new Intent(this,MainActivity.class);
        startActivity(spg);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
}