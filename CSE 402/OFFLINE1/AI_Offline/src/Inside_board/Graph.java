package Inside_board;
import javafx.util.Pair;

import java.util.ArrayList;

public class Graph {


    public static  final int  ADMISSIBLE=10;
    public static  final int  NON_ADMISSIBLE=11;
    public  static  int findHeuristic(int[][] board,int type)
    {
        if(type==ADMISSIBLE)
        {
            return Dijkstra_Max_Distance(board);
        }
        else if(type==NON_ADMISSIBLE)
        {
            return getAllConnected(board);
        }
        return 0;
    }
    private  static  int getAllConnected(int [][] board)
    {
        boolean [][] visited=new boolean[board.length][board[0].length];
        ArrayList<Pair<Integer,Integer>> nb=getNeighbour(0,0,board[0][0],visited,board);
        int count=0;
        for(int i=0;i<nb.size();i++)
        {
            visited=new boolean[board.length][board[0].length];
            int j,k;
            j=nb.get(i).getKey();
            k=nb.get(i).getValue();

            count+=getConnected(j,k,board[j][k],visited,board);

        }
        return  count;

    }
    private  static  int getConnected(int i,int j,int color,boolean[][] visited,int [][] board)
    {
        int temp=0;
        if(board[i][j]!=color)
        {
            return  temp;
        }
        if(board[i][j]==color && !visited[i][j])
        {
            temp=1;
            visited[i][j]=true;
            if(inBoundary(i-1,j,board)&& !visited[i-1][j])
            {
                temp+=getConnected(i-1,j,color,visited,board);
            }
            if(inBoundary(i+1,j,board)&& !visited[i+1][j])
            {
                temp+=getConnected(i+1,j,color,visited,board);
            }
            if(inBoundary(i,j-1,board)&& !visited[i][j-1])
            {
                temp+=getConnected(i,j-1,color,visited,board);
            }
            if(inBoundary(i,j+1,board)&& !visited[i][j+1])
            {
                temp+=getConnected(i,j+1,color,visited,board);
            }
        }
        return temp;
        
        

    }
    private  static ArrayList<Pair<Integer,Integer>> getNeighbour(int i,int j,int color,boolean[][] visited,int [][]board)
    {
        ArrayList<Pair<Integer,Integer>> nb=new ArrayList<>();
        if(board[i][j]!=color && visited[i][j])
        {
            return nb;
        }
        if(board[i][j]!=color && !visited[i][j])
        {
            visited[i][j]=true;
            nb.add(new Pair<>(i,j));
            return  nb;
        }
        visited[i][j]=true;
        if(inBoundary(i-1,j,board)&& !visited[i-1][j])
        {
            nb.addAll(getNeighbour(i-1,j,color,visited,board));
        }
        if(inBoundary(i+1,j,board)&& !visited[i+1][j])
        {
            nb.addAll(getNeighbour(i+1,j,color,visited,board));
        }
        if(inBoundary(i,j-1,board)&& !visited[i][j-1])
        {
            nb.addAll(getNeighbour(i,j-1,color,visited,board));
        }
        if(inBoundary(i,j+1,board)&& !visited[i][j+1])
        {
            nb.addAll(getNeighbour(i,j+1,color,visited,board));
        }
        return  nb;


    }
    public    static  boolean inBoundary(int i,int j,int[][] node)
    {
        if((i>=0)&&(i<node.length)&&(j>=0)&&(j<node[i].length))
        {

            return  true;

        }
        return false;
    }
    private  static  int Dijkstra_Max_Distance(int[][] board)
    {
       int[][] distance=new int[board.length][board[0].length];
       boolean[][] visited=new boolean[board.length][board[0].length];
       for(int i=0;i<distance.length;i++)
       {
           for(int j=0;j<distance[0].length;j++)
           {
               visited[i][j]=false;
               distance[i][j]=Integer.MAX_VALUE;
           }
       }
       distance[0][0]=0;
        for(int i=0;i<=((board.length)*(board[0].length));i++)
        {
               Pair<Integer,Integer> u=min_distance(visited,distance);
               int i1=u.getKey();
               int j1=u.getValue();
               visited[i1][j1]=true;
               if(
                       inBoundary(i1-1,j1,board) &&
                       (!visited[i1-1][j1])  &&
                       (distance[i1][j1]!=Integer.MAX_VALUE) &&
                       ((distance[i1][j1]+isSame(i1,j1,i1-1,j1,board))<distance[i1-1][j1])
               )
               {
                    distance[i1-1][j1]=(distance[i1][j1]+isSame(i1,j1,i1-1,j1,board));
               }
            if(
                    inBoundary(i1+1,j1,board) &&
                            (!visited[i1+1][j1])  &&
                            (distance[i1][j1]!=Integer.MAX_VALUE) &&
                            ((distance[i1][j1]+isSame(i1,j1,i1+1,j1,board))<distance[i1+1][j1])
            )
            {
                distance[i1+1][j1]=(distance[i1][j1]+isSame(i1,j1,i1+1,j1,board));
            }
            if(
                    inBoundary(i1,j1-1,board) &&
                            (!visited[i1][j1-1])  &&
                            (distance[i1][j1]!=Integer.MAX_VALUE) &&
                            ((distance[i1][j1]+isSame(i1,j1,i1,j1-1,board))<distance[i1][j1-1])
            )
            {
                distance[i1][j1-1]=(distance[i1][j1]+isSame(i1,j1,i1,j1-1,board));
            }
            if(
                    inBoundary(i1,j1+1,board) &&
                            (!visited[i1][j1+1])  &&
                            (distance[i1][j1]!=Integer.MAX_VALUE) &&
                            ((distance[i1][j1]+isSame(i1,j1,i1,j1+1,board))<distance[i1][j1+1])
            )
            {
                distance[i1][j1+1]=(distance[i1][j1]+isSame(i1,j1,i1,j1+1,board));
            }
        }
        return max(distance);

    }
    private static  int max(int [][] distance)
    {
        int max=Integer.MIN_VALUE;
        for(int i=0;i<distance.length;i++)
        {
            for(int j=0;j<distance[i].length;j++)
            {
                if(distance[i][j]>max)
                {
                    max=distance[i][j];
                }
            }
        }
        return  max;
    }
    private static  Pair<Integer,Integer> min_distance(boolean[][] visited,int[][] distance)
    {
        int min=Integer.MAX_VALUE;
        Pair<Integer,Integer> temp;
        int row=0,column=0;
        for(int i=0;i<distance.length;i++)
        {
            for(int j=0;j<distance[0].length;j++)
            {
                if((!visited[i][j])&&(distance[i][j]<=min))
                {
                    row=i;
                    column=j;
                    min=distance[i][j];
                }
            }
        }
        temp=new Pair<>(row,column);
        return  temp;
    }
    private static  int isSame(int i1,int j1,int i2,int j2,int[][] board)
    {
        if((board[i1][j1]==board[i2][j2]))
        {
            return 0;
        }
        return  1;
    }
}
