package com.company.helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Schedule  implements Comparable{
    private  Matrix routine;
    int clashCount;
    int[][][]teacherClash,subjectClash,roomClash;
    int period;
    int day;
    int subjectCount;
    int teacherCount;
    int clashArray[][];
    double fitness;

    public Schedule(int day,int period,int subjectCount,int teacherCount) {
        this.period = period;
        this.day = day;
        this.subjectCount=subjectCount;
        this.teacherCount=teacherCount;
        routine=new Matrix(day,period);
        teacherClash=new int[teacherCount][day][period];
        subjectClash=new int[subjectCount][day][period];
        roomClash=new int[subjectCount][day][period];
        clashCount=0;
        clashArray=new int[day][period];
        fitness=0;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getSubjectCount() {
        return subjectCount;
    }

    public int getTeacherCount() {
        return teacherCount;
    }

    public int[][][] getTeacherClash() {
        return teacherClash;
    }

    public int[][][] getSubjectClash() {
        return subjectClash;
    }

    public int[][][] getRoomClash() {
        return roomClash;
    }

    public Matrix getRoutine() {
        return routine;
    }

    public void setRoutine(Matrix routine) {
        this.routine = routine;
    }

    public int getClashCount() {
        return clashCount;
    }

    public void setClashCount(int clashCount) {
        this.clashCount = clashCount;
    }

    public int[][] getTeacherClash(int teacher) {
        return teacherClash[teacher];
    }

    public void setTeacherClash(int[][] teacherClash,int teacher) {
        this.teacherClash[teacher] = teacherClash;
    }

    public void setTeacherClash(int[][][] teacherClash) {
        this.teacherClash = teacherClash;
    }

    public void setSubjectClash(int[][][] subjectClash) {
        this.subjectClash = subjectClash;
    }

    public void setRoomClash(int[][][] roomClash) {
        this.roomClash = roomClash;
    }

    public int[][] getSubjectClash(int subject) {
        return subjectClash[subject];
    }

    public void setSubjectClash(int[][] subjectClash,int subject) {
        this.subjectClash[subject] = subjectClash;
    }

    public int[][] getRoomClash(int room) {
        return roomClash[room];
    }

    public void setRoomClash(int[][] roomClash,int room) {
        this.roomClash[room] = roomClash;
    }

    public int[][] getClashArray() {
        return clashArray;
    }

    public int getPeriod() {
        return period;
    }
    public int getDay() {
        return day;
    }
    public void updateClash()
    {
        int ac=0;
        int temp=0;
        for(int i=0;i<teacherClash[0].length;i++)
        {

            for(int j=0;j<teacherClash[0][0].length;j++)
            {
                temp=ac;
                for(int k=0;k<teacherClash.length;k++)
                {
                    if(teacherClash[k][i][j]>0)
                    {
                        ac+=teacherClash[k][i][j]-1;
                    }
                    if(subjectClash[k][i][j]>0)
                    {
                        ac+=subjectClash[k][i][j]-1;
                    }
                    if(roomClash[k][i][j]>0)
                    {
                        ac+=roomClash[k][i][j]-1;
                    }
                }
                clashArray[i][j]=ac-temp;
            }
        }

        clashCount=ac;
    }



    @Override
    public boolean equals(Object obj) {
        Schedule t=(Schedule)obj;
        return this.routine.equals(t.getRoutine());
    }

    @Override
    public String toString() {
        ArrayList<Slot> currentSlot;
        String  toPrint="";
        for(int i=0;i<routine.getDay();i++)
        {
            for(int j=0;j<routine.getPeriod();j++)
            {
                currentSlot=routine.getPos(i,j);
                toPrint+="|";
                if(currentSlot.size()==0)
                {
                    toPrint+="Empty";
                }
                for(int k=0;k<currentSlot.size();k++)
                {
                    toPrint+=currentSlot.get(k).toString()+", ";
                }
                toPrint+="|";
            }
            toPrint+="\n\n\n\n";
        }
        return toPrint;
    }


    @Override
    public int compareTo(Object o) {
        Schedule t=(Schedule)o;
        return (new Integer(this.getClashCount())).compareTo(t.getClashCount());
    }
}
