package com.company.helper;

import java.util.ArrayList;

public class Board {
    private int[] player1Pit;
    private int[] player2Pit;
    private  int player1Store;
    private int player2Store;
    private  int boardSize;
    private  int initialGem;
    private int player1DoubleMovesCount;
    private int player2DoubleMovesCount;
    private int player1DoubleGrabOppositeMoveCount;
    private int player2DoubleGrabOppositeMoveCount;
    private int player1DoubleGrabOppositeMoveAmount;
    private int player2DoubleGrabOppositeMoveAmount;


    public Board(int boardSize,int initialGem) {
        player1Pit=new int[boardSize];
        player2Pit=new int[boardSize];
        for(int i=0;i<boardSize;i++)
        {
            player1Pit[i]=initialGem;
            player2Pit[i]=initialGem;
        }
        player1Store=0;
        player2Store=0;
        this.boardSize=boardSize;
        this.initialGem=initialGem;
        player2DoubleMovesCount=player2DoubleGrabOppositeMoveCount=player2DoubleGrabOppositeMoveAmount=0;
        player1DoubleMovesCount=player1DoubleGrabOppositeMoveCount=player1DoubleGrabOppositeMoveAmount=0;

    }

    public  int action(int player,int pit)
    {
        if(player==Utility.player1)
        {
            int gemCount=player1Pit[pit];
            int temp;
            player1Pit[pit]=0;
            if(gemCount==0)
            {
                return Utility.noMovePossible;
            }
            for(int i=pit-1;(i>=0)&&(gemCount>0);i--)
            {
                player1Pit[i]=player1Pit[i]+1;
                gemCount--;
                if((player1Pit[i]==1)&&(gemCount==0)&&(player2Pit[i]!=0))
                {
                    player1DoubleGrabOppositeMoveCount++;
                    player1DoubleGrabOppositeMoveAmount+=player2Pit[i];
                    player1Store+=player2Pit[i]+1;
                    player2Pit[i]=0;
                    player1Pit[i]=0;
                    return Utility.grabOppositeCell;
                }
            }
            if(gemCount>0)
            {
                gemCount--;
                player1Store++;
                if(gemCount==0)
                {
                    player1DoubleMovesCount++;
                    return Utility.doubleChance;
                }
            }
            for(int i=0;(i<boardSize)&&(gemCount>0);i++)
            {
                player2Pit[i]=player2Pit[i]+1;
                gemCount--;
            }
            for(;gemCount>0;)
            {
                for(int i=boardSize-1;(i>=0)&&(gemCount>0);i--)
                {
                    player1Pit[i]=player1Pit[i]+1;
                    gemCount--;
                    if((player1Pit[i]==1)&&(gemCount==0)&&(player2Pit[i]!=0))
                    {
                        player1DoubleGrabOppositeMoveCount++;
                        player1DoubleGrabOppositeMoveAmount+=player2Pit[i];
                        player1Store+=player2Pit[i]+1;
                        player2Pit[i]=0;
                        player1Pit[i]=0;
                        return Utility.grabOppositeCell;
                    }
                }
                if(gemCount>0) {
                    player1Store++;
                    gemCount--;
                    if(gemCount==0)
                    {
                        player1DoubleMovesCount++;
                        return Utility.doubleChance;
                    }
                }
                for(int i=0;(i<boardSize)&&(gemCount>0);i++)
                {
                    player2Pit[i]=player2Pit[i]+1;
                    gemCount--;
                }
            }
            //TODO: terminal case
        }
        else if(player==Utility.player2)
        {
            int gemCount=player2Pit[pit];
            player2Pit[pit]=0;
            if(gemCount==0)
            {
                return Utility.noMovePossible;
            }
            for(int i=pit+1;(i<boardSize)&&(gemCount>0);i++)
            {
                player2Pit[i]=player2Pit[i]+1;
                gemCount--;
                if((player2Pit[i]==1)&&(gemCount==0)&&(player1Pit[i]!=0))
                {
                    player2DoubleGrabOppositeMoveCount++;
                    player2DoubleGrabOppositeMoveAmount+=player1Pit[i];
                    player2Store+=player1Pit[i]+1;
                    player1Pit[i]=0;
                    player2Pit[i]=0;
                    return Utility.grabOppositeCell;
                }

            }
            if(gemCount>0)
            {
                gemCount--;
                player2Store++;
                if(gemCount==0)
                {
                    player2DoubleMovesCount++;
                    return Utility.doubleChance;
                }
            }
            for(int i=boardSize-1;(i>=0)&&(gemCount>0);i--)
            {
                player1Pit[i]=player1Pit[i]+1;
                gemCount--;
            }
            for(;gemCount>0;)
            {
                for(int i=0;(i<boardSize)&&(gemCount>0);i++)
                {
                    player2Pit[i]=player2Pit[i]+1;
                    gemCount--;
                    if((player2Pit[i]==1)&&(gemCount==0)&&(player1Pit[i]!=0))
                    {
                        player2DoubleGrabOppositeMoveAmount++;
                        player2DoubleGrabOppositeMoveAmount+=player1Pit[i];
                        player2Store+=player1Pit[i]+1;
                        player1Pit[i]=0;
                        player2Pit[i]=0;

                        return Utility.grabOppositeCell;
                    }
                }
                if(gemCount>0)
                {
                    player2Store++;
                    gemCount--;
                    if(gemCount==0)
                    {
                        player2DoubleMovesCount++;
                        return Utility.doubleChance;
                    }
                }
                for(int i=boardSize-1;(i>=0)&&(gemCount>0);i--)
                {
                    player1Pit[i]=player1Pit[i]+1;
                    gemCount--;
                }
            }

        }
        return Utility.normalMove;
    }


