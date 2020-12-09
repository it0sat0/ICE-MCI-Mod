import numpy as np
import cv2
from makeImage import save_frame_range_sec
from matplotlib import pyplot as plt

image = save_frame_range_sec('movies/level6.mp4', 0, 5, 0.1, 'images', 'img')

#image = 10
temp = 8
count = 0
val = []
#t = [1,2,3,4,5,6,7,8]
tc = 0
t = []
for t_num in range(temp):
    val = []
    t = []
    tc = 0
    for i_num in range(image):
        img = cv2.imread('images/img' + str(i_num) + '.jpg',0)
        img2 = img.copy()
        template = cv2.imread('templates/t' + str(t_num) + '.jpg',0)
        w, h = template.shape[::-1]

        # All the 6 methods for comparison in a list
        #, 'cv2.TM_CCOEFF_NORMED', 'cv2.TM_CCORR', 'cv2.TM_CCORR_NORMED'
        methods = ['cv2.TM_CCOEFF']

        for meth in methods:
            img = img2.copy()
            method = eval(meth)

            # Apply template Matching
            res = cv2.matchTemplate(img,template,method)
            min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(res)
            #print(max_val, max_loc)
            val.append(max_val)
            t.append(tc)
            tc += 1

            # If the method is TM_SQDIFF or TM_SQDIFF_NORMED, take minimum
            """
            if method in [cv2.TM_SQDIFF, cv2.TM_SQDIFF_NORMED]:
                top_left = min_loc
            else:
            """
            top_left = max_loc
            bottom_right = (top_left[0] + w, top_left[1] + h)
            cv2.rectangle(img,top_left, bottom_right, 255, 2)
            """
            plt.subplot(122),plt.imshow(img,cmap = 'gray')
            plt.title('Detected Point_'+ str(i_num)+str(t_num)), plt.xticks([]), plt.yticks([])
            plt.suptitle(meth)
            """

            plt.show()
    """
    plt.plot(t, val, marker='o')
    plt.show()
    """
    print(t_num)
    print(np.average(val))
    print('------------------')