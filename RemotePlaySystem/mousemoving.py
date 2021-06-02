import win32api
import win32gui

def move(x, y):
    win32api.SetCursorPos((x, y))

def get_cur_pos():
    p={"x":0,"y":0}
    pos = win32gui.GetCursorPos()
    p['x']=pos[0]
    p['y']=pos[1]
    return p

def offset_move(x, y):
	pos = win32gui.GetCursorPos()
	move(pos[0]+x, pos[1]+y)
