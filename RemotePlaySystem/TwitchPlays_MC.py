import win32gui
import socket
import threading
import time
import keypresser
import mouse
import keyboard
import datetime
import csv

hwnd = win32gui.FindWindow(None, 'Minecraft* 1.15.2 - シングルプレイ') #ここ！！変更の必要あり！！
win32gui.SetForegroundWindow(hwnd)
x,y,height,width = win32gui.GetWindowRect(hwnd)
#772 368 1650 887
print('------------------')
print(x,y,width,height) #ここも間違ってそう
print('------------------')

#from ahk import AHK
#ahk = AHK(executable_path='C:\\Program Files\\AutoHotkey\\AutoHotkey.exe')

SERVER = "irc.twitch.tv" #固定
PORT = 6667 #固定

#Your OAUTH Code Here https://twitchapps.com/tmi/
PASS = "oauth:hm336j4zk99b9121e1nutixh1oghx1" #自分のTwitchアカから取れるので、変える(変更DONE)
#こちらに変更かもoauth:qo79khjfjstpusa8mar51aqdgikgjw
#What you'd like to name your bot
BOT = "ICE_MCI_testBot"

#The channel you want to monitor
CHANNEL = "it0sat0" #自分のチャンネルの名前

#Your account
OWNER = "it0sat0" #自分のチャンネルの名前

message = ""
user = ""

irc = socket.socket()

irc.connect((SERVER, PORT))
irc.send((	"PASS " + PASS + "\n" +
			"NICK " + BOT + "\n" +
			"JOIN #" + CHANNEL + "\n").encode())

TimeStamp = []
command = []

playername = "it0sat0sub"  #ちゃんと変更すること

def gamecontrol():  #あまり気にしなくていい
	global message
	while True:
		time.sleep(0.001)
		#mousemoving.offset_move(mouse_offset_x, mouse_offset_y)
		if playername == user.lower():
			if "st" == message.lower(): #旧システムのコマンド
				mouse.move(0, 0, absolute=False, duration=0.1)
				keypresser.key_up('w')
				keypresser.key_up('a')
				keypresser.key_up('s')
				keypresser.key_up('d')
				message = ""
			elif "mo" in message.lower():
				l = message.lower().split(',')
				print('------------------')
				print(x,y,width,height) 
				print('------------------')
				mouse.move(x+(height-x)*float(l[1]),y+(width-y)*float(l[2]),absolute=True, duration=0.0)
				print(x+(height-x)*float(l[1]),y+(width-y)*float(l[2]),x,y) 
				time.sleep(0.3)
				mouse.right_click()
				message = ""
			elif "t" == message.lower():
				mouse.move(0, -5, absolute=False, duration=0.5)
				message = ""
			elif "b" == message.lower():
				mouse.move(0, 5, absolute=False, duration=0.5)
				message = ""
			elif "r" == message.lower():
				mouse.move(5, 0, absolute=False, duration=0.5)
				message = ""
			elif "l" == message.lower():
				mouse.move(-5, 0, absolute=False, duration=0.5)
				message = ""
			elif "mt" == message.lower():
				mouse.move(0, -30, absolute=False, duration=0.5)
				message = ""
			elif "ml" == message.lower():
				mouse.move(-30, 0, absolute=False, duration=0.5)
				message = ""
			elif "mb" == message.lower():
				mouse.move(0, 30, absolute=False, duration=0.5)
				message = ""
			elif "mr" == message.lower():
				mouse.move(30, 0, absolute=False, duration=0.5)
				message = ""
			elif "w" == message.lower(): #ここをどうするか相談中
				keypresser.key_down('w')
				time.sleep(1)
				keypresser.key_up('w')
				message = ""
			elif "a" == message.lower():
				keypresser.key_down('a')
				time.sleep(1)
				keypresser.key_up('a')
				message = ""
			elif "s" == message.lower():
				keypresser.key_down('s')
				time.sleep(1)
				keypresser.key_up('s')
				message = ""
			elif "d" == message.lower():
				keypresser.key_down('d')
				time.sleep(1)
				keypresser.key_up('d')
				message = ""
			elif "c" == message.lower():
				mouse.click()
				message = ""
			elif "op" == message.lower():
				time.sleep(0.2)
				mouse.right_click()
				message = ""
			elif "ow" == message.lower():
				keypresser.key_down('w')
				time.sleep(0.5)
				keypresser.key_up('w')
				message = ""
			elif "oa" == message.lower():
				keypresser.key_down('a')
				time.sleep(0.5)
				keypresser.key_up('a')
				message = ""
			elif "os" == message.lower():
				keypresser.key_down('s')
				time.sleep(0.5)
				keypresser.key_up('s')
				message = ""
			elif "od" == message.lower():
				keypresser.key_down('d')
				time.sleep(0.5)
				keypresser.key_up('d')
				message = ""
			elif "o" in message.lower():
				message = ""
			elif "m" in message.lower():
				message = ""
			elif "t" in message.lower():
				message = ""
			elif "b" in message.lower():
				message = ""
			elif "r" in message.lower():
				message = ""
			elif "cl" == message.lower(): #旧、新どちらにも使用する
				keypresser.key_down('e')
				keypresser.key_up('e')
				message = ""
			elif "l" in message.lower():
				message = ""
			elif "open" == message.lower(): #ここから、新システム用コマンド
				time.sleep(1.0)
				mouse.right_click()
				message = ""
			elif "craft" == message.lower(): 
				time.sleep(1.0)
				mouse.right_click()
				message = ""
			elif "vw" in message.lower():
				l = message.lower().split(',')
				mouse.move(0, -3*int(l[1]), absolute=False, duration=0.5)
				message = ""
			elif "vs" in message.lower():
				l = message.lower().split(',')
				mouse.move(0, 3*int(l[1]), absolute=False, duration=0.5)
				message = ""
			elif "va" in message.lower():
				l = message.lower().split(',')
				mouse.move(-3*int(l[1]), 0, absolute=False, duration=0.5)
				message = ""
			elif "vd" in message.lower():
				l = message.lower().split(',')
				mouse.move(3*int(l[1]), 0, absolute=False, duration=0.5)
				message = ""
			elif "w," in message.lower():
				l = message.lower().split(',')
				keypresser.key_down('w')
				time.sleep(int(l[1]))
				keypresser.key_up('w')
				message = ""
			elif "a," in message.lower():
				l = message.lower().split(',')
				keypresser.key_down('a')
				time.sleep(int(l[1]))
				keypresser.key_up('a')
				message = ""
			elif "s," in message.lower():
				l = message.lower().split(',')
				keypresser.key_down('s')
				time.sleep(int(l[1]))
				keypresser.key_up('s')
				message = ""
			elif "d," in message.lower():
				l = message.lower().split(',')
				keypresser.key_down('d')
				time.sleep(int(l[1]))
				keypresser.key_up('d')
				message = ""
			'''
			elif "cr" == message.lower():
				time.sleep(0.2)
				mouse.right_click()
				message = ""
			'''

