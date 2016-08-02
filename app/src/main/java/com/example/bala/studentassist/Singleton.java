package com.example.bala.studentassist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bala on 7/24/16.
 */
public class Singleton {
    private static Singleton instance;
    public static List<MyPlace> placeList;
    public static List<RealEstate> realEstateList;
    public static Map<String,MyPlace> placeMap;
    public static List<MyPlace> historyList;
    static {
        realEstateList = new ArrayList<>();
        placeMap = new HashMap<>();
        placeList = new ArrayList<>();
        historyList = new ArrayList<>();
    }
    private Singleton(){}
    public static Singleton getInstance()
    {
        if(instance==null)
        {
            synchronized (Singleton.class) {
                if(instance == null)
                    instance = new Singleton();
            }
        }
        return instance;
    }
}
