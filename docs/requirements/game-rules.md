# Risk - Game Rules

## Overview
Risk is a turn-based strategy board game of "global domination" for 2-6 players. The goal 
of the game is to conquer every territory on the board and eliminate all opponents.

## The "Board"
The board (GUI) will display a map of Northwestern University divded into ? territories and
? continents. Each territory shows:
- The territory name
- The number of armies currently stationed there
- A color indicating which player controls it 

## Setup
1. Each player picks a color and are given their armies.
2. Players take turns placing on army on any unclaimed territory until all 42 are claimed.
3. Players then distribute their remaining starting armies by clicking their own territories one at a time.
4. Once all armies are placed, the game begins.

[Cards will already be shuffled digitally and hidden for all players.]

## Turn Structure
Each turn has three phases, completed in order. The UI will highlight what actions are 
available to you at each phase.

### 1. Draft
You automatically receive new armies at the start of your turn based on:
- Territories: 1 army per 3 territories you own (minimum 3)
- Continents: Bonus armies for controlling an entire continent
- Card trade-ins: Trade a valid set of 3 cards for bonus armies
  Click any of your territories to place your incoming armies. You must place all armies before proceeding.

### 2. Attack *(optional)*
Click one of your territories to select it as the attacker, then click an adjacent enemy territory to initiate an attack.

- You need at least 2 armies in the attacking territory
- Select how many dice to roll (up to 3 for attacker, up to 2 for defender, the computer controls the defender's dice)
- Dice are compared from highest to lowest. Ties go to the defender
- Each comparison costs the losing side one army
- If the defender is wiped out, you capture the territory. You'll be prompted to move armies in
- You must always leave at least 1 army behind in any territory
You can attack as many times as you'd like during this phase.

### 3. Fortify *(optional)*
Click one of your territories, then click an adjacent territory you own to move armies between them. 
You may only fortify once per turn. When you're done, click End Turn.

## Risk Cards 
- Capture at least one territory on your turn to automatically receive a Risk card.
- Cards are shown in your hand at the bottom of the screen.
- Each card is one of: Infantry, Cavalry, Artillery, or Wild
- Trade in a valid set of 3 to receive bonus armies:
  - Three of the same type
  - One of each type
  - Any two + a Wild
- If a traded card matches a territory you own, you'll receive 2 bonus armies on that territory.
- If you eliminate a player, you inherit all of their cards. If this puts you at 6+, you must trade in immediately.

## Win Condition
The last player with armies on the board wins. Eliminated players can spectate the rest of the game.