def twitch(): #まるごとコピーして使ったらいい。いつもコメントを取得している

	global user
	global message

	def joinchat():
		Loading = True
		while Loading:
			readbuffer_join = irc.recv(1024)
			readbuffer_join = readbuffer_join.decode()
			print(readbuffer_join)
			for line in readbuffer_join.split("\n")[0:-1]:
				print(line)
				Loading = loadingComplete(line)

	def loadingComplete(line):
		if("End of /NAMES list" in line):
			print("TwitchBot has joined " + CHANNEL + "'s Channel!")
			sendMessage(irc, "Hello World!")
			return False
		else:
			return True

	def sendMessage(irc, message):
		messageTemp = "PRIVMSG #" + CHANNEL + " :" + message
		irc.send((messageTemp + "\n").encode())

	def getUser(line):
		#global user
		colons = line.count(":")
		colonless = colons-1
		separate = line.split(":", colons)
		user = separate[colonless].split("!", 1)[0]
		return user

	def getMessage(line):
		#global message
		try:
			colons = line.count(":")
			message = (line.split(":", colons))[colons]
		except:
			message = ""
		return message

	def console(line):
		if "PRIVMSG" in line:
			return False
		else:
			return True

	joinchat()
	irc.send("CAP REQ :twitch.tv/tags\r\n".encode())
	while True:
		try:
			readbuffer = irc.recv(1024).decode()
		except:
			readbuffer = ""
		for line in readbuffer.split("\r\n"):
			if line == "":
				continue
			if "PING :tmi.twitch.tv" in line:
				print(line)
				msgg = "PONG :tmi.twitch.tv\r\n".encode()
				irc.send(msgg)
				print(msgg)
				continue
			else:
				try:
					user = getUser(line)
					message = getMessage(line)
					print(user + " : " + message)
				except Exception:
					pass

def main():
	if __name__ =='__main__':
		for i in range(3):
			print(3-i)
			time.sleep(1)
		print('start')
		t1 = threading.Thread(target = twitch)
		t1.start()
		t2 = threading.Thread(target = gamecontrol)
		t2. start()
main()