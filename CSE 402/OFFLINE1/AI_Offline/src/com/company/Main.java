package com.company;

import Game_Playing.SearchNode;
import Inside_board.Graph;

import java.io.*;
import java.util.*;

public class Main {

    public  static  final  int NO_COLOR=-1;
    public  static int heuristic_type=Graph.ADMISSIBLE;
    public static void main(String[] args) {

        ArrayList<SearchNode> pq=new ArrayList<>();
        int initialBoard[][]=loadGame("input.txt");
        SearchNode initialSearchNode=new SearchNode(null,initialBoard,NO_COLOR,0, 0);
        pq.add(initialSearchNode);
        SearchNode goal=letTheGameBegin(pq);
        output(goal);
    }
    private  static SearchNode letTheGameBegin(ArrayList<SearchNode> pq)
    {
        ArrayList<SearchNode> expanded=new ArrayList<>();
        for(;!pq.isEmpty();)
        {
            SearchNode currentNode=pq.remove(pq.size()-1);
            expanded.add(currentNode);
            if(isGoal(currentNode))
            {
                return currentNode;
            }
            else
            {
                ArrayList<SearchNode> neighbours=getNeighbours(currentNode);
                for(int i=0;i<neighbours.size();i++)
                {
                    if(expanded.contains(neighbours.get(i)))
                    {
                        continue;
                    }
                    else
                    {
                        int index=pq.indexOf(neighbours.get(i));
                        if((index!=-1)&&(pq.get(index).getF()>neighbours.get(i).getF()))
                        {
                            pq.remove(index);
                        }
                        pq.add(neighbours.get(i));
                    }
                }
                pq.sort(new Comparator<SearchNode>() {
                    @Override
                    public int compare(SearchNode o1, SearchNode o2) {
                        return (-1)*Integer.compare(o1.getF(),o2.getF());
                    }
                });
            }

        }
        return  null;
    }

    private  static  ArrayList<SearchNode> getNeighbours(SearchNode node)
    {
        ArrayList<SearchNode> neighbours=new ArrayList<>();
        boolean found[]=new boolean[7];
        for(int i=0;i<node.getBoard().length;i++)
        {
            for(int j=0;j<node.getBoard()[i].length;j++)
            {
                found[node.getBoard()[i][j]]=true;
            }
        }
        for(int i=1;i<found.length;i++)
        {
            if((!found[i])||(i==node.getBoard()[0][0]))
            {
                continue;
            }
            else
            {
                int [][] board=copyBoard(node.getBoard());
                colorRegion(0,0,i,board,board[0][0]);
                SearchNode temp=new SearchNode(node,board,i,node.getG()+1,Graph.findHeuristic(board,heuristic_type));
                neighbours.add(temp);
            }
        }
        return neighbours;
    }
    private static int[][] copyBoard(int[][] board)
    {
        int [][] temp;
        temp=new int[board.length][board[0].length];
        for(int i=0;i<board.length;i++)
        {
            for(int j=0;j<board[i].length;j++)
            {
                temp[i][j]=board[i][j];
            }
        }
        return temp;
    }
    private  static void colorRegion(int i,int j,int color,int [][] board,int pastColor)
    {
        if(!Graph.inBoundary(i,j,board))
        {
            return ;
        }
        if(board[i][j]!=pastColor)
        {
            return ;
        }
        board[i][j]=color;
        colorRegion(i+1,j,color,board,pastColor);
        colorRegion(i-1,j,color,board,pastColor);
        colorRegion(i,j-1,color,board,pastColor);
        colorRegion(i,j+1,color,board,pastColor);
    }
    private   static  boolean isGoal(SearchNode searchNode)
    {
        int color=searchNode.getBoard()[0][0];
        for(int i=0;i<searchNode.getBoard().length;i++)
        {
            for(int j=0;j<searchNode.getBoard()[i].length;j++)
            {
                if(searchNode.getBoard()[i][j]!=color)
                {
                    return  false;
                }
            }
        }
        return true;
    }
    private   static  int[][] loadGame(String  fileName)
    {
        int game[][]=null;
        File inputFile=new File(fileName);
        try {
            Scanner scn=new Scanner(inputFile);
            int dimension=Integer.parseInt(scn.nextLine());
            game=new int[dimension][dimension];
            String  line;
            String [] lineContent;
            for(int i=0;i<dimension;i++)
            {
                line=scn.nextLine();
                lineContent=line.split("");
                for(int j=0;j<dimension;j++)
                {
                    game[i][j]=Integer.parseInt(lineContent[j]);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return game;
    }
    private  static  void output(SearchNode goal)
    {
        FileWriter outputFile= null;
        try
        {
            outputFile = new FileWriter("solution.txt");
            BufferedWriter bufferedWriter=new BufferedWriter(outputFile );
            if(goal==null)
            {
                System.out.println("Goal is not achieved");
            }
            else
            {
                Stack<SearchNode> result=new Stack<>();
                ArrayList<Integer> steps=new ArrayList<>();
                for(;goal!=null;)
                {
                    result.push(goal);
                    steps.add(goal.getColor());
                    goal=goal.getParent();
                }
                steps.remove(steps.size()-1);
                bufferedWriter.write("Total steps: "+steps.size()+"\n");
                for(int i=steps.size()-1;i>=0;i--)
                {
                    bufferedWriter.write(steps.get(i)+"  ");
                }
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                int l=steps.size()-1;
                for(int k=0;!result.isEmpty();k++)
                {
                    int [][]temp=result.pop().getBoard();
                    if(k==0)
                    {
                        bufferedWriter.write("Initial State \n");
                        bufferedWriter.write("\n\n\n\n");
                    }
                    else
                    {
                        bufferedWriter.write("Step no: "+k+" step taken: "+steps.get(l));
                        l--;
                        bufferedWriter.write("\n\n\n\n");
                    }
                    for(int i=0;i<temp.length;i++)
                    {
                        for(int j=0;j<temp[i].length;j++)
                        {
                            bufferedWriter.write(temp[i][j]+"  ");
                        }
                        bufferedWriter.write("\n\n");
                    }
                }
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
