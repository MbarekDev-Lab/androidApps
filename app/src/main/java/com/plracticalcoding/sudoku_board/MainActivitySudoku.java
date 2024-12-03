package com.plracticalcoding.sudoku_board;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.plracticalcoding.myapplication.R;

public class MainActivitySudoku extends AppCompatActivity {
    private SudokuBoard gameBoard;
    private Solver gameBoardSolver;
    private Button solveBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        gameBoard = findViewById(R.id.SudokuBoard);
        solveBTN = findViewById(R.id.solveBTN);

        gameBoardSolver = gameBoard.getSolver();
    }
    public void BTNOnePress(View view){
        gameBoardSolver.setNumberPos(1);
        gameBoard.invalidate();
    }

    public void BTNTwoPress(View view){
        gameBoardSolver.setNumberPos(1);
        gameBoard.invalidate();
    }
    public void BTNThreePress(View view){
        gameBoardSolver.setNumberPos(1);
        gameBoard.invalidate();
    }

    public void BTNFourPress(View view){
        gameBoardSolver.setNumberPos(1);
        gameBoard.invalidate();
    }
    public void BTNFivePress(View view){
        gameBoardSolver.setNumberPos(1);
        gameBoard.invalidate();
    }

    public void BTNDSixPress(View view){
        gameBoardSolver.setNumberPos(1);
        gameBoard.invalidate();
    }
    public void BTNDsevenPress(View view){
        gameBoardSolver.setNumberPos(1);
        gameBoard.invalidate();
    }

    public void BTNEightPress(View view){
        gameBoardSolver.setNumberPos(1);
        gameBoard.invalidate();
    }

    public void BTNNinePress(View view){
        gameBoardSolver.setNumberPos(1);
        gameBoard.invalidate();
    }

    public void  solve(View view){
        if (solveBTN.getText().toString().equals(getString(R.string.solve))){
            solveBTN.setText(getString(R.string.clear));
        }else {
            solveBTN.setText(getString(R.string.solve));
        }
    }

}