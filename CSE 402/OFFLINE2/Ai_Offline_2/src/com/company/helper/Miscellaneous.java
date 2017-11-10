package com.company.helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public  class Miscellaneous {
    public static void printArray(String  fileName,int [][] array)
    {

        FileWriter outputFile= null;
        try {
            outputFile = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(outputFile);
            for(int i=0;i<array.length;i++)
            {
                for(int j=0;j<array[i].length;j++)
                {
                    bufferedWriter.write(array[i][j]+"  ");
                }
                bufferedWriter.write("\n");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void printRoutine(String  fileName,Schedule schedule)
    {

        FileWriter outputFile= null;
        try {
            outputFile = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(outputFile);
            bufferedWriter.write(schedule.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  static int[][][]  arrayCopy(int [][][] array)
    {
        int temp[][][]=new int[array.length][array[0].length][array[0][0].length];
        for(int i=0;i<array.length;i++)
        {
            for(int j=0;j<array[0].length;j++)
            {
                for(int k=0;k<array[0][0].length;k++)
                {
                    temp[i][j][k]=array[i][j][k];
                }
            }
        }
        return temp;
    }
    public  static  boolean inBoundary(int day,int period,int i,int j)
    {
        if((i>=0)&&(i<day)&&(j>=0)&&(j<period))
        {
            return true;
        }
        return false;
    }
    public  static  int getCount(ArrayList<Slot> [][]m2,Slot target)
    {
        int count=0;
        for(int i=0;i<m2.length;i++)
        {
            for(int j=0;j<m2[i].length;j++)
            {
                for(int k=0;k<m2[i][j].size();k++)
                {
                    if(m2[i][j].get(k).equals(target))
                    {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public  static  void delete(ArrayList<Slot> [][]m2,Slot target,int num)
    {
        int count=0;

        for(int i=0;i<m2.length;i++)
        {
            for(int j=0;j<m2[i].length;j++)
            {
                for(int k=0;k<m2[i][j].size();k++)
                {
                    if(m2[i][j].get(k).equals(target))
                    {
                        count++;
                        m2[i][j].remove(k);
                        k--;
                        if(num==count)
                        {
                            return;
                        }
                    }
                }
            }
        }
    }
    public  static void completeAllArrays(Schedule child)
    {
        ArrayList<Slot> m2[][]=child.getRoutine().getMatrix();
        int[][][] teacher = child.getTeacherClash();
        int[][][] subject = child.getSubjectClash();
        int[][][] room = child.getRoomClash();
        int t,r,s;
        Slot temp;
        for(int i=0;i<m2.length;i++)
        {
            for(int j=0;j<m2[i].length;j++)
            {
                for(int k=0;k<m2[i][j].size();k++)
                {
                    temp=m2[i][j].get(k);
                    r=Integer.parseInt(temp.getRoom())-1;
                    t=Integer.parseInt(temp.getTeacher())-1;
                    s=Integer.parseInt(temp.getSubject())-1;
                    teacher[t][i][j]++;
                    room[r][i][j]++;
                    subject[s][i][j]++;
                }
            }
        }
        child.updateClash();
    }
    public   static  boolean isAllAvailable(Schedule child,int slots)
    {
        ArrayList<Slot> m2[][]=child.getRoutine().getMatrix();
        int acc=0;
        for(int i=0;i<m2.length;i++) {
            for (int j = 0; j < m2[i].length; j++) {
                acc+=m2[i][j].size();
            }
        }
        System.out.println(acc);
        if(acc<slots)
        {
            return  false;
        }
        return  true;
    }
    public  static  int tournamentSelection(ArrayList<Schedule> best_k,int tournamentSize)
    {
        int bestVal=Integer.MAX_VALUE,pos,bestPos=0;

        Random random=new Random();
        for(int i=0;i<tournamentSize;i++)
        {
            pos=random.nextInt(best_k.size()-1);

            if(bestVal>best_k.get(pos).getClashCount())
            {

                bestVal=best_k.get(pos).getClashCount();
                bestPos=pos;
            }
        }
        return  bestPos;
    }

}
