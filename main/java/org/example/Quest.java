package org.example;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Quest {
    public String mQuestName = "";
    public String mQuestNumber = "";
    public String mQuestDescription = "";
    public ArrayList<Integer > mQuestCompleteScores;
    public String[] mQuestReqs;
    public LinkedHashMap<String, String> mQuestValues;


    public Quest(String questName, String questNumber, String questDescription,
                 ArrayList<Integer> questCompleteScores, String[] questReqs, LinkedHashMap<String, String> questValues) {
        this.mQuestName = questName;
        this.mQuestNumber = questNumber;
        this.mQuestDescription = questDescription;
        this.mQuestCompleteScores = questCompleteScores;
        this.mQuestReqs = questReqs;
        this.mQuestValues = questValues;
    }

}