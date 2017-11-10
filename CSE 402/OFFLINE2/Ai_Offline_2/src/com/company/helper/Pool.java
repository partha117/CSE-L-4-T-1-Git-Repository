package com.company.helper;

import java.util.ArrayList;

public class Pool<T extends  Comparable> {

    private ArrayList<T> storage;
    private  int storageSize;
    private  int currentPoint;

    public Pool(int storageSize) {
        this.storageSize = storageSize;
        storage=new ArrayList<>();
        currentPoint=0;
    }
    public  void store(T obj)
    {
        if(currentPoint<storageSize)
        {
            storage.add(obj);
            currentPoint++;
        }
        else
        {
            ArrayList<T> newStorage=new ArrayList<>();
            for(int i=1;i<storageSize;i++)
            {
                newStorage.add(storage.get(i));
            }
            newStorage.add(obj);
            storage=newStorage;
        }
    }
    public  boolean isAllSame()
    {

        for(int i=1;(i<currentPoint)&&(i<storageSize);i++)
        {
            if((storage.get(i-1)).compareTo(storage.get(i))!=0)
            {
                return false;
            }
        }
        return  true;
    }

    @Override
    public String toString() {
        String  st="";
        for(int i=0;(i<currentPoint)&&(i<storageSize);i++)
        {
            st+=storage.get(i)+"\n";
        }
        return  st;
    }
}
