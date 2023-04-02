# wiki.js-parser
Companion to [plugin-mmquest](https://github.com/aaronkwan/plugin-mmquest): 
a command-line wiki.js parser utlizing Jsoup and Gson to convert the Monumenta Quest Wiki to JSON config file.

# features:
- Hashes login parameters, authenticating with the Monumenta Quest Wiki.  
- Saves JWT (Json Web Token) Authenticator for webpage access.
- Parses HTML of [Region 1](https://wiki.playmonumenta.com/moderating/quest-scores/region_2), [Region 2](https://wiki.playmonumenta.com/moderating/quest-scores/region_2), and [Region 3](https://wiki.playmonumenta.com/moderating/quest-scores/region_3) Quests.
- Utilizes Gson and Jsoup libraries to convert Quest data to JSON.
      
# Features:

![image](https://user-images.githubusercontent.com/123356351/229330725-4947f7db-f256-43b9-8e0e-49c8d59daa46.png)

# Usage:
Download jar and use from the command line:

        java -jar <path_to_jar> <username> <password> <file_path>
        
# Example:
        java -jar /Downloads/wikiparser-1.0.0-all.jar myUsername myPassword ./mmquest.json
        
This will parse the quest wiki (provided the username and password is correct), posting the results into the file path specified (in this case, it creates a file called mmquest.json in the Downloads folder).
