import csv
import pprint

player = "playername"
level = "11"

#level1 and 2 and 3(test)
LowLevel = [[[-132,-631],[-103,-678]],[[-494,719],[-515,643]]]
#level6,8 and 11
HighLevel = [[[116,58],[115,42],[143,-13],[85,-6]],[[19,129],[38,93],[-17,111],[3,57]],[[352,20],[315,-57],[313,-4],[348,-41]]]

time = 0
stop = 0

if level == "1":
    lev = 0
elif level == "2":
    lev = 1
elif level == "6":
    lev = 0
elif level == "8":
    lev = 1
elif level == "11":
    lev = 2

with open('C:/minecraft_mci_files/' + player + '/level' + level + '_MovementResult.csv') as f:
    reader = csv.reader(f)
    l = [row for row in reader]

    if level=="1" or level=="2":
        for i in range(len(l)-1):
            if l[i][4] == l[i+1][4]:
                if LowLevel[lev][1][0] - 5 <= int(float(l[i][1])) and int(float(l[i][1])) <= LowLevel[lev][1][0] + 5:
                    if LowLevel[lev][1][1] - 5 <= int(float(l[i][3])) and int(float(l[i][3])) <= LowLevel[lev][1][1] + 5:
                            stop += 1
                            print(i)
                    if LowLevel[lev][0][0] - 5 <= int(float(l[i][1])) and int(float(l[i][1])) <= LowLevel[lev][0][0] + 5:
                        if LowLevel[lev][0][1] - 5 <= int(float(l[i][3])) and int(float(l[i][3])) <= LowLevel[lev][0][1] + 5:
                            if l[i][5] != l[0][5] and l[i][6] != l[0][6]:
                                stop += 1
                                print("-")
                                print(i)
    else:
        for i in range(len(l)-1):
            if l[i][4] == l[i+1][4]:
                for j in range(1,4):
                    if HighLevel[lev][j][0] - 5 <= int(float(l[i][1])) and int(float(l[i][1])) <= HighLevel[lev][j][0] + 5:
                        if HighLevel[lev][j][1] - 5 <= int(float(l[i][3])) and int(float(l[i][3])) <= HighLevel[lev][j][1] + 5:
                            stop += 1
                            print(i)
                if HighLevel[lev][0][0] - 5 <= int(float(l[i][1])) and int(float(l[i][1])) <= HighLevel[lev][0][0] + 5:
                    if HighLevel[lev][0][1] - 5 <= int(float(l[i][3])) and int(float(l[i][3])) <= HighLevel[lev][0][1] + 5:
                        if l[i][5] != l[0][5] and l[i][6] != l[0][6]:
                            stop += 1
                            print("-")
                            print(i)

    print("--------------------")
    print(len(l)-stop+1)