# wiki.js-parser
Companion to plugin-mmquest; A wiki.js parser to convert Quest Wiki page to JSON file.

- See [plugin-mmquest](https://github.com/aaronkwan/plugin-mmquest)

## Features:
- Parses Quest tables from Wiki.js -> JSON File:
![image](https://user-images.githubusercontent.com/123356351/221446553-17c388f4-66de-4574-b424-b078117a3aca.png)
- Obtains:
- The Quest's Name: "Mages Legacy"
- The Quest's Scoreboard: "Quest03"
- The Quest's Description: "Quest Start: Vargos (-735 155 116)"
- The Quest's Completion Scores: [21]
- The Quest's Requirements: []
- The Quest's Values: "questValues": {
        "0": "Unstarted",
        "1": "Speaking to Vargos",
        "2": "Speaking to Vargos",
        "3": "Speaking to Vargos",
        "4": "Speaking to Vargos",
        "5": "Speaking to Vargos",
        "6": "Speaking to Vargos",
        "7": "Speaking to Vargos",
        "8": "Speaking to Vargos",
        "9": "Tasked to find Ezariah's notes",
        "10": "Returned notes to Vargos",
        "11": "Tasked to go to the office on the roof",
        "-4: -4 (Yes, negative. Don’t ask)": "Puzzle done, but dc’d before commands finished. Return to Vargos to get to 13 or redo puzzle to get to -5",
        "-5: -5 (Yes, negative. Don't ask)": "Completed puzzle on roof / Return to Vargos",
        "13": "Tasked to find Hermy (-320, 92, 340)",
        "14": "Got gloop from witch’s village (-390, 95, 400) and gave them to Hermy",
        "15": "Received translated notes from Hermy",
        "21": "Quest Complete"
      }
