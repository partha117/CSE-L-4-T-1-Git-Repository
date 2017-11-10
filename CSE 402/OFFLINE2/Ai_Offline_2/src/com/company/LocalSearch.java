package com.company;
import com.company.helper.Matrix;
import com.company.helper.Pool;
import com.company.helper.Schedule;
import com.company.helper.Slot;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static com.company.helper.Miscellaneous.*;


public class LocalSearch {


    public static final int numberOfPeriod = 5;
    public static final int numberOfDay = 6;
    public static final int k = 4;
    private static int count = 0;
    public  static  final int STOCHAISTIC_SEARCH=30;
    public  static  final int NORMAL_SEARCH=31;
    public  static  final int GENETIC_ALGORITHM=32;
    public  static Random random;
    public  static  int tryLimit=80;//Very useful find a good value 80,115 seems work for some case
    public  static  boolean saturationPoint=false;
    private  static Pool<Integer> bestPool;
    public  static  int slotsSize;
    public static double deathLimit=50;
    

    public  static  Schedule findSolution(ArrayList<Slot> slots, int solutionMethod,int teacherCount)
    {
        random=new Random();
        count=teacherCount;
        bestPool=new Pool<>(10);
        ArrayList<Schedule> best_k = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            best_k.add(randomScheduleGenerator(slots));
        }
        
