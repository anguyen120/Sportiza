CREATE TABLE "teams" (
  "id" int,
  "name" text,
  "conference" int,
  "season" int
);

CREATE TABLE "players" (
  "id" int,
  "Team Code" int,
  "Last Name" text,
  "First Name" text,
  "Uniform Number" text,
  "Class" varchar(2),
  "Position" text,
  "Height" int,
  "Weight" int,
  "Home Town" text,
  "Home State" text,
  "Home Country" text,
  "Last School" text,
  "Season" int
);

CREATE TABLE "conferences" (
  "id" int UNIQUE PRIMARY KEY,
  "name" text,
  "subdivision" varchar(3)
);

CREATE TABLE "games" (
  "id" BIGINT UNIQUE PRIMARY KEY,
  "date" text,
  "visitTeam" int,
  "homeTeam" int,
  "stadium" int,
  "site" text,
  "season" int
);

CREATE TABLE "gameStats" (
  "Game Code" BIGINT,
  "attendance" int,
  "duration" int
);

CREATE TABLE "stadiums" (
  "id" int UNIQUE PRIMARY KEY,
  "name" text,
  "city" text,
  "state" text,
  "capacity" int,
  "surface" text,
  "founded" int
);

CREATE TABLE "kickoff" (
  "Game Code" BIGINT,
  "Play Number" int,
  "Team Code" int,
  "Player Code" int,
  "Attempt" boolean,
  "Yards" int,
  "Fair Catch" boolean,
  "Touchback" boolean,
  "Downed" boolean,
  "Out Of Bounds" boolean,
  "Onside" boolean,
  "Onside Success" boolean
);

CREATE TABLE "kickoff return" (
  "Game Code" BIGINT,
  "Play Number" int,
  "Team Code" int,
  "Player Code" int,
  "Attempt" boolean,
  "Yards" int,
  "Touchdown" boolean,
  "Fumble" int,
  "Fumble Lost" boolean,
  "Safety" boolean,
  "Fair Catch" boolean
);

CREATE TABLE "passes" (
  "Game Code" BIGINT,
  "Play Number" int,
  "Team Code" int,
  "Passer Player Code" int,
  "Receiver Player Code" int,
  "Attempt" boolean,
  "Completion" boolean,
  "Yards" int,
  "Touchdown" boolean,
  "Interception" boolean,
  "1st Down" boolean,
  "Dropped" boolean
);

CREATE TABLE "punt return" (
  "Game Code" BIGINT,
  "Play Number" int,
  "Team Code" int,
  "Player Code" int,
  "Attempt" boolean,
  "Yards" int,
  "Touchdown" boolean,
  "Fumble" boolean,
  "Fumble Lost" boolean,
  "Safety" boolean,
  "Fair Catch" boolean
);

CREATE TABLE "punt" (
  "Game Code" BIGINT,
  "Play Number" int,
  "Team Code" int,
  "Player Code" int,
  "Attempt" boolean,
  "Yards" int,
  "Blocked" boolean,
  "Fair Catch" boolean,
  "Touchback" boolean,
  "Downed" boolean,
  "Out Of Bounds" boolean
);

CREATE TABLE "reception" (
  "Game Code" BIGINT,
  "Play Number" int,
  "Team Code" int,
  "Player Code" int,
  "Reception" boolean,
  "Yards" int,
  "Touchdown" boolean,
  "1st Down" boolean,
  "Fumble" boolean,
  "Fumble Lost" boolean,
  "Safety" boolean
);

CREATE TABLE "rush" (
  "Game Code" BIGINT,
  "Play Number" int,
  "Team Code" int,
  "Player Code" int,
  "Attempt" boolean,
  "Yards" int,
  "Touchdown" boolean,
  "1st Down" boolean,
  "Sack" boolean,
  "Fumble" boolean,
  "Fumble Lost" boolean,
  "Safety" boolean
);

CREATE TABLE "plays" (
  "Game Code" BIGINT,
  "Play Number" int,
  "Period Number" int,
  "Clock" int,
  "Offense Team Code" int,
  "Defense Team Code" int,
  "Offense Points" int,
  "Defense Points" int,
  "Down" int,
  "Distance" int,
  "Spot" int,
  "Play Type" text,
  "Drive Number" int,
  "Drive Play" boolean
);

