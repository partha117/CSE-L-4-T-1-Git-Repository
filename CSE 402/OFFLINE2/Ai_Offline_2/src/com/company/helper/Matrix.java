package com.company.helper;

import java.util.ArrayList;

public class Matrix {
    private int period;
    private int day;
    private ArrayList<Slot> matrix[][];

    public Matrix( int day,int period) {
        this.period = period;
        this.day = day;
        matrix= (ArrayList<Slot>[][])new ArrayList[day][period];
        for(int i=0;i<day;i++)
        {
            for(int j=0;j<period;j++)
            {
                matrix[i][j]=new ArrayList<>();
            }
        }
    }

    public Matrix(ArrayList<Slot>[][] matrix) {
        this.matrix = matrix;
        this.day=matrix.length;
        this.period=matrix[0].length;
    }

    public int getPeriod() {
        return period;
    }



    public int getDay() {
        return day;
    }


    public ArrayList<Slot>[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(ArrayList<Slot>[][] matrix) {
        this.matrix = matrix;
    }

    public ArrayList<Slot> getPos(int day,int period)
    {
        return matrix[day][period];
    }
    public void setPos(int day,int period,Slot slot)
    {
        matrix[day][period].add(slot);
    }

    @Override
    public boolean equals(Object obj) {

        Matrix temp=(Matrix)obj;
        if((period==temp.getPeriod())&&(day==temp.getDay()))
        {
            for(int i=0;i<day;i++)
            {
                for(int j=0;j<period;j++)
                {
                    if(matrix[i][j].size()!=temp.getMatrix()[i][j].size())
                    {
                        //System.out.println("List size does not match");
                        return false;
                    }
                    for(int k=0;k<matrix[i][j].size();k++)
                    {
                        if(!matrix[i][j].get(k).equals(temp.getPos(i,j).get(k)))
                        {
                           // System.out.println("Mismatch at "+i+" "+j+" "+k);
                            return false;
                        }
                    }

                }
            }
            return true;
        }
        return false;
    }
    public ArrayList<Slot>[][]copy()
    {
        ArrayList<Slot> nmatrix[][];
        nmatrix= (ArrayList<Slot>[][])new ArrayList[day][period];
        for(int i=0;i<day;i++)
        {
            for(int j=0;j<period;j++)
            {
                nmatrix[i][j]=new ArrayList<>();
            }
        }
        for(int i=0;i<day;i++)
        {
            for(int j=0;j<period;j++)
            {
                for(int k=0;k<matrix[i][j].size();k++)
                {
                    Slot tempSlot=matrix[i][j].get(k);
                    Slot nSlot=new Slot(tempSlot.getTeacher(),tempSlot.getRoom(),tempSlot.getSubject());
                    nmatrix[i][j].add(nSlot);
                }

            }
        }
        return nmatrix;
    }
}
