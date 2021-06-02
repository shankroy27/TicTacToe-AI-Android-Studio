package com.example.tictactoe2p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class joinGame extends AppCompatActivity {

    TextView hostID,myId,gameJID;
    EditText myName,gCode;
    Context jGcont=this;
    String hostName;
    long gID;

    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        hostID=(TextView)findViewById(R.id.opId);
        myId=(TextView)findViewById(R.id.myId);
        gameJID=(TextView)findViewById(R.id.gameJId);

        database=FirebaseDatabase.getInstance("https://tictactoe-2p-15bd8-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("gameRooms");

        getNameCode();
    }

    public void getNameCode(){
        AlertDialog.Builder gDetails=new AlertDialog.Builder(jGcont);
        LayoutInflater inflater=this.getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.join_dialog,null);
        gDetails.setView(dialogView);
        myName=(EditText) dialogView.findViewById(R.id.myName);
        gCode=(EditText) dialogView.findViewById(R.id.gameCode);
        gDetails.setTitle("Game Details");
        gDetails.setMessage("Enter the User Name and Game Code");
        gDetails.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterGame();
            }
        });
        gDetails.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        gDetails.show();
    }

    public void enterGame(){
        myRef=myRef.child(gCode.getText().toString());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    snapshot=snapshot.child("host");
                    hostName=snapshot.getValue(String.class);
                    hostID.setText(hostName);
                    myId.setText(myName.getText().toString());
                    myRef.child("guest").setValue(myName.getText().toString());
                    gameJID.setText(gCode.getText().toString());
                    gID=Long.parseLong(gCode.getText().toString());
                    launchGame();
                }
                else {
                    new AlertDialog.Builder(jGcont).setMessage("Game Code Incorrect").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void launchGame(){
        Intent jgA=new Intent(this,GuestGamePlay.class);
        jgA.putExtra("myName",myName.getText().toString());
        jgA.putExtra("oppName",hostName);
        jgA.putExtra("gameID",gID);
        startActivity(jgA);
        finish();
    }
}