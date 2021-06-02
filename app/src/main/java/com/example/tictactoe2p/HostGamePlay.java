package com.example.tictactoe2p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HostGamePlay extends AppCompatActivity {

    ArrayList<ImageView> imageViews=new ArrayList<>();
    int activePlayer=0;
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    FirebaseDatabase database;
    DatabaseReference myRef,gMove;
    long gameID;
    String myName,oppName;
    Context activityCont=this;
    TextView myPlayerName,oppPlayerName,turnStatus;

    public ArrayList<Integer> emptySpots(int[] board){
        ArrayList<Integer> eSpots=new ArrayList<Integer>();
        for(int i=0;i<9;i++){
            if(board[i]==2){
                eSpots.add(i);
            }
        }
        return eSpots;
    }

    public int checkWin(int[] board){
        for(int[] winPosition:winPositions){
            if(board[winPosition[0]]==board[winPosition[1]]&&board[winPosition[1]]==board[winPosition[2]]&&board[winPosition[2]]!=2){
                if(board[winPosition[0]]==0){
                    return -1;
                }
                else{
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game_play);

        imageViews.add((ImageView)findViewById(R.id.imageView0));
        imageViews.add((ImageView)findViewById(R.id.imageView1));
        imageViews.add((ImageView)findViewById(R.id.imageView2));
        imageViews.add((ImageView)findViewById(R.id.imageView3));
        imageViews.add((ImageView)findViewById(R.id.imageView4));
        imageViews.add((ImageView)findViewById(R.id.imageView5));
        imageViews.add((ImageView)findViewById(R.id.imageView6));
        imageViews.add((ImageView)findViewById(R.id.imageView7));
        imageViews.add((ImageView)findViewById(R.id.imageView8));
        imageViews.add((ImageView)findViewById(R.id.imageView9));


        Intent intent = getIntent();

        myName=intent.getExtras().getString("myName");
        oppName=intent.getExtras().getString("oppName");
        gameID=intent.getExtras().getLong("gameID");

        myPlayerName=(TextView)findViewById(R.id.myPlayerName);
        oppPlayerName=(TextView)findViewById(R.id.oppPlayerName);
        turnStatus=(TextView)findViewById(R.id.turnStatus);

        myPlayerName.setText(myName);
        oppPlayerName.setText(oppName);

        database = FirebaseDatabase.getInstance("https://tictactoe-2p-15bd8-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference();
        myRef=myRef.child("gameRooms").child(String.valueOf(gameID));
        gMove=myRef;

    }
    public void playerTap(View view){
        ImageView img=(ImageView)view;
        int tappedImage=Integer.parseInt(img.getTag().toString());
        if(gameState[tappedImage]==2){
            gameState[tappedImage]=0;
            //activePlayer=activePlayer==0?1:0;
            img.setImageResource(R.drawable.x);
            myRef.child("hMove").setValue(tappedImage);
        }
        if(checkWin(gameState)==-1){
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.game_end,null);
            alert.setView(dialogView);
            TextView geL = (TextView)dialogView.findViewById(R.id.gamEndLine);
            String winLine="YOU WON ";
            geL.setText(winLine);
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
        }
        else if(checkWin(gameState)==0&&emptySpots(gameState).size()<1){
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.game_end,null);
            alert.setView(dialogView);
            TextView geL = (TextView)dialogView.findViewById(R.id.gamEndLine);
            geL.setText(" GAME DRAW ");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
        }
        else{
            lockBoard();
            switchTurn();
        }
    }

    public void switchTurn(){
        gMove.child("gMove").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    long gM=snapshot.getValue(Long.class);
                    int guestMove=(int)gM;
                    if(gameState[guestMove]==2){
                        gameState[guestMove]=1;
                        //activePlayer=activePlayer==0?1:0;
                        imageViews.get(guestMove).setImageResource(R.drawable.o);
                    }
                    if(checkWin(gameState)==1){
                        AlertDialog.Builder alert=new AlertDialog.Builder(activityCont);
                        LayoutInflater inflater = getWindow().getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.game_end,null);
                        alert.setView(dialogView);
                        TextView geL = (TextView)dialogView.findViewById(R.id.gamEndLine);
                        String winLine="YOU LOST ";
                        geL.setText(winLine);
                        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                        alert.show();
                    }
                    else if(checkWin(gameState)==0&&emptySpots(gameState).size()<1){
                        AlertDialog.Builder alert=new AlertDialog.Builder(activityCont);
                        LayoutInflater inflater = getWindow().getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.game_end,null);
                        alert.setView(dialogView);
                        TextView geL = (TextView)dialogView.findViewById(R.id.gamEndLine);
                        geL.setText(" GAME DRAW ");
                        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                        alert.show();
                    }
                    else{
                        unlockBoard();
                    }
                    gMove.child("gMove").removeEventListener(this);
                    gMove.child("gMove").removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void lockBoard(){
        turnStatus.setText("Wait! Its your opponent's Turn.");
        Log.d("shankybhaikagame","lock board running");
        for(ImageView imageView:imageViews){
            imageView.setClickable(false);
        }
    }

    public void unlockBoard(){
        turnStatus.setText("YourTurn- Tap on the box to Place");
        Log.d("shankybhaikagame","unlock board running");
        for(ImageView imageView:imageViews){
            imageView.setClickable(true);
        }
    }
}