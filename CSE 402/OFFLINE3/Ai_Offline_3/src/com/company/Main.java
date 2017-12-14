package com.company;

import com.company.helper.Board;
import com.company.helper.Utility;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
	// write your code here
        Board board=new Board(6,4);
        AiPlayer aiPlayer=new AiPlayer(board,Utility.alphaBetaWithMoveOrdering,Utility.HeuristicOne);
        Scanner scanner=new Scanner(System.in);
        int input;
        boolean moveType;
        for(;!board.isTerminal();)
        {
            aiPlayer.provideMove();
            if(board.isTerminal())
            {
                break;
            }
            System.out.println(board);
            moveType=true;
            for(;moveType&&(!board.isTerminal());)
            {
                input=scanner.nextInt();
                moveType=aiPlayer.player2Move(input);
                System.out.println(board);
            }
        }
        board.terminalStep();
        System.out.println(board.winnerDeclare());
        System.out.println(board);


    }
}
