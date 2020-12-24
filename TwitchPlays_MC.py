import socket
import threading
import time
import keypresser
import mouse
import keyboard
import datetime
import csv
#from ahk import AHK
#ahk = AHK(executable_path='C:\\Program Files\\AutoHotkey\\AutoHotkey.exe')

SERVER = "irc.twitch.tv" #固定
PORT = 6667 #固定

#Your OAUTH Code Here https://twitchapps.com/tmi/
PASS = "oauth:xex3y8k1uf78kuoxyzoi2fuc0ya0aj" #自分のTwitchアカから取れるので、変える(変更DONE)

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
	def printout():
		dt_now = datetime.datetime.now()
		time = dt_now.strftime('%Y%m%d_%H%M%S')
		file = open(r'C:\SHQ_mci_files\CommandData_' + time + '.csv', 'w')
		for i in range(len(TimeStamp)):
			w = csv.writer(file)
			w.writerow([TimeStamp[i],command[i]])
		file.close()

	global message
	while True:
		time.sleep(0.001)
		#mousemoving.offset_move(mouse_offset_x, mouse_offset_y)
		if playername == user.lower():
			if "st" == message.lower(): #Minecraft用のコマンドたち
				mouse.move(0, 0, absolute=False, duration=0.1)
				keypresser.key_up('w')
				keypresser.key_up('a')
				keypresser.key_up('s')
				keypresser.key_up('d')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append(message)
			elif "up" == message.lower():
				mouse.move(0, -2, absolute=False, duration=0.5)
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('up')
			elif "do" == message.lower():
				mouse.move(0, 2, absolute=False, duration=0.5)
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('down')
			elif "l" == message.lower():
				mouse.move(-5, 0, absolute=False, duration=0.5)
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('l')
			elif "r" == message.lower():
				mouse.move(5, 0, absolute=False, duration=0.5)
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('r')
			elif "w" == message.lower():
				keypresser.key_down('w')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('w')
			elif "a" == message.lower():
				keypresser.key_down('a')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('a')
			elif "s" == message.lower():
				keypresser.key_down('s')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('s')
			elif "d" == message.lower():
				keypresser.key_down('d')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('d')
			elif "sw" == message.lower():
				keypresser.key_down('w')
				time.sleep(1)
				keypresser.key_up('w')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('w')
			elif "sa" == message.lower():
				keypresser.key_down('a')
				time.sleep(1)
				keypresser.key_up('a')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('sa')
			elif "ss" == message.lower():
				keypresser.key_down('s')
				time.sleep(1)
				keypresser.key_up('s')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('ss')
			elif "sd" == message.lower():
				keypresser.key_down('d')
				time.sleep(1)
				keypresser.key_up('d')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('sd')
			elif "mu" == message.lower():
				mouse.move(0, -50, absolute=False, duration=0.5)
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('mup')
			elif "mdo" == message.lower():
				mouse.move(0, 50, absolute=False, duration=0.5)
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('mdown')
			elif "ml" == message.lower():
				mouse.move(-50, 0, absolute=False, duration=0.5)
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('ml')
			elif "mr" == message.lower():
				mouse.move(50, 0, absolute=False, duration=0.5)
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('mr')
			elif "c" == message.lower():
				mouse.click()
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('click')
			elif "op" == message.lower():
				mouse.right_click()
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('open')
			elif "cl" == message.lower():
				keypresser.key_down('e')
				keypresser.key_up('e')
				message = ""
				TimeStamp.append(datetime.datetime.now().isoformat())
				command.append('close')


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