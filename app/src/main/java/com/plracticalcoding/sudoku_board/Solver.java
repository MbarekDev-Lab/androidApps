package com.plracticalcoding.sudoku_board;

import java.util.ArrayList;

public class Solver {

    int[][] board;
    ArrayList<ArrayList<Object>> emptyBoxIndex;
    int selected_row;
    int selected_column;
    public Solver() {
        selected_row = -1;
        selected_column = -1;

        board = new  int[9][9];
        for (int r = 0; r < 9; r++){
            for (int c= 0; c<9; c++){
                board[r][c] = 0;
            }
        }
        emptyBoxIndex = new ArrayList<>();

    }
     public  void getEmptyBoxIndexs(){
        for (int r = 0; r < 9; r++){
            for (int c= 0; c<9; c++){
                if (this.board[r][c] == 0){
                    this.emptyBoxIndex.add(new ArrayList<>());
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size() -1).add(r);
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size() -1).add(c);

                }
            }
        }
    }

    public void setNumberPos(int num){
        if (this.getSelected_row() != -1 && this.selected_column != -1){
            if (this.board[this.selected_row-1][this.selected_column-1] == num){
                this.board[this.selected_row-1][this.selected_column-1] = 0;

            }else {
                this.board[this.selected_row-1][this.selected_column-1] = num;
            }
        }
    }

    public int[][] getBoard(){
        return this.board;
    }

    public int getSelected_row(){
        return  selected_row;
    }

    public int getSelected_column(){
        return  selected_column;
    }

    public void setSelected_row(int row){
        selected_row = row;

    }
    public void setSelected_column(int column){
        selected_row = column;
    }
    public ArrayList<ArrayList<Object>> getEmptyBoxIndex() {
        return this.emptyBoxIndex;
    }

}
