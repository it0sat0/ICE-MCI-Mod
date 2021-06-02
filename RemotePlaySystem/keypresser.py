# coding=utf-8
import win32con
import win32api

key_map = {
    "0": 49, "1": 50, "2": 51, "3": 52, "4": 53, "5": 54, "6": 55, "7": 56, "8": 57, "9": 58,
    "A": 65, "B": 66, "C": 67, "D": 68, "E": 69, "F": 70, "G": 71, "H": 72, "I": 73, "J": 74,
    "K": 75, "L": 76, "M": 77, "N": 78, "O": 79, "P": 80, "Q": 81, "R": 82, "S": 83, "T": 84,
    "U": 85, "V": 86, "W": 87, "X": 88, "Y": 89, "Z": 90, "SPACE": 32, "SHIFT": 16
}

def key_down(key):
	#if key not in key_map:
	#	return
	key = key.upper()
	vk_code = key_map[key]
	#print(vk_code)
	win32api.keybd_event(vk_code,win32api.MapVirtualKey(vk_code, 0),0,0)

def key_up(key):
	#if key not in key_map:
	#	return
	key = key.upper()
	vk_code = key_map[key]
	win32api.keybd_event(vk_code, win32api.MapVirtualKey(vk_code, 0), win32con.KEYEVENTF_KEYUP, 0)