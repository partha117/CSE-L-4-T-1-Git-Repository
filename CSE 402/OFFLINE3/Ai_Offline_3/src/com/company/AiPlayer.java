package com.company;

import com.company.helper.Board;
import com.company.helper.Utility;

import java.util.ArrayList;

public class AiPlayer {
    public  static  final  int maxDepth=15;
    private Board board;
    private  int strategy;
    private  int heuristic;


    public AiPlayer(Board board, int strategy,int heuristic) {
        this.board = board;
        this.strategy=strategy;
        this.heuristic=heuristic;

    }
    public boolean player2Move(int pit)
    {
        pit--;
        if(pit>=board.getBoardSize()|| pit<0)
        {
            System.out.println("Not a valid move");
        }
        int temp=board.action(Utility.player2,pit);
        if(temp==Utility.doubleChance)
        {
            return  true;
        }
        return  false;
    }
    public  void provideMove()
    {
        int move;
        int moveType;
        if((strategy==Utility.alphaBetaWithMoveOrdering)||(strategy==Utility.alphaBetaWithoutMoveOrdering))
        {
            move=alphaBetaSearch();
            System.out.println("Computer provides move "+(move+1));
            moveType=board.action(Utility.player1,move);
            for(;moveType==Utility.doubleChance;)
            {
                if(board.isTerminal())
                {
                    return;
                }
                move=alphaBetaSearch();
                System.out.println("Computer provides move "+(move+1));
                moveType=board.action(Utility.player1,move);
            }
        }
    }
    private int alphaBetaSearch()
    {
        ArrayList<Integer> allMoves=getAllMoves(board,Utility.player1);
        Board tempBoard;
        double maxValue=Double.NEGATIVE_INFINITY;
        int move=0;
        double tempValue;
        for(int i:allMoves)
        {
            tempBoard=board.copy();
            tempBoard.action(Utility.player1,i);
            tempValue=minValue(tempBoard,maxDepth,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,1);
            if(maxValue<=tempValue)
            {
                maxValue=tempValue;
                move=i;
            }
        }

        return  move;

    }
    private double maxValue(Board board,int depth,double alpha,double beta,int currentDepth)
    {
        double v;
        Board myBoard;
        int actionResult;
        if(currentDepth==depth)
        {
            return evaluatingFunction(board,Utility.player1);
        }
        if(board.isTerminal())
        {
            return evaluatingFunction(board,Utility.player1);
        }
        v=Double.NEGATIVE_INFINITY;
        ArrayList<Integer> allMoves=getAllMoves(board,Utility.player1);
        for(int i:allMoves)
        {
            myBoard=board.copy();
            actionResult=myBoard.action(Utility.player1,i);
            if(actionResult==Utility.doubleChance)
            {
                //doubleMove(myBoard,Utility.player1);
                v = Double.max(v, maxValue(myBoard, depth, alpha, beta, currentDepth + 1));
            }
            else {
                v = Double.max(v, minValue(myBoard, depth, alpha, beta, currentDepth + 1));
            }
            if(v>=beta)
            {
                return v;
            }
            alpha=Double.max(alpha,v);
        }
        return v;
    }
    private double minValue(Board board,int depth,double alpha,double beta,int currentDepth)
    {
        double v;
        Board myBoard;
        int actionResult;
        if(currentDepth==depth)
        {
            return evaluatingFunction(board,Utility.player2);
        }
        if(board.isTerminal())
        {
            return evaluatingFunction(board,Utility.player2);
        }
        v=Double.POSITIVE_INFINITY;
        ArrayList<Integer> allMoves=getAllMoves(board,Utility.player2);
        for(int i:allMoves)
        {
            myBoard=board.copy();
            actionResult=myBoard.action(Utility.player2,i);
            if(actionResult==Utility.doubleChance)
            {
                //doubleMove(myBoard,Utility.player2);
                v = Double.min(v, minValue(myBoard, depth, alpha, beta, currentDepth + 1));
            }
            else {
                v = Double.min(v, maxValue(myBoard, depth, alpha, beta, currentDepth + 1));
            }
            if(v<=alpha)
            {
                return v;
            }
            beta=Double.min(beta,v);
        }
        return v;
    }
    private void doubleMove(Board board, int player)
    {
        int []pitArray;
        Board tempBoard;
        double tempScore;
        double score;
        int move=0;
        if(player==Utility.player1)
        {
            pitArray=board.getPlayer1Pit();
            score=Double.NEGATIVE_INFINITY;
        }
        else
        {
            pitArray=board.getPlayer2Pit();
            score=Double.POSITIVE_INFINITY;
        }
        ArrayList<Integer> allMoves=getAllMoves(board,player);
        for(int i:allMoves)
        {
            tempBoard=board.copy();
            tempBoard.action(player,i);
            tempScore=evaluatingFunction(board,player);
            if(player==Utility.player1)
            {
                score=Double.max(score,tempScore);
                move=i;
            }
            else
            {
                score=Double.min(score,tempScore);
                move=i;
            }
        }
        board.action(player,move);
    }
    private   double utilityFunction()
    {
        return board.getPlayer1Store()-board.getPlayer2Store();
    }
    private  double evaluatingFunction(Board board,int player)
    {
        int player1gem,player2gem;
        player1gem=player2gem=0;
        int functionValue=0;
        for(int i=0;i<board.getBoardSize();i++)
        {
            player1gem+=board.getPlayer1Pit()[i];
            player2gem+=board.getPlayer2Pit()[i];
        }
        if(heuristic==Utility.HeuristicOne)
        {
            functionValue=(board.getPlayer1Store()-board.getPlayer2Store())+(player1gem-player2gem);
        }
        else if(heuristic==Utility.HeuristicTwo)
        {
            functionValue=(board.getPlayer1Store()-board.getPlayer2Store());
        }
        else if(heuristic==Utility.HeuristicThree)
        {
            functionValue=(board.getPlayer1Store()-board.getPlayer2Store())+(player1gem-player2gem);
        }
        else if(heuristic==Utility.HeuristicFour)
        {
            functionValue=(board.getPlayer1Store()-board.getPlayer2Store())+(player1gem-player2gem);
        }

        /*if(player==Utility.player1)
        {
            functionValue=(board.getPlayer1Store()-board.getPlayer2Store())+(player1gem-player2gem);
        }
        else
        {
            functionValue=(board.getPlayer2Store()-board.getPlayer1Store())+(player2gem-player1gem);
        }*/
        return  functionValue;
    }
    private ArrayList<Integer>  getAllMoves(Board board1,int player)
    {
        Board myBoard=board1.copy();//TODO: Check is it unnecessary?
        ArrayList <Integer> moves=new ArrayList<>();
        ArrayList<Integer> temp=new ArrayList<>();
        ArrayList <Integer> nonEmpty=new ArrayList<>();
        int [] pitArray;
        int [] opponentArray;
        if(player==Utility.player1)
        {
            pitArray=myBoard.getPlayer1Pit();
            opponentArray=myBoard.getPlayer2Pit();
        }
        else {
            pitArray=myBoard.getPlayer2Pit();
            opponentArray=myBoard.getPlayer1Pit();
        }
        for(int i=0;i<myBoard.getBoardSize();i++)
        {
            if(pitArray[i]!=0)
            {
                nonEmpty.add(i);
            }
        }
        if(strategy==Utility.alphaBetaWithoutMoveOrdering)
        {
            return nonEmpty;
        }
        for(int i=0;i<nonEmpty.size();i++)
        {
            if(pitArray[nonEmpty.get(i)]==nonEmpty.get(i)+1)
            {
                temp.add(nonEmpty.get(i));
                moves.add(nonEmpty.get(i));

            }
        }
        nonEmpty.removeAll(temp);
        temp.clear();
        //TODO:capture
        for(int i:nonEmpty)
        {
            Board myboard2 = myBoard.copy();
            if (myboard2.action(player, i) == Utility.grabOppositeCell) {
                temp.add(i);
                moves.add(i);
            }

        }
        nonEmpty.removeAll(temp);
        temp.clear();
        if((pitArray[myBoard.getBoardSize()-1]!=0)&&(nonEmpty.contains(myBoard.getBoardSize()-1)))
        {
            nonEmpty.remove(nonEmpty.indexOf(myBoard.getBoardSize()-1));
            moves.add(myBoard.getBoardSize()-1);
        }
        if((pitArray[myBoard.getBoardSize()-2]!=0)&&(nonEmpty.contains(myBoard.getBoardSize()-2)))
        {
            nonEmpty.remove(nonEmpty.indexOf(myBoard.getBoardSize()-2));
            moves.add(myBoard.getBoardSize()-2);
        }
        for(int i:nonEmpty)
        {
            moves.add(i);
        }
        return  moves;
    }



}