        if(solutionMethod==STOCHAISTIC_SEARCH)
        {
            return stochaisticLoaclSearch(best_k);
        }
        else if(solutionMethod==NORMAL_SEARCH)
        {
            return  localSearch(best_k);
        }
        else if(solutionMethod==GENETIC_ALGORITHM)
        {
            return  geneticAlgorithm(best_k);
        }
        System.out.println("Unknown Choice");
        return null;
    }




    public  static  Schedule localSearch(ArrayList<Schedule> best_k)
    {

        ArrayList<Schedule> temp_k=new ArrayList<>();
        int numberOfDelete=0;
        for (; true; ) {
            int j = 0;
            best_k.sort(new Comparator<Schedule>() {
                @Override
                public int compare(Schedule o1, Schedule o2) {
                    return Integer.compare(o1.getClashCount(), o2.getClashCount());
                }
            });
            if (best_k.get(0).getClashCount() == 0) {
                //System.out.println(best_k.get(0));
                return best_k.get(0);
            }
            System.out.println(best_k.get(0).getClashCount());
            bestPool.store(best_k.get(0).getClashCount());
            saturationPoint=bestPool.isAllSame();
            numberOfDelete=best_k.size()-k;
            for(int i=k;j<numberOfDelete;i++,j++)
            {
                best_k.remove(best_k.size()-1);
            }
            temp_k.clear();
            temp_k.addAll(best_k);
            for (int i = 0; i < best_k.size(); i++) {
                temp_k.addAll(successorGenerator(best_k.get(i),temp_k));
            }
            best_k.addAll(temp_k);

        }
    }
    public  static  Schedule stochaisticLoaclSearch(ArrayList<Schedule> best_k)
    {
        ArrayList<Schedule> temp_k=new ArrayList<>();

        for (; true; ) {
            int j = 0;
            best_k.sort(new Comparator<Schedule>() {
                @Override
                public int compare(Schedule o1, Schedule o2) {
                    return Integer.compare(o1.getClashCount(), o2.getClashCount());
                }
            });
            if (best_k.get(0).getClashCount() == 0) {
                return best_k.get(0);

            }
            System.out.println(best_k.get(0).getClashCount());
            bestPool.store(best_k.get(0).getClashCount());
            saturationPoint=bestPool.isAllSame();
            temp_k.clear();
            for (int i = 0; i < k; i++) {
                temp_k.add(best_k.get(tournamentSelection(best_k,tryLimit)));
            }
            // temp_k.add(0,best_k.get(0));
            best_k.clear();
            best_k.addAll(temp_k);
            temp_k.clear();

            for (int i = 0; i < best_k.size(); i++) {
                temp_k.addAll(successorGenerator(best_k.get(i),temp_k));
            }
            best_k.addAll(temp_k);

        }
    }

    public  static  Schedule geneticAlgorithm(ArrayList<Schedule> best_k)
    {
        best_k.sort(new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                 return Integer.compare(o1.getClashCount(), o2.getClashCount());
            }
        });

        Schedule parent1,parent2;
        ArrayList<Schedule> temp_k=new ArrayList<>();
        double mValue=0.56;
        for(;best_k.get(0).getClashCount()!=0;)
        {

            System.out.println(best_k.get(0).getClashCount());
            for(int i=0;i<best_k.size();i++)
            {
                parent1=best_k.get(tournamentSelection(best_k,80));
                parent2=best_k.get(tournamentSelection(best_k,80));
                Schedule child=createChild(parent1,parent2);
                temp_k.add(child);
            }
           // temp_k=eliminateDead(temp_k,mValue);
            best_k.addAll(temp_k);

            best_k.sort(new Comparator<Schedule>() {
                @Override
                public int compare(Schedule o1, Schedule o2) {
                    return Integer.compare(o1.getClashCount(), o2.getClashCount());
                }
            });
            for(int i=0;best_k.size()>80;i++)
            {
                best_k.remove(best_k.size()-1);
            }
            temp_k.clear();

        }
        return  best_k.get(0);
    }
    private static ArrayList<Schedule> eliminateDead(ArrayList<Schedule> temp,double mValue)
    {
        double fValue;
        ArrayList<Schedule> retVal=new ArrayList<>();
        temp.sort(new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                return Integer.compare(o1.getClashCount(), o2.getClashCount());
            }
        });
        for(int i=0;(i<50)&&(i<temp.size());i++)
        {
            /*fValue=Math.exp(i-1);
            if(fValue>deathLimit)
            {
                return retVal;
            }
            temp.get(i).setFitness(fValue);*/
            retVal.add(temp.get(i));
        }
        return retVal;
        /*temp.sort(new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                return Double.compare(o1.getFitness(), o2.getFitness());
            }
        });*/
    }
    private  static  Schedule createChild(Schedule parent1,Schedule parent2)
    {
        Schedule child=new Schedule(numberOfDay,numberOfPeriod,count,count);
        ArrayList<Slot> m1[][]=parent1.getRoutine().getMatrix();
        ArrayList<Slot> m2[][]=parent2.getRoutine().getMatrix();
        ArrayList<Slot> m3[][]=child.getRoutine().getMatrix();
        int p1Point,p2Point;
        /***********************Mating**************/
        for(int i=0;i<m1.length;i++)
        {
            for(int j=0;j<m1[i].length;j++)
            {
                p1Point=random.nextInt(6);
                p2Point=random.nextInt(6);
                for(int k=0;(k<p1Point)&&(k<m1[i][j].size());k++)
                {
                    m3[i][j].add(m1[i][j].get(k));
                }
                for(int k=p2Point;k<m2[i][j].size();k++)
                {
                    m3[i][j].add(m2[i][j].get(k));
                }

            }
        }
        /*******************/
       // boolean iss=isAllAvailable(child,slotsSize);
        removeDuplicate(parent1,child);
        //iss=isAllAvailable(child,slotsSize);
        makeComplete(parent1,child);
       // iss=isAllAvailable(child,slotsSize);
        mutation(child);
       // iss=isAllAvailable(child,slotsSize);
        completeAllArrays(child);
        //iss=isAllAvailable(child,slotsSize);
        return child;

    }
    public static void mutation(Schedule child)
    {
        ArrayList<Slot> m3[][]=child.getRoutine().getMatrix();
        int mutationCount=random.nextInt(8);
        int sday,speriod,tday,tperiod;
        Slot temp;
        for(int i=0;i<mutationCount;i++)
        {
            sday=random.nextInt(numberOfDay);
            speriod=random.nextInt(numberOfPeriod);
            tday=random.nextInt(numberOfDay);
            tperiod=random.nextInt(numberOfPeriod);
            if(m3[sday][speriod].size()!=0)
            {
                temp=m3[sday][speriod].remove(0);
                m3[tday][tperiod].add(temp);
            }
        }
    }
    public static  void removeDuplicate(Schedule parent1,Schedule child)
    {
        ArrayList<Slot> m2[][]=parent1.getRoutine().getMatrix();
        ArrayList<Slot> m3[][]=child.getRoutine().getMatrix();
        int t,c;
        for(int i=0;i<m2.length;i++) {
            for (int j = 0; j < m2[i].length; j++)
            {
                for (int k = 0; k < m2[i][j].size(); k++)
                {
                    t=getCount(m2,m2[i][j].get(k));
                    c=getCount(m3,m2[i][j].get(k));
                    if(c>t)
                    {
                        delete(m3,m2[i][j].get(k),c-t);
                    }

                }
            }
        }
    }
    public  static  void makeComplete(Schedule parent,Schedule child)
    {
        ArrayList<Slot> m2[][]=parent.getRoutine().getMatrix();
        ArrayList<Slot> m3[][]=child.getRoutine().getMatrix();
        int dayPos,periodPos,t,c;
        for(int i=0;i<m2.length;i++) {
            for (int j = 0; j < m2[i].length; j++) {
                for(int k=0;k<m2[i][j].size();k++)
                {
                    c=getCount(m3,m2[i][j].get(k));
                    t=getCount(m2,m2[i][j].get(k));
                    if(c<t)
                    {
                        for(int l=0;l<(t-c);l++)
                        {
                            dayPos = random.nextInt(numberOfDay);
                            periodPos = random.nextInt(numberOfPeriod);
                            m3[dayPos][periodPos].add(m2[i][j].get(k));
                        }
                    }
                }
            }
        }
    }






    public static Schedule randomScheduleGenerator(ArrayList<Slot> slots)
    {
        Random random=new Random();
        int period,day;
        Schedule randomSchedule=new Schedule(numberOfDay,numberOfPeriod,count,count);
        int [][][] t,s,r;
        t=randomSchedule.getTeacherClash();
        s=randomSchedule.getSubjectClash();
        r=randomSchedule.getRoomClash();
        for(int i=0;i<slots.size();i++)
        {
            period=random.nextInt(numberOfPeriod);
            day=random.nextInt(numberOfDay);
            t[Integer.parseInt(slots.get(i).getTeacher())-1][day][period]++;
            s[Integer.parseInt(slots.get(i).getSubject())-1][day][period]++;
            try {
                r[Integer.parseInt(slots.get(i).getRoom()) - 1][day][period]++;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            randomSchedule.getRoutine().setPos(day,period,slots.get(i));

        }
        randomSchedule.updateClash();
        return randomSchedule;

    }

    public  static  boolean isCollision(ArrayList<Slot> p)
    {
        for(int i=0;i<p.size();i++)
        {
            for(int j=i+1;j<p.size();j++)
            {
                if((p.get(i).getRoom().equals(p.get(j).getRoom()))||
                        (p.get(i).getTeacher().equals(p.get(j).getTeacher()))||
                        (p.get(i).getSubject().equals(p.get(j).getSubject())))
                {
                    return true;
                }
            }
        }
        return false;
    }
    public  static  ArrayList<Schedule> successorGenerator(Schedule parent,ArrayList<Schedule> accumulator)
    {
        ArrayList<Schedule> successors=new ArrayList<>();
        ArrayList<Slot> parentMatrix[][]=parent.getRoutine().getMatrix();
        for(int i=0;i<parentMatrix.length;i++)
        {
            for(int j=0;j<parentMatrix[i].length;j++)
            {
                for(int k=0;(isCollision(parentMatrix[i][j]))&& (k<parentMatrix[i][j].size());k++)
                {
                    if(saturationPoint)
                    {
                        for (int l = 0; l < parentMatrix.length; l++) {
                            for (int m = 0; m < parentMatrix[l].length; m++) {
                                Schedule newSchedule = copyPaste(parent, l, m, i, j, k);
                                if (!successors.contains(newSchedule) &&!accumulator.contains(newSchedule)) {
                                    successors.add(newSchedule);
                                }
                            }
                        }
                    }
                    else
                    {
                        /****************UP*************/
                        Schedule newSchedule = copyPaste(parent, i-1, j, i, j, k);
                        if (newSchedule!=null && !successors.contains(newSchedule)) {
                            successors.add(newSchedule);
                        }
                        /****************DOWN*************/
                        newSchedule = copyPaste(parent, i+1, j, i, j, k);
                        if (newSchedule!=null && !successors.contains(newSchedule)) {
                            successors.add(newSchedule);
                        }
                        /****************LEFT*************/
                        newSchedule = copyPaste(parent, i, j-1, i, j, k);
                        if (newSchedule!=null && !successors.contains(newSchedule)) {
                            successors.add(newSchedule);
                        }
                        /****************RIGHT*************/
                        newSchedule = copyPaste(parent, i, j+1, i, j, k);
                        if (newSchedule!=null && !successors.contains(newSchedule)) {
                            successors.add(newSchedule);
                        }
                    }
                    /*if(inBoundary(parent.getDay(),parent.getPeriod(),i-1,j))
                    {
                        Schedule newSchedule=copyPaste(parent,UP,i,j,k);
                        if(!successors.contains(newSchedule))
                        {
                            successors.add(newSchedule);
                        }
                    }*/


                }
            }
        }
        return successors;
    }
    private  static  Schedule copyPaste(Schedule parent,int l,int m,int i,int j,int removePos)
    {
        int targetI=l,targetJ=m;
        if(inBoundary(numberOfDay,numberOfPeriod,targetI,targetJ))
        {
            Schedule newSchedule = new Schedule(parent.getDay(), parent.getPeriod(), parent.getSubjectCount(), parent.getTeacherCount());
            ArrayList<Slot> newMatrix[][] = parent.getRoutine().copy();
            int[][][] newTeacher = arrayCopy(parent.getTeacherClash());
            int[][][] newSubject = arrayCopy(parent.getSubjectClash());
            int[][][] newRoom = arrayCopy(parent.getRoomClash());
            Slot removedSlot = newMatrix[i][j].remove(removePos);
            int t,s,r;
            t=Integer.parseInt(removedSlot.getTeacher())-1;
            s=Integer.parseInt(removedSlot.getSubject())-1;
            r=Integer.parseInt(removedSlot.getRoom())-1;
            newTeacher[t][i][j]--;
            newSubject[s][i][j]--;
            newRoom[r][i][j]--;

            newMatrix[targetI][targetJ].add(removedSlot);
            newTeacher[t][targetI][targetJ]++;
            newSubject[s][targetI][targetJ]++;
            newRoom[r][targetI][targetJ]++;
            newSchedule.setRoutine(new Matrix(newMatrix));
            newSchedule.setTeacherClash(newTeacher);
            newSchedule.setSubjectClash(newSubject);
            newSchedule.setRoomClash(newRoom);
            newSchedule.updateClash();
            return newSchedule;
        }
        return  null;

    }


}
