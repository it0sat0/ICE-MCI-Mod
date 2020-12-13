import numpy as np
import cv2
from makeImage import save_frame_range_sec
from matplotlib import pyplot as plt

image = save_frame_range_sec('movies/move.mp4', 0, 5, 0.1, 'images', 'img')

maxvalu = []
t = []
tc = 0
#temp = 5 #temp1
#temp = 8 #temp2
temp = 7 #temp3
#temp = 14 #temp4
count = 0
for i_num in range(image):
    val = 0
    for t_num in range(temp):
        img = cv2.imread('images/img' + str(i_num) + '.jpg',0)
        img2 = img.copy()
        template = cv2.imread('templates3/t' + str(t_num) + '.jpg',0) #template num
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

            top_left = max_loc
            bottom_right = (top_left[0] + w, top_left[1] + h)
            cv2.rectangle(img,top_left, bottom_right, 255, 2)
            
            """
            plt.imshow(img,cmap = 'gray')
            plt.title(str(i_num)+'_'+ str(t_num)), plt.xticks([]), plt.yticks([])
            plt.suptitle(meth)
            plt.show()
            """
            
    maxvalu.append(val)
    t.append(tc)
    tc += 1

print(np.average(maxvalu))

plt.plot(t, maxvalu, marker='o')
plt.show()