import os
import csv
import sys

import csv
mergedFilesPath = "../mergedCSVtables/"
sqlScriptsPAth = "../populateSQLScripts/"
#generated command base on csvline, table name, the columns for the table, and the columns that need special handling
def generateCommand(csvLine,tableName, columns, columnsWithText):
    parameters = ("{},"*len(columns))[:-1]
    columnsFormat = ("\"{}\","*len(columns))[:-1]
    #replacing all null parameters with NULL
    line  = [x if x !='' else 'NULL'  for x in csvLine]
    #handling all columns with a text or character value
    for col in columnsWithText:
        #checking for a NUll value
        if line[col] != 'NULL':
            #replacing ' for '' to escape it
            line[col] = line[col].replace("'","''")
            line[col] = "\'{}\'".format(line[col])

    command = '''INSERT INTO "%s"('''%tableName + columnsFormat + ") VALUES(" + parameters + "); \n"
    command = command.format(*(columns  +  line))
    return command
#handles all types of insertion
def sqlScriptFromCSV(mergedFile,tableName, columnsWithTEXT):
    csv_file = open(mergedFile , "r")
    csv_reader = csv.reader(csv_file, delimiter=',')
    columns = next(csv_reader)
    sqlFile = open(sqlScriptsPAth+tableName + ".sql","w+")
    for line in csv_reader:
        command = generateCommand(line,tableName,columns,columnsWithTEXT) + "\n"
        sqlFile.write(command)
            
def usage():
    print('''
Usage:
    generateSQLscripts.py [-csvFile] [-SQLTableName] [-textColumns]
        csvFile: path to csv file that contains data
        SQLTableName: name of table that will be loaded into PostgresSQL data base
        textColumns: 0 or more column numbers in csv file that contain a varchar or text field
''')
if __name__ == "__main__":
    arguments = sys.argv
    if (len(arguments) < 3):
        usage()
    else:
        csvFile = arguments[1]
        tableName = arguments[2]
        columnsWithText = arguments[3:]
        columnsWithText = [int(i) for i in columnsWithText] 
        #print(columnsWithText)
        sqlScriptFromCSV(csvFile,tableName,columnsWithText)