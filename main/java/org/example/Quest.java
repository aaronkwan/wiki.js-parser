package org.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Quest {
    public String questName = "";
    public String questNumber = "";
    public String questDescription = "";
    public ArrayList<Integer > questCompleteScores;
    public String[] questReqs;
    public LinkedHashMap<String, String> questValues;


    public Quest(String questName, String questNumber, String questDescription,
                 ArrayList<Integer> questCompleteScores, String[] questReqs, LinkedHashMap<String, String> questValues) {
        this.questName = questName;
        this.questNumber = questNumber;
        this.questDescription = questDescription;
        this.questCompleteScores = questCompleteScores;
        this.questReqs = questReqs;
        this.questValues = questValues;
    }

}