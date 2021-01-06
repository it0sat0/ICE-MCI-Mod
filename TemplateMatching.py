import numpy as np
import cv2
from makeImage import save_frame_range_sec
from matplotlib import pyplot as plt

player = 'inoue'
level = '11'
time_leng = 600
print('movies/'+ player+level +'.mp4')
print(player +'_images' + level)
image = save_frame_range_sec(player, level, 'movies/'+ player + level +'.mp4', 0, time_leng, 0.1, player +'_images' + level, 'img')

maxvalu = []
t = []
tc = 0
#temp = 33 #no 0~20, yse 21~
#temp = 11 #temp1
temp = 21 #temp2
count = 0
time = 0

for i_num in range(image):
    val = 0
    for t_num in range(temp):
        img = cv2.imread(player +'_images'+ level +'/img' + str(i_num) + '.jpg',0)
        img2 = img.copy()
        template = cv2.imread('templates2/t' + str(t_num) + '.jpg',0) #template num
        w, h = template.shape[::-1]

        methods = ['cv2.TM_CCOEFF']

        for meth in methods:
            img = img2.copy()
            method = eval(meth)

            # Apply template Matching
            res = cv2.matchTemplate(img,template,method)
            min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(res)

            if val < max_val:
                val = max_val
                count = t_num

            top_left = max_loc
            bottom_right = (top_left[0] + w, top_left[1] + h)
            cv2.rectangle(img,top_left, bottom_right, 255, 2)
            
            """
            plt.imshow(img,cmap = 'gray')
            plt.title(str(i_num)+'_'+ str(t_num)), plt.xticks([]), plt.yticks([])
            plt.suptitle(meth)
            plt.show()
            """
    #print(count)
    if count < 14:      #temp->21 temp1->7 temp2->14
        maxvalu.append(0)
    else:
        maxvalu.append(1)
        time += 1

    t.append(tc)
    tc += 1

#print(np.average(maxvalu))
print('time:' + str(time*0.1))

plt.plot(t, maxvalu, marker='o')
plt.show()