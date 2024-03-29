import win32gui
import socket
import threading
import time
import keypresser
import mouse
import keyboard
import datetime
import csv

hwnd = win32gui.FindWindow(None, r'NoxPlayer')
win32gui.SetForegroundWindow(hwnd)
x,y,height,width = win32gui.GetWindowRect(hwnd)
print('------------------')
print(x,y,height,width)
print('------------------')
#print(dimensions) #(x,y,height,width)

#from ahk import AHK
#ahk = AHK(executable_path='C:\\Program Files\\AutoHotkey\\AutoHotkey.exe')

SERVER = "irc.twitch.tv" #固定
PORT = 6667 #固定

#Your OAUTH Code Here https://twitchapps.com/tmi/
PASS = "" #自分のTwitchアカから取れるので、変える(変更DONE)

#What you'd like to name your bot
BOT = "ICE_MCI_testBot"

#The channel you want to monitor
CHANNEL = "" #自分のチャンネルの名前

#Your account
OWNER = "" #自分のチャンネルの名前

message = ""
user = ""

irc = socket.socket()

irc.connect((SERVER, PORT))
irc.send((	"PASS " + PASS + "\n" +
			"NICK " + BOT + "\n" +
			"JOIN #" + CHANNEL + "\n").encode())

TimeStamp = []
command = []

playername = ""  #ちゃんと変更すること

host = ""

def gamecontrol():  #あまり気にしなくていい
	def printout():
		dt_now = datetime.datetime.now()
		time = dt_now.strftime('%Y%m%d_%H%M%S')
		file = open(r'C:\SHQ_mci_files\CommandData_' + playername + '_' + time + '.csv', 'w')
		for i in range(len(TimeStamp)):
			w = csv.writer(file)
			w.writerow([TimeStamp[i],command[i]])
		file.close()

	global message
	while True:
		time.sleep(0.001)
		#mousemoving.offset_move(mouse_offset_x, mouse_offset_y)
		if playername == user.lower() or host == user.lower():
			if "a" == message.lower():
				mouse.move(int(x+width*(1/8)), int(y+height*(2/3)+5), absolute=True, duration=0.0)
				mouse.press()
				time.sleep(0.5)
				mouse.release()
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('left')
			elif "d" == message.lower():
				mouse.move(int(x+width*(2/5)), int(y+height*(2/3)+5), absolute=True, duration=0.0)
				mouse.press()
				time.sleep(0.5)
				mouse.release()
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('right')
			elif "st" == message.lower():#ゲームスタート
				mouse.click()
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('start')
			elif "ex" == message.lower():#コンティニュー検出
				mouse.click()
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('exit')
				printout()
				print('DONE')
			"""
			elif "c" == message.lower():
				mouse.press()
				time.sleep(0.5)
				mouse.release()
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('click')
			"""


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
			print("TwitchBot has joined " + CHANNEL + "")
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
