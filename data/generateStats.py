import os
from glob import glob

##################################################################
### This Unzips all seasons files under its own year directory ###
##################################################################
def unzipAllfiles():    
    dir = os.getcwd()
    paths = glob(dir+"/*.zip")

    year = 2005
    for path in paths:
        command = "unzip " +  '\"' + path  + '\" '+ " -d "  + str(year) 
        os.system(command)
        year +=1


if __name__ == "__main__":
    unzipAllfiles()
    