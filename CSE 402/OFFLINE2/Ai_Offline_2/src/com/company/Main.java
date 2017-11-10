package com.company;

import com.company.helper.Matrix;
import com.company.helper.Pool;
import com.company.helper.Schedule;
import com.company.helper.Slot;


import java.io.*;
import java.util.*;

import static com.company.helper.Miscellaneous.*;

public class Main {

    public static final int k = 4;
    public  static int count;

    public static void main(String[] args) {

        Random random = new Random();
        String fileName = "hdtt4req.txt";
        ArrayList<Slot> slots = fileReader(fileName);
        Long start,end;
        start=System.currentTimeMillis();
        LocalSearch.slotsSize=slots.size();
        Schedule result=LocalSearch.findSolution(slots,LocalSearch.STOCHAISTIC_SEARCH,count);
        end=System.currentTimeMillis();
        System.out.println("Total time: "+((end-start)/1000)+"s");

        printRoutine("Routine.txt",result);
        /*printArray("t1.txt",best_k.get(0).getTeacherClash(0));
        printArray("t2.txt",best_k.get(0).getTeacherClash(1));
        printArray("t3.txt",best_k.get(0).getTeacherClash(2));
        printArray("t4.txt",best_k.get(0).getTeacherClash(3));
        printArray("r1.txt",best_k.get(0).getRoomClash(0));
        printArray("r2.txt",best_k.get(0).getRoomClash(1));
        printArray("r3.txt",best_k.get(0).getRoomClash(2));
        printArray("r4.txt",best_k.get(0).getRoomClash(3));
        printArray("s1.txt",best_k.get(0).getSubjectClash(0));
        printArray("s2.txt",best_k.get(0).getSubjectClash(1));
        printArray("s3.txt",best_k.get(0).getSubjectClash(2));
        printArray("s4.txt",best_k.get(0).getSubjectClash(3));
        printArray("Clash.txt",best_k.get(0).getClashArray());
        System.out.println("Total Clash: "+best_k.get(0).getClashCount());*/

    }
    public static ArrayList<Slot> fileReader(String  fileName)
    {
        File inputFile=new File("Data/"+fileName);
        ArrayList<Slot> allSlots=new ArrayList<>();

        int teacherCount;
        try {
            Scanner scn = new Scanner(inputFile);
            teacherCount=Integer.parseInt(scn.nextLine());
            count=teacherCount;
            String line;
            String lineContent[];
            Slot slot;
            int sub;
            int room;
            for(int i=1;scn.hasNextLine();i++)
            {
                line=scn.nextLine();
                lineContent=line.split("[ ]+");
                sub=i%teacherCount;
                room=(int) Math.ceil((double) i / teacherCount);
                //System.out.println("SUb: "+sub+"Room: "+room);
                for(int j=0;j<lineContent.length;j++)
                {

                    if(sub==0)
                    {
                        sub=teacherCount;
                    }

                    for(int k=0;k<Integer.parseInt(lineContent[j]);k++)
                    {
                        slot=new Slot((j+1)+"",room+"",sub+"");
                        allSlots.add(slot);
                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return allSlots;

    }

}