CREATE TABLE "drive" (
  "Game Code" BIGINT,
  "Drive Number" int,
  "Team Code" int,
  "Start Period" int,
  "Start Clock" int,
  "Start Spot" int,
  "Start Reason" text,
  "End Period" int,
  "End Clock" int,
  "End Spot" int,
  "End Reason" text,
  "Plays" int,
  "Yards" int,
  "Time Of Possession" int,
  "Red Zone Attempt" boolean
);

CREATE TABLE "Game Team Stats" (
  "Team Code" int,
  "Game Code" BIGINT,
  "Rush Att" int,
  "Rush Yard" int,
  "Rush TD" int,
  "Pass Att" int,
  "Pass Comp" int,
  "Pass Yard" int,
  "Pass TD" int,
  "Pass Int" int,
  "Pass Conv" int,
  "Kickoff Ret" int,
  "Kickoff Ret Yard" int,
  "Kickoff Ret TD" int,
  "Punt Ret" int,
  "Punt Ret Yard" int,
  "Punt Ret TD" int,
  "Fum Ret" int,
  "Fum Ret Yard" int,
  "Fum Ret TD" int,
  "Int Ret" int,
  "Int Ret Yard" int,
  "Int Ret TD" int,
  "Misc Ret" int,
  "Misc Ret Yard" int,
  "Misc Ret TD" int,
  "Field Goal Att" int,
  "Field Goal Made" int,
  "Off XP Kick Att" int,
  "Off XP Kick Made" int,
  "Off 2XP Att" int,
  "Off 2XP Made" int,
  "Def 2XP Att" int,
  "Def 2XP Made" int,
  "Safety" int,
  "Points" int,
  "Punt" int,
  "Punt Yard" int,
  "Kickoff" int,
  "Kickoff Yard" int,
  "Kickoff Touchback" int,
  "Kickoff Out-Of-Bounds" int,
  "Kickoff Onside" int,
  "Fumble" int,
  "Fumble Lost" int,
  "Tackle Solo" int,
  "Tackle Assist" int,
  "Tackle For Loss" int,
  "Tackle For Loss Yard" int,
  "Sack" int,
  "Sack Yard" int,
  "QB Hurry" int,
  "Fumble Forced" int,
  "Pass Broken Up" int,
  "Kick/Punt Blocked" int,
  "1st Down Rush" int,
  "1st Down Pass" int,
  "1st Down Penalty" int,
  "Time Of Possession" int,
  "Penalty" int,
  "Penalty Yard" int,
  "Third Down Att" int,
  "Third Down Conv" int,
  "Fourth Down Att" int,
  "Fourth Down Conv" int,
  "Red Zone Att" int,
  "Red Zone TD" int,
  "Red Zone Field Goal" int
);

CREATE TABLE "Player Game Stats" (
  "Player Code" int,
  "Game Code" BIGINT,
  "Rush Att" int,
  "Rush Yard" int,
  "Rush TD" int,
  "Pass Att" int,
  "Pass Comp" int,
  "Pass Yard" int,
  "Pass TD" int,
  "Pass Int" int,
  "Pass Conv" int,
  "Rec" int,
  "Rec Yards" int,
  "Rec TD" int,
  "Kickoff Ret" int,
  "Kickoff Ret Yard" int,
  "Kickoff Ret TD" int,
  "Punt Ret" int,
  "Punt Ret Yard" int,
  "Punt Ret TD" int,
  "Fum Ret" int,
  "Fum Ret Yard" int,
  "Fum Ret TD" int,
  "Int Ret" int,
  "Int Ret Yard" int,
  "Int Ret TD" int,
  "Misc Ret" int,
  "Misc Ret Yard" int,
  "Misc Ret TD" int,
  "Field Goal Att" int,
  "Field Goal Made" int,
  "Off XP Kick Att" int,
  "Off XP Kick Made" int,
  "Off 2XP Att" int,
  "Off 2XP Made" int,
  "Def 2XP Att" int,
  "Def 2XP Made" int,
  "Safety" int,
  "Points" int,
  "Punt" int,
  "Punt Yard" int,
  "Kickoff" int,
  "Kickoff Yard" int,
  "Kickoff Touchback" int,
  "Kickoff Out-Of-Bounds" int,
  "Kickoff Onside" int,
  "Fumble" int,
  "Fumble Lost" int,
  "Tackle Solo" int,
  "Tackle Assist" int,
  "Tackle For Loss" int,
  "Tackle For Loss Yard" int,
  "Sack" int,
  "Sack Yard" int,
  "QB Hurry" int,
  "Fumble Forced" int,
  "Pass Broken Up" int,
  "Kick/Punt Blocked" int
);