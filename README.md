Match API
---------

    A Spring Boot REST API for managing sports matches and their betting odds.  
    The system supports match creation, retrieval, updates, deletion, and odds management with business validations.
    There is a script which inserts 3 mathces on application start (2 football and 1 basketball).

Tech Stack
----------

  - Java 17
  - Spring Boot 3
  - Spring Web (REST API)
  - Spring Data JPA (Hibernate)
  - PostgreSQL
  - Maven
  - Docker & Docker Compose
  - Lombok

Features
--------

    Match Management
    - Create a match
    - Get match by ID
    - Update match
    - Delete match
    
    Odds Management
    - Add odds to a match
    - Update existing odds
    - Retrieve odds by match ID
    
    Validations
    -Global validations
        - NOT_FOUND : match ID, odd ID
    -Match validations
        - teamA must be different from teamB
        - description must be "teamA-teamB"
        - Duplicate check: Can not create match entry for same teamA, teamB, matchDate, Sport
        - Field validations
            - Not empty: teamA, teamB, description
            - Not null: matchDate, matchTime, Sport
    -Odds validations
        - odd value >= 0,01 and odd value <= 10000,00
        - Specifier BASKETBALL can not have odd for DRAW

    Domain Model
    Match
        ● id
        ● description
        ● match_date
        ● match_tiime
        ● team_a
        ● team_b
        ● sport (enum with values 1.Football, 2.Basketball)

    MatchOdds
        ● id
        ● match_id
        ● specifier (enum with values 1.Home, 2.Draw, 3.Away)
        ● odd

Pain points
-----------
I found the handling of Enums unnecessary complicated. I had to implement a custom converter class for both enums to jump between
json and database values. I would recommend the use of separated entities for Sport and Specifier with an OneToOne relation between
Match and MatchOdd entities respectively. Another reason for this is to be able to update those entities from the database. Enums
have fixed values which requires a new API version in case of a change/addition

How to Run the Application
-------------------------

1. Start Infrastructure engine
   - Start docker engine

2. From a powershell terminal window navigate to project root and run:
    - docker compose down -v
    - mvn clean package
    - docker compose up --build

How to Use the Application with log examples
--------------------------------------------

//GET match by id - Retrieves info of match id = 1
    
    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches/1" ` | ConvertTo-Json -Depth 5
    
    RESPONSE:
{
  "success": true,
  "data": {
    "id": 1,
    "description": "OSFP-PAO",
    "matchDate": "31/03/2021",
    "matchTime": "12:00",
    "teamA": "OSFP",
    "teamB": "PAO",
    "sport": "FOOTBALL",
    "odds": [
      {
        "id": 1,
        "matchId": 1,
        "specifier": "HOME",
        "odd": 1.3
      },
      {
        "id": 2,
        "matchId": 1,
        "specifier": "DRAW",
        "odd": 2.5
      },
      {
        "id": 3,
        "matchId": 1,
        "specifier": "AWAY",
        "odd": 3.5
      }
    ]
  },
  "error": null
}

//GET match odds by match id - Retrieves odds info of match id = 1
    
    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches/1/odds" -Method Get ` | ConvertTo-Json -Depth 5
    
    RESPONSE:
{
  "success": true,
  "data": [
    {
      "id": 1,
      "matchId": 1,
      "specifier": "HOME",
      "odd": 1.3
    },
    {
      "id": 2,
      "matchId": 1,
      "specifier": "DRAW",
      "odd": 2.5
    },
    {
      "id": 3,
      "matchId": 1,
      "specifier": "AWAY",
      "odd": 3.5
    }
  ],
  "error": null
}

//UPDATE odds list for a match id - Updates odds for existing match id = 1
    
    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches/1/odds" `
-Method Post `
-ContentType "application/json" `
-Body '[
{ "specifier": "HOME", "odd": 1.3 },
{ "specifier": "DRAW", "odd": 2.5 },
{ "specifier": "AWAY", "odd": 4.5 }
]' | ConvertTo-Json -Depth 5
    
    RESPONSE:
{
  "success": true,
  "data": [
    {
      "id": 1,
      "matchId": 1,
      "specifier": "HOME",
      "odd": 1.3
    },
    {
      "id": 2,
      "matchId": 1,
      "specifier": "DRAW",
      "odd": 2.5
    },
    {
      "id": 3,
      "matchId": 1,
      "specifier": "AWAY",
      "odd": 4.5
    }
  ],
  "error": null
}

//DELETE match by id - Deletes match info for match id = 1
    
    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches/1" -Method Delete ` | ConvertTo-Json -Depth 5

    RESPONSE: ""

//VERIFY DELETED match by id - Verify that match id = 1 is deleted
    
    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches/1" -Method Get ` | ConvertTo-Json -Depth 5   
    
    RESPONSE:
Invoke-RestMethod:
Invoke-RestMethod:
{
  "success": false,
  "data": null,
  "error": "Match id 1 not found"
}

//CREATE new match without odds list (id would be 4)

    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches" `
-Method Post `
-ContentType "application/json" `
-Body '{
"description": "OSFP-PAO",
"matchDate": "14/03/2021",
"matchTime": "12:00",
"teamA": "OSFP",
"teamB": "PAO",
"sport": "FOOTBALL"
}' | ConvertTo-Json -Depth 5

    RESPONSE:
{
  "success": true,
  "data": {
    "id": 4,
    "description": "OSFP-PAO",
    "matchDate": "14/03/2021",
    "matchTime": "12:00",
    "teamA": "OSFP",
    "teamB": "PAO",
    "sport": "FOOTBALL",
    "odds": null
  },
  "error": null
}

