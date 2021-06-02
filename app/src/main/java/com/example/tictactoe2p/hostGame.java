package com.example.tictactoe2p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class hostGame extends AppCompatActivity {

    TextView hostID,gameUID,guestID;
    EditText hName;
    FirebaseDatabase database;
    DatabaseReference myRef,gameRoom,gJoin;
    Context activityCont=this;
    String guestName;
    long gameID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);
        hostID=(TextView)findViewById(R.id.opId);
        gameUID=(TextView)findViewById(R.id.gameJId);
        guestID=(TextView)findViewById(R.id.guestID);

        database = FirebaseDatabase.getInstance("https://tictactoe-2p-15bd8-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("gameUID");
        gJoin= database.getReference();
        gameRoom = database.getReference();


        AlertDialog.Builder eName=new AlertDialog.Builder(this);
        LayoutInflater inflater=this.getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.enter_name,null);
        eName.setView(dialogView);
        hName=(EditText) dialogView.findViewById(R.id.etName);
        eName.setTitle("UserName required");
        eName.setMessage("Enter the User Name to Host Game");
        eName.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createGame();
            }
        });
        eName.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        eName.show();
    }

    public void createGame(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameID=snapshot.getValue(long.class);
                Log.d("shankykagameid",String.valueOf(gameID));
                hostID.setText(hName.getText().toString());
                gameUID.setText(String.valueOf(gameID));
                gameRoom.child("gameRooms").child(String.valueOf(gameID)).child("host").setValue(hName.getText().toString());
                long nextGID=gameID+1;
                myRef.setValue(nextGID);
                guestJoining();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void guestJoining(){
        Log.d("shankykagameid",String.valueOf(gameID));
        gJoin=gJoin.child("gameRooms").child(String.valueOf(gameID));
        gJoin.child("guest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    guestName = snapshot.getValue(String.class);
                    guestID.setText(guestName);
                    Toast.makeText(activityCont, guestName + " Joined", Toast.LENGTH_LONG).show();
                    launchGame();
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    public void launchGame(){
        Intent hgA=new Intent(this,HostGamePlay.class);
        hgA.putExtra("myName",hName.getText().toString());
        hgA.putExtra("oppName",guestName);
        hgA.putExtra("gameID",gameID);
        startActivity(hgA);
        finish();
    }
}