import numpy as np
import cv2
import mss
# from matplotlib import pyplot as plt
import threading
import time
import entropy
import mouse
import keyboard

# cap = cv2.VideoCapture(cv2.samples.findFile("slow_traffic_small.mp4"))
# ret, frame1 = cap.read()
w = 720
h = 480
# plt.figure(figsize=(8, 6), dpi=80)
exitFlag = 0
frames = None
flows = None
monitor = {'top': 30, 'left': 0, 'width': w, 'height': h}


class ScreenRecord(threading.Thread):
    def __init__(self, thread_id, name):
        threading.Thread.__init__(self)
        self.threadID = thread_id
        self.name = name

    def run(self):
        global frames, flows
        print("开始线程：" + self.name)
        with mss.mss() as sct:
            frame1 = cv2.cvtColor(np.array(sct.grab(monitor)), cv2.COLOR_BGRA2BGR)
            prvs = cv2.cvtColor(frame1, cv2.COLOR_BGR2GRAY)
            hsv = np.zeros_like(frame1)
            hsv[..., 1] = 255
            while 'Screen capturing':
                #last_time = time.time()
                frame2 = cv2.cvtColor(np.array(sct.grab(monitor)), cv2.COLOR_BGRA2BGR)
                frame_h = [frame2[0:h // 3, :], frame2[h // 3:2 * h // 3, :], frame2[2 * h // 3:h, :]]
                frame_v = [frame2[:, 0:w // 3], frame2[:, w // 3:2 * w // 3], frame2[:, 2 * w // 3:w]]
                frames = [frame_h, frame_v]
                # cv2.imshow('OpenCV/Numpy normal0', frame2[:, :, 0])
                # cv2.imshow('OpenCV/Numpy normal1', frame2[:, :, 1])
                # cv2.imshow('OpenCV/Numpy normal2', frame2[:, :, 2])
                next = cv2.cvtColor(frame2, cv2.COLOR_BGR2GRAY)
                flow = cv2.calcOpticalFlowFarneback(prvs, next, None, 0.5, 3, 15, 3, 5, 1.2, 0)
                mag, ang = cv2.cartToPolar(flow[..., 0], flow[..., 1])
                hsv[..., 0] = ang * 180 / np.pi / 2
                hsv[..., 2] = cv2.normalize(mag, None, 0, 255, cv2.NORM_MINMAX)
                bgr = cv2.cvtColor(hsv, cv2.COLOR_HSV2BGR)
                flow_h = [bgr[0:h // 3, :], bgr[h // 3:2 * h // 3, :], bgr[2 * h // 3:h, :]]
                flow_v = [bgr[:, 0:w // 3], bgr[:, w // 3:2 * w // 3], bgr[:, 2 * w // 3:w]]
                flows = [flow_h, flow_v]
                #print('fps: {0}'.format(1 / (time.time() - last_time)))
                # k = cv2.waitKey(30) & 0xff
                # if k == 27:
                #     break
                prvs = next

def normalization(data):
    _range = np.max(abs(data))
    return data / _range


def save_entropy(frame2, fout, action):
    fout.write(str(entropy.calcEntropy(frame2[:, :, 0])) + " ")
    fout.write(str(entropy.calcEntropy(frame2[:, :, 1])) + " ")
    fout.write(str(entropy.calcEntropy(frame2[:, :, 2])) + " ")


def save(images_h, images_v, fout, action):
    dataf_h = np.array([np.sum(images_h[0]), np.sum(images_h[1]), np.sum(images_h[2])])
    dataf_h = normalization(dataf_h)
    for one in dataf_h:
        fout.write(str(one) + " ")

    dataf_v = np.array([np.sum(images_v[0]), np.sum(images_v[1]), np.sum(images_v[2])])
    dataf_v = normalization(dataf_v)
    for one in dataf_v:
        fout.write(str(one) + " ")
    fout.write(action + " \n")

def test(command): #TwitchPlaysで気にしなくてよかったところ。ここは気にしておこう
    # cv2.imshow("up", frames[0][0])
    # cv2.imshow("middle", frames[0][1])
    # cv2.imshow("down", frames[0][2])
    # cv2.waitKey(0)
    # cv2.destroyAllWindows()
    if command is "1":
        keyboard.press('w') #'w'を押した
        #mouse.move(0, 30, absolute=False, duration=0.1)
    elif command is "2":
        mouse.move(30, 0, absolute=False, duration=1)
    elif command is "3":
        keyboard.release('w') #リリースに'w'を入れた
        #mouse.move(0, -30, absolute=False, duration=0.1)
    else:
        mouse.move(-30, 0, absolute=False, duration=1) #x軸とy軸の値をいれる。duration＝このうごきを何秒で終わらせるか
    # keyboard.press("esc")
    # keyboard.release(hotkey)

#以下の4行はいらない。TwitchPlayではいらない。
keyboard.add_hotkey('up', test, args="1")
keyboard.add_hotkey('down', test, args="3")
keyboard.add_hotkey('right', test, args="2")
keyboard.add_hotkey('left', test, args="4")

#この下もいらない
sr = ScreenRecord(0, 'sr')
sr.start()

keyboard.wait('esc')