//INSERT odds for new match (id = 4)
    
    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches/4/odds" `
-Method Post `
-ContentType "application/json" `
-Body '[
{ "specifier": "HOME", "odd": 1.3 },
{ "specifier": "DRAW", "odd": 2.5 },
{ "specifier": "AWAY", "odd": 4.5 }
]' | ConvertTo-Json -Depth 5

    RESPONSE:
{
  "success": true,
  "data": [
    {
      "id": null,
      "matchId": 4,
      "specifier": "HOME",
      "odd": 1.3
    },
    {
      "id": null,
      "matchId": 4,
      "specifier": "DRAW",
      "odd": 2.5
    },
    {
      "id": null,
      "matchId": 4,
      "specifier": "AWAY",
      "odd": 4.5
    }
  ],
  "error": null
}

//VERIFY odds creation for match id = 4

    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches/4" -Method Get ` | ConvertTo-Json -Depth 5

    RESPONSE:
{
  "success": true,
  "data": {
    "id": 4,
    "description": "OSFP-PAO",
    "matchDate": "14/03/2021",
    "matchTime": "12:00",
    "teamA": "OSFP",
    "teamB": "PAO",
    "sport": "FOOTBALL",
    "odds": [
      {
        "id": 9,
        "matchId": 4,
        "specifier": "HOME",
        "odd": 1.3
      },
      {
        "id": 10,
        "matchId": 4,
        "specifier": "DRAW",
        "odd": 2.5
      },
      {
        "id": 11,
        "matchId": 4,
        "specifier": "AWAY",
        "odd": 4.5
      }
    ]
  },
  "error": null
}

//CREATE new match with odds list (id would be 5)

    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches" `
-Method Post `
-ContentType "application/json" `
-Body '{
"description": "OSFP-PAO",
"matchDate": "15/03/2023",
"matchTime": "12:00",
"teamA": "OSFP",
"teamB": "PAO",
"sport": "FOOTBALL",
"odds": [
{ "specifier": "HOME", "odd": 1.3 },
{ "specifier": "DRAW", "odd": 2.5 },
{ "specifier": "AWAY", "odd": 3.5 }
]
}' | ConvertTo-Json -Depth 5

    RESPONSE:
{
  "success": true,
  "data": {
    "id": 5,
    "description": "OSFP-PAO",
    "matchDate": "15/03/2023",
    "matchTime": "12:00",
    "teamA": "OSFP",
    "teamB": "PAO",
    "sport": "FOOTBALL",
    "odds": [
      {
        "id": 12,
        "matchId": 5,
        "specifier": "HOME",
        "odd": 1.3
      },
      {
        "id": 13,
        "matchId": 5,
        "specifier": "DRAW",
        "odd": 2.5
      },
      {
        "id": 14,
        "matchId": 5,
        "specifier": "AWAY",
        "odd": 3.5
      }
    ]
  },
  "error": null
}

Log examples for validations
----------------------------

//Get an unexisting match

    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches/10" -Method Get ` | ConvertTo-Json -Depth 5

    RESPONSE:
Invoke-RestMethod:
{
  "success": false,
  "data": null,
  "error": "Match id 10 not found"
}

//teamA must be different from teamB

    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches" `
-Method Post `
-ContentType "application/json" `
-Body '{
"description": "OSFP-OSFP",
"matchDate": "15/03/2025",
"matchTime": "12:00",
"teamA": "OSFP",
"teamB": "OSFP",
"sport": "FOOTBALL"
}' | ConvertTo-Json -Depth 5

    RESPONSE:
Invoke-RestMethod:
{
  "success": false,
  "data": {
    "teamA": "teamA and teamB must be different"
  },
  "error": "Validation failed"
}

//description must be "teamA-teamB"

    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches" `
-Method Post `
-ContentType "application/json" `
-Body '{
"description": "OSFP-OSFP",
"matchDate": "15/03/2025",
"matchTime": "12:00",
"teamA": "OSFP",
"teamB": "PAO",
"sport": "FOOTBALL"
}' | ConvertTo-Json -Depth 5

    RESPONSE:
Invoke-RestMethod:
{
  "success": false,
  "data": {
    "description": "description must be in format: teamA-teamB"
  },
  "error": "Validation failed"
}

//Duplicate check: Can not create match entry for same teamA, teamB, matchDate, Sport

    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches" `
-Method Post `
-ContentType "application/json" `
-Body '{
"description": "OSFP-PAO",
"matchDate": "15/03/2023",
"matchTime": "12:00",
"teamA": "OSFP",
"teamB": "PAO",
"sport": "FOOTBALL"
}' | ConvertTo-Json -Depth 5

    RESPONSE:
Invoke-RestMethod:
{
  "success": false,
  "data": null,
  "error": "Match already exists for OSFP vs PAO on 2023-03-15 for FOOTBALL"
}

//Specifier BASKETBALL can not have odd for DRAW

    REQUEST:
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/matches/3/odds" `
-Method Post `
-ContentType "application/json" `
-Body '[
{ "specifier": "HOME", "odd": 1.3 },
{ "specifier": "DRAW", "odd": 2.5 },
{ "specifier": "AWAY", "odd": 4.5 }
]' | ConvertTo-Json -Depth 5

    RESPONSE:
Invoke-RestMethod:
{
  "success": false,
  "data": null,
  "error": "Basketball match id 3 can not have odd for DRAW"
}