    public  void terminalStep()
    {
        int i=0,gemCount=0;
        for(i=0;i<boardSize;i++)
        {
            if(player1Pit[i]!=0)
            {
                break;
            }
        }
        if(i==boardSize)
        {
            for(i=0;i<boardSize;i++)
            {
                gemCount+=player2Pit[i];
            }
            player2Store+=gemCount;
            return;
        }
        for(i=0;i<boardSize;i++)
        {
            if(player2Pit[i]!=0)
            {
                break;
            }
        }
        if(i==boardSize)
        {
            for(i=0;i<boardSize;i++)
            {
                gemCount+=player1Pit[i];
            }
            player1Store+=gemCount;
            return;
        }

    }
    public String  winnerDeclare()
    {
        String  temp="";
        if(player1Store>player2Store)
        {
            temp="Computer wins";
        }
        else if(player2Store>player1Store)
        {
            temp="opponent wins";
        }
        else
        {
            temp="Game draw";
        }
        return  temp;
    }
    public int[] getPlayer1Pit() {
        return player1Pit;
    }

    public void setPlayer1Pit(int[] player1Pit) {
        this.player1Pit = player1Pit;
    }

    public int[] getPlayer2Pit() {
        return player2Pit;
    }

    public void setPlayer2Pit(int[] player2Pit) {
        this.player2Pit = player2Pit;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getPlayer1Store() {
        return player1Store;
    }

    public void setPlayer1Store(int player1Store) {
        this.player1Store = player1Store;
    }

    public int getPlayer2Store() {
        return player2Store;
    }

    public void setPlayer2Store(int player2Store) {
        this.player2Store = player2Store;
    }
    public Board copy()
    {
        Board temp=new Board(boardSize,initialGem);
        int []temp1Array=new int[boardSize];
        int []temp2Array=new int[boardSize];
        temp.setPlayer1Store(player1Store);
        temp.setPlayer2Store(player2Store);
        for(int i=0;i<boardSize;i++)
        {
            temp1Array[i]=player1Pit[i];
            temp2Array[i]=player2Pit[i];
        }
        temp.setPlayer1Pit(temp1Array);
        temp.setPlayer2Pit(temp2Array);
        return temp;

    }
    public  boolean isTerminal()
    {
        boolean p1,p2;
        p1=true;
        p2=true;
        for(int i=0;i<boardSize;i++)
        {
            if(player1Pit[i]!=0)
            {
                p1=false;
            }
        }
        for(int i=0;i<boardSize;i++)
        {
            if(player2Pit[i]!=0)
            {
                p2=false;
            }
        }
        return(p1|p2);
    }

    @Override
    public String toString() {
        String temp="";
        temp+="Opponent Store: "+player2Store+"\n";
        for(int i=0;i<boardSize;i++)
        {
            temp+="___";
        }
        temp+="\n";
        for(int i=boardSize-1;i>=0;i--)
        {
            temp+="|"+player2Pit[i];
        }
        temp+="|\n";
        for(int i=0;i<boardSize;i++)
        {
            temp+="---";
        }
        temp+="\n";

        temp+="Computer Store: "+player1Store+"\n";
        for(int i=0;i<boardSize;i++)
        {
            temp+="___";
        }
        temp+="\n";
        for(int i=boardSize-1;i>=0;i--)
        {
            temp+="|"+player1Pit[i];
        }
        temp+="|\n";
        for(int i=0;i<boardSize;i++)
        {
            temp+="---";
        }
        temp+="\n";
        return temp;


    }
}
