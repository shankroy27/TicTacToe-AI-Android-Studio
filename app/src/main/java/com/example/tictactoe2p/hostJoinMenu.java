package com.example.tictactoe2p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class hostJoinMenu extends AppCompatActivity {

    public void hostGame(View view) {
        Intent hG = new Intent(this, hostGame.class);
        startActivity(hG);
    }
    public void joinGame(View view){
        Intent jG=new Intent(this,joinGame.class);
        startActivity(jG);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_join_menu);
    }
}