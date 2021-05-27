package com.example.tictactoe2p;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Math.addExact;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {
    boolean gameActive=true;
    int activePlayer=0;
    ArrayList<ImageView> grid=new ArrayList<>();
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    public static int count=0;
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
    public ArrayList<Integer> emptySpots(int[] board){
        ArrayList<Integer> eSpots=new ArrayList<Integer>();
        for(int i=0;i<9;i++){
            if(board[i]==2){
                eSpots.add(i);
            }
        }
        return eSpots;
    }
    public int genPmoves(int[] board,int activePlayer,boolean isMax){
        ArrayList<Integer> eBoard= new ArrayList<>();
        eBoard=emptySpots(board);
        int res=checkWin(board);
        if(res==1){
            return 1;
        }
        else if(res==-1){
            return -1;
        }
        else if(res==0&&eBoard.size()<1){
            return 0;
        }
        if(isMax) {
            int bestScore=Integer.MIN_VALUE;
            int score;
            for (int i = 0; i < eBoard.size(); i++) {
                board[eBoard.get(i)] = activePlayer;
                score=genPmoves(board, 0, false);
                board[eBoard.get(i)] = 2;
                bestScore=max(bestScore,score);
            }
            return bestScore;
        }
        else{
            int bestScore=Integer.MAX_VALUE;
            int score;
            for (int i = 0; i < eBoard.size(); i++) {
                board[eBoard.get(i)] = activePlayer;
                score=genPmoves(board, 1, true);
                board[eBoard.get(i)] = 2;
                bestScore=min(bestScore,score);
            }
            return bestScore;
        }
    }
    public int aiMove(int[] board){
        ArrayList<Integer> eBoard= new ArrayList<>();
        eBoard=emptySpots(board);
        int bestScore=Integer.MIN_VALUE;
        int bestMove=eBoard.get(0);
        int score;
        for(int i=0;i<eBoard.size();i++){
            board[eBoard.get(i)] = 1;
            score=genPmoves(board,0,false);
            board[eBoard.get(i)] = 2;
            if(score>bestScore){
                bestScore=score;
                bestMove=eBoard.get(i);

            }

        }
        return bestMove;
    }
    public void playerTap(View view){
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());
        if(!gameActive){
            gameReset(view);
        }
        else if(gameState[tappedImage]==2){
            count++;
            if(count==9){
                gameActive=false;
            }
            gameState[tappedImage]=0;
            if(activePlayer==0){
                img.setImageResource(R.drawable.x);
                TextView status = findViewById(R.id.status);
                //Log.d("shankypmovesb","running");
                if(emptySpots(gameState).size()>=1) {
                    int ans=aiMove(gameState);
                    gameState[ans]=1;
                    //Log.d("shankypmovesb",Integer.toString(ans));
                    count++;
                    grid.get(ans).setImageResource(R.drawable.o);
                }
                status.setText("Next Turn");
            }
            int flag=checkWin(gameState);
            if(flag==-1) {
                gameActive=false;
                TextView status = findViewById(R.id.status);
                status.setText("X has Won");
            }
            else if(flag==1){
                gameActive=false;
                TextView status = findViewById(R.id.status);
                status.setText("O has Won");
            }
            else if(count==9&&flag==0){
                TextView status=findViewById(R.id.status);
                status.setText("Match Draw");
            }
        }
    }
    public void gameReset(View view) {
        count=0;
        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            grid.get(i).setImageResource(0);
            gameState[i] = 2;
        }
        TextView status = findViewById(R.id.status);
        status.setText("X's Turn - Tap to play");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid.add(((ImageView) findViewById(R.id.imageView0)));
        grid.add(((ImageView) findViewById(R.id.imageView1)));
        grid.add(((ImageView) findViewById(R.id.imageView2)));
        grid.add(((ImageView) findViewById(R.id.imageView3)));
        grid.add(((ImageView) findViewById(R.id.imageView4)));
        grid.add(((ImageView) findViewById(R.id.imageView5)));
        grid.add(((ImageView) findViewById(R.id.imageView6)));
        grid.add(((ImageView) findViewById(R.id.imageView7)));
        grid.add(((ImageView) findViewById(R.id.imageView8)));
    }
}