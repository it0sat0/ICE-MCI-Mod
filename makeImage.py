import cv2
import os

def save_frame_range_sec(player, level, video_path, start_sec, stop_sec, step_sec,
                         dir_path, basename, ext='jpg'):
    cap = cv2.VideoCapture(video_path)
    c = 0

    if not cap.isOpened():
        return c

    #os.makedirs(dir_path, exist_ok=True)
    os.makedirs(dir_path)
    base_path = os.path.join(dir_path, basename)

    digit = len(str(int(cap.get(cv2.CAP_PROP_FRAME_COUNT))))

    fps = cap.get(cv2.CAP_PROP_FPS)
    fps_inv = 1 / fps

    sec = start_sec
    while sec < stop_sec:
        n = round(fps * sec)
        cap.set(cv2.CAP_PROP_POS_FRAMES, n)
        ret, frame = cap.read()
        if ret:
            cv2.imwrite(
                '{}{}.{}'.format(
                   player + '_images'+ level + '/', c, ext
                ),
                frame
            )
        else:
            return c
        oimg = cv2.imread(player + '_images'+ level + '/' + str(c) + ".jpg")
        h, w, channel = oimg.shape
        timg = oimg[ int(h/2+h/6) : int(h-h/5)-10, 0 : w]
        cv2.imwrite(str(base_path) + str(c) + ".jpg", timg)

        sec += step_sec
        c += 1
    return c