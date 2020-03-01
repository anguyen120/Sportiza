import os
import csv
import pandas as pd

#creates a list of all directories for a specified type of table for all seasons

mergedTablesPath = "/home/edgarp/school/CSCE315/project2/Sportiza/mergedCSVtables/"
def tableDirectories(csvFileName):
    allSeasonsDir= "/home/edgarp/school/CSCE315/project2/Sportiza/data/"
    seasonsDirs = [allSeasonsDir +  str(i) + "/" + csvFileName for i in range(2005,2014)]
    return seasonsDirs


#merges all conferences tables
def mergeConferences(directories):
    column_names = ["Conference Code", "Name", "Subdivision"]
    conferenceTable = pd.DataFrame(columns = column_names)
    secondTable = pd.read_csv(directories[0])
    uniqueConferences = 0
    for dir in directories:
        seasonTable = pd.read_csv(dir)
        for index, row in seasonTable.iterrows():
            if row["Conference Code"] in conferenceTable["Conference Code"].values:
                print("Conference: " + str(row["Name"]) + " Already Exists")
            else:
                print("Adding a new Conference: " +  str(row["Name"]))
                pandaRow = dict(zip(column_names,row))
                conferenceTable = conferenceTable.append(pandaRow,ignore_index=True)
                uniqueConferences +=1
        print("Number of Conferences: " + str(seasonTable.shape[0]))
    print("Total of UNique conferences: " + str(uniqueConferences))
    print("-----------Writing Merged Table to CSV--------------")
    conferenceTable.to_csv(mergedTablesPath+"Merged-Conferences.csv",index=False)

#merges tables of the same type without changing anything
#does not check for dulicates
def mergeTablesNoChange(tableName):
    directories = tableDirectories(tableName)
    column_names =  csv.DictReader(open(directories[0],'r')).fieldnames
    mergedTable = pd.DataFrame(columns=column_names)
    for dir in directories:
        seasonTable = pd.read_csv(dir)
        mergedTable = mergedTable.append(seasonTable)
        print("Table " + dir + " has:  " + str(seasonTable.shape[0]) + " rows" )
    print(mergedTable)
    print("Number of rows for merged Table " + str(mergedTable.shape[0]) )
    mergedTable.to_csv(mergedTablesPath+"Merged-" + tableName,index=False)

#mergin all game tables with game statistics
def mergeGameTables():
    gameTableDirs = tableDirectories("game.csv")
    column_names = ["Game Code","Date","Visit Team Code","Home Team Code","Stadium Code","Site", "Season"]
    gameTable = pd.DataFrame(columns = column_names)
    year = 2005
    totalRows  = 0 
    for gameDir in gameTableDirs:
        seasonTable = pd.read_csv(gameDir)
        for index, row in seasonTable.iterrows():
            #print(row)
            pandaRow = dict(zip(column_names[:],row))
            pandaRow["Season"] = year
            #making sure there is not a duplicate game code
            if pandaRow["Game Code"] in gameTable["Game Code"].values:
                print("Game Code: " + pandaRow["Game Code"] + " is not unique in table")
            else:
                gameTable = gameTable.append(pandaRow, ignore_index= True)
        print("Number of rows for " + gameDir + " Table : " + str(seasonTable.shape[0]) + " on year " + str(year) )
        year +=1
        totalRows += seasonTable.shape[0]
    print("Total Number of Rows for Merged Table: " + str(totalRows))
    print("--------------Converting to CSV------------")
    gameTable.to_csv(mergedTablesPath+"Merged-" + "games.csv", index=False)

def mergePlayerTables():
    directories = tableDirectories("player.csv")
    column_names = ["Player Code","Team Code","Last Name","First Name","Uniform Number","Class","Position","Height","Weight","Home Town","Home State","Home Country","Last School", "Season"]

    mergedTable = pd.DataFrame(columns = column_names)
    year = 2005
    for dir in directories:
        playerTable = pd.read_csv(dir)   
        for index, row in playerTable.iterrows():
            pandaRow = dict(zip(column_names,row))
            pandaRow["Season"] = year
            mergedTable = mergedTable.append(pandaRow, ignore_index=True)
        year +=1
        print("Number of Rows for table " + dir + ": " + str(playerTable.shape[0]))
    print("Total Number of Rows for merged Table: " + str(mergedTable.shape[0]))
    mergedTable.to_csv(mergedTablesPath+"Merged-player.csv",index=False)

#merges all team tables into one adding an extra paramater which is season
def mergeTeamTables():
    directories = tableDirectories("team.csv")
    column_names = ["Team Code","Name","Conference Code", "Season"]
    mergedTable = pd.DataFrame(columns = column_names)
    year = 2005
    for dir in directories:
        teamsTable = pd.read_csv(dir)   
        for index, row in teamsTable.iterrows():
            pandaRow = dict(zip(column_names,row))
            pandaRow["Season"] = year
            #print(pandaRow)
            mergedTable = mergedTable.append(pandaRow, ignore_index=True)
        year +=1
        print("Number of Rows for table " + dir + ": " + str(teamsTable.shape[0]))
    print("Total Number of Rows for merged Table: " + str(mergedTable.shape[0]))
    print("--------------Creating CSV File------------------------")
    mergedTable.to_csv(mergedTablesPath+"Merged-Teams.csv",index=False)

def mergeStadiums():
    directories = tableDirectories("stadium.csv")
    column_names = ["Stadium Code","Name","City","State","Capacity","Surface","Year Opened"]
    mergedTable = pd.DataFrame(columns = column_names)
    for dir in directories:
        stadiumsTable = pd.read_csv(dir)   
        for index, row in stadiumsTable.iterrows():
            pandaRow = dict(zip(column_names,row))
            if pandaRow["Stadium Code"] in mergedTable["Stadium Code"].values:
                print("Stadium code: " + str(pandaRow["Stadium Code"]) + " already exists")
            else:
                mergedTable = mergedTable.append(pandaRow,ignore_index=True)
        print("Number of Rows for table " + dir + ": " + str(stadiumsTable.shape[0]))
    print("Total Number of Rows for merged Table: " + str(mergedTable.shape[0]))
    print("--------------Creating CSV File------------------------")
    mergedTable.to_csv(mergedTablesPath+"Merged-Stadiums.csv",index=False)
if __name__ == "__main__":
    mergeConferences(tableDirectories("conference.csv"))
    #mergeTablesNoChange("drive.csv")
    #mergeGameTables()
    #mergePlayerTables()
    #mergeTeamTables()
    #mergeStadiums()
    #mergeTablesNoChange("kickoff-return.csv")
    #mergeTablesNoChange("kickoff.csv")
    #mergeTablesNoChange("pass.csv")
    #mergeTablesNoChange("punt-return.csv")
    #mergeTablesNoChange("punt.csv")
    #mergeTablesNoChange("reception.csv")
    #mergeTablesNoChange("rush.csv")
    #mergeTablesNoChange("play.csv")
    #mergeTablesNoChange("player-game-statistics.csv")
    #mergeTablesNoChange("team-game-statistics.csv")
    #mergeTablesNoChange("game-statistics.csv")