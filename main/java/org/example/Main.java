package org.example;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("-----------------------------LOOK BELOW-----------------------------");

        // Create allMyQuests object with built-in ArrayList to hold Quests:
        AllMyQuests allMyQuests = new AllMyQuests();

        // Try Parsing:
        try {
            // Make HTTP request:
            String url = "https://aaronkwan.github.io/ReferenceFiles/Region%201%20Quests%20_%20Wiki.js.html";
            Connection connection = Jsoup.connect(url);
            connection.header("Content-Type", "text/html; charset=UTF-8");
            Document doc = connection.get();

            // Extract from webpage:
            Elements headers = doc.select("h1"); // One header per Quest, gives title + score.

            // Extract text content:
            // Workflow:
            // 1. For each <h1> element, select the following descending elements.
            // 3. Parse the contents of each element selected into our format.
            // 4. Use this to construct objects, then write them to JSON using Gson.
            // 5. Note: if element is null, we either append an empty string or append nothing at all.


            for (Element header : headers) {
                String[] titleScoreboardArray = grabTitleAndScoreboard(header); // Title + Scoreboard of the Quest Array
                System.out.println(Arrays.toString(titleScoreboardArray));

                Element nextEle = header.nextElementSibling(); // Random <div></div>
                Element requirements = nextEle.nextElementSibling(); // Requirements + Description

                String[] requirementsArray = grabRequirements(requirements); // Requirements Array
                System.out.println(Arrays.toString(requirementsArray));

                String description = grabDescription(requirements); // Description String
                System.out.println(description);

                Element nextEle2 = requirements.nextElementSibling(); // Another random <div></div>
                Element table = nextEle2.nextElementSibling(); // Our table of Quest scoreboard values

                // Create a HashMap with quest scores mapped to their descriptions:
                LinkedHashMap<String,String> questValueHashMap = grabValuesAndDescription(table); // hashmap
                System.out.println(Arrays.toString(questValueHashMap.entrySet().toArray())); // print it out

                ArrayList<Integer> questCompletion = grabQuestCompletionValues(questValueHashMap); // quest completion values
                System.out.println(questCompletion.toString());

                System.out.println("");
                // Make Quest object, append to allMyQuests object's questArrayList:
                allMyQuests.questArrayList.add(new Quest(titleScoreboardArray[0],titleScoreboardArray[1],description,questCompletion,requirementsArray,questValueHashMap));
            }
            // Catch errors:
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-----------------------------LOOK ABOVE-----------------------------");

        // convert allMyQuests object to JSON:

        // Create a Gson object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // Convert allMyQuests object to JSON:
        String json = gson.toJson(allMyQuests);
        // Replace pesky "\u0027" characters with apostrophes:
        // json = json.replace("\\u0027", "'");

        // Write to File:
        File jsonFile = new File("C:/Users/aaron/Desktop/JavaTesting/src/main/resources/mmquest.json");
        try {
            FileWriter writer = new FileWriter(jsonFile, StandardCharsets.UTF_8);
            writer.write(json);
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] grabTitleAndScoreboard(Element header) {
        // Convert to string & remove non-number/letter characters (except dashes + whitespace):
        String cleanHeader = header.text().replaceAll("[^a-zA-Z0-9\\s-]", "");
        // Split string at the dash character, trim whitespace:
        String[] parts = cleanHeader.split("-");
        parts[0] = parts[0].trim();
        parts[1] = parts[1].trim();
        // Return:
        return (parts);
    }

    public static String[] grabRequirements(Element requirements) {
        // Convert to string, split Requirements and Description:
        String cleanRequirements = requirements.text();
        String[] parts = cleanRequirements.split("Quest Start:");
        // We want parts[0], the requirements section. Parse out "Requirements"
        parts[0] = parts[0].replaceAll("Requirements", "").trim();
        // Create Array to hold requirements:
        String[] questRequirements = new String[] {};
        // If parts[0] = "None", return empty array:
        if (parts[0].matches("(?i)[-:]\\s*None$")) {
            return questRequirements;
        }
        // Otherwise split by commas, replace non letter/numbers & trim:
        questRequirements = parts[0].split(",");
        for (int i = 0; i < questRequirements.length; i++) {
            questRequirements[i] = questRequirements[i].replaceAll("[^a-zA-Z0-9\\s]", "");
            questRequirements[i] = questRequirements[i].trim();
        }
        // Return:
        return (questRequirements);
    }

    public static String grabDescription(Element requirements) {
        // Convert to string, split Requirements and Description by the <br> element:
        String cleanRequirements = requirements.html();
        String[] parts = cleanRequirements.split("\\<br\\s*/*\\>");
        // Return description, or empty string if no description:
        if (parts.length <= 1) {
            return ("");
        }
        return (parts[1].trim());
    }

    public static LinkedHashMap<String, String> grabValuesAndDescription(Element table) {
        // Create Hashmap:
        LinkedHashMap<String, String> questValueMap = new LinkedHashMap<>();
        // Select table rows:
        Elements tablerows = table.select("tr");
        // Extract the first and second cells out of each row:
        for (Element row : tablerows) {
            Element scoreCell = row.select("td").first();
            if (scoreCell == null) {
                continue;
            } // If no cells, skip.
            Element valueCell = scoreCell.nextElementSibling();
            // Convert to text, trim:
            String questScore = scoreCell.text().trim();
            String questValueDescription = valueCell.text().trim();

            // Workflow: Convert String questScore to a nice integer, append any extra words to questValueDescription:
            // There are 3 possible cases for questScore (curse the people who put words in the "Scores" column! xP)
            // CASE 1: A nice, simple number. EX: ("-5"; "5, 6"; "4-6, 7")
            // CASE 2: Numbers at the front, words at the back. EX: ("5 (it is a great number)"; "2 - Don't ask how!")
            // CASE 3: No number at the front. EX: ("Has tag Azzy"; "Has 5 white completions")

            int CASE = 0;
            if (questScore.matches("[0-9,\\s-]+")) { // Only contains numbers, commas, dashes, or whitespace
                CASE = 1;
            } else {
                String[] parts = questScore.split("(?<=\\d)(?=\\D+)", 2); // Split after the number
                if (parts[0].matches("[0-9,\\s-]+")) { // Number(s) is at the front
                    CASE = 2;
                } else {
                    CASE = 3; // Number is not at the front / no number at all
                }
            }

            switch (CASE) {
                case 3:
                    // Note a non-scoreboard value association:
                    questValueDescription = questValueDescription + " (NOTE: no scoreboard value association)";
                    // Append to HashMap:
                    questValueMap.put(questScore, questValueDescription);
                    break;
                case 2:
                    // Split the string after the number:
                    String[] parts = questScore.split("(?<=\\d)(?=\\D+)", 2);
                    // Run stringToIntegerList on parts[0], the number portion:
                    // NOTE: appending the integer at the beginning of string.
                    ArrayList<Integer> questScoreInts = stringToIntegerList(parts[0]);
                    for (Integer questScoreInt : questScoreInts) {
                        questScore = questScoreInt + ": " + questScore;
                        questValueMap.put(questScore,questValueDescription);
                    }
                    break;
                case 1:
                    // Run stringToIntegerList on questScore:
                    ArrayList<Integer> questScoreInts2 = stringToIntegerList(questScore);
                    for (Integer questScoreInt2 : questScoreInts2) {
                        questValueMap.put(questScoreInt2.toString(),questValueDescription);
                    }
                    break;
            }
        }
        // Return HashMap
        return (questValueMap);
    }
    public static ArrayList<Integer> stringToIntegerList(String questScore) {
        // Transform questScore into an array of questScoresInts:
        // Workflow: separate numbers by commas and dashes:
        // 1. Split by commas. EX: "4, 5 - 7, 9" -> ("4", " 5 - 7", " 9").
        // 2. Create arraylist to store integers.
        // 3. Split dashes, convert to Integer, store in list. EX: ("4", "5-7", "9") -> (4, 5, 6, 7, 9)
        String[] parts = questScore.split(","); // Split by commas
        ArrayList<Integer> questScoreInts = new ArrayList<>(); // Use ArrayList to store integers
        for (String part : parts) {
            part = part.replaceAll("\\s", ""); // Remove whitespace
            //if (!(part.contains("-") || part.matches(".*\\d-\\d.*"))) {
            if (!part.contains("-")) { // Part contains no dashes.
                // If no dashes or no dashes between two digits, parse int and move on:
                // parse int when:
                // 1: no dashes.
                // if dash, still parse int if no dash between two digits.
                questScoreInts.add(Integer.parseInt(part));
                continue;
            }
            else if (!part.matches(".*\\d-.*")) { // Part contains no dashes between two numbers.
                questScoreInts.add(Integer.parseInt(part));
                continue;
            }
            // Otherwise, split by dashes, store a range of integers:
            String[] partsRange = part.split("-");
            int start = Integer.parseInt(partsRange[0]);
            int end = Integer.parseInt(partsRange[1]);
            for (int i = start; i <= end; i++) {
                questScoreInts.add(i);
            }
        }
        // Return ArrayList:
        return(questScoreInts);
    }
    public static ArrayList<Integer> grabQuestCompletionValues(LinkedHashMap<String,String> questValueMap) {
        // Workflow: Loop through HashMap, attemping to match these 3 Reqs:
        // Req 1. Look for "Quest Complete" - this solves 99% of quests
        // Req 2. Look for "Complete" along with "Quest" - this solves 0.5% of quests
        // Req 3. Look for "Complete" - this solves the last 0.5% of quests
        // Finally, return the Req[] Array which is not empty (favoring 1>2>3, in that order).

        // Create ArrayLists to store quest completion values:
        ArrayList<Integer> req1 = new ArrayList<Integer>();
        ArrayList<Integer> req2 = new ArrayList<Integer>();
        ArrayList<Integer> req3 = new ArrayList<Integer>();
        // Loop through each HashMap entry, if satisfies a req, convert score to integer:
        for (Map.Entry<String, String> entry : questValueMap.entrySet()) {
            String value = entry.getValue().toLowerCase();
            // Test if value is a quest completion score:
            // Req 1:
            if (value.contains("quest complete")) {
                String key = entry.getKey().trim();
                // Convert string to number:
                if (key.matches("\\d+")) {
                    req1.add(Integer.parseInt(key));
                }
                continue;
            }
            // Req 2:
            if (value.contains("quest") && value.contains("complete")) {
                String key = entry.getKey().trim();
                // Convert string to number:
                if (key.matches("\\d+")) {
                    req2.add(Integer.parseInt(key));
                }
                continue;
            }
            // Req 3:
            if (value.contains("complete")) {
                String key = entry.getKey().trim();
                // Convert string to number:
                if (key.matches("\\d+")) {
                    req3.add(Integer.parseInt(key));
                }
            }
        }

        // Select whichever Req list is not empty:
        if (!req1.isEmpty()) {
            return (req1);
        }
        if (!req2.isEmpty()) {
            return (req2);
        }
        if (!req3.isEmpty()) {
            return (req3);
        }
        // Returns a EMPTY ArrayList:
        return (req1);
    }
}

