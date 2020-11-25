import socket
import threading
import time
import keypresser
import mousemoving
import mouse
import keyboard
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

def gamecontrol():  #あまり気にしなくていい
	global message
	#keypresser.key_down('a')
	mouse_offset_x = 0
	mouse_offset_y = 0
	while True:
		time.sleep(0.001)
		#mousemoving.offset_move(mouse_offset_x, mouse_offset_y)
		if "stop" == message.lower():
			mouse_offset_x = 0
			mouse_offset_y = 0
			keypresser.key_up('w')
			keypresser.key_up('a')
			keypresser.key_up('s')
			keypresser.key_up('d')
			message = ""
		elif "forward" == message.lower():
			keypresser.key_up('s')
			keypresser.key_down('w')
			message = ""
		elif "backward" == message.lower():
			keypresser.key_up('w')
			keypresser.key_down('s')
			message = ""
		elif "leftward" == message.lower():
			keypresser.key_up('d')
			keypresser.key_down('a')
			message = ""
		elif "rightward" == message.lower():
			keypresser.key_up('a')
			keypresser.key_down('d')
			message = ""

		elif "rotate up" == message.lower():
			mouse_offset_y = -2
			message = ""
		elif "rotate down" == message.lower():
			mouse_offset_y = 2
			message = ""
		elif "rotate left" == message.lower():
			mouse_offset_x = -2
			message = ""
		elif "rotate right" == message.lower():
			mouse_offset_x = 2
			message = ""

		elif "w" == message.lower():
			keypresser.key_down('w')
			time.sleep(2)
			keypresser.key_up('w')
			message = ""
		elif "a" == message.lower():
			keypresser.key_down('a')
			time.sleep(2)
			keypresser.key_up('a')
			message = ""
		elif "s" == message.lower():
			keypresser.key_down('s')
			time.sleep(2)
			keypresser.key_up('s')
			message = ""
		elif "d" == message.lower():
			keypresser.key_down('d')
			time.sleep(2)
			keypresser.key_up('d')
			message = ""
		elif "rise" == message.lower():
			keypresser.key_down('space')
			time.sleep(2)
			keypresser.key_up('space')
			message = ""
		elif "drop" == message.lower():
			keypresser.key_down('SHIFT')
			time.sleep(2)
			keypresser.key_up('SHIFT')
			message = ""
		elif "fly" == message.lower():
			keypresser.key_down('space')
			time.sleep(0.06)
			keypresser.key_up('space')
			time.sleep(0.06)
			keypresser.key_down('space')
			time.sleep(2)
			keypresser.key_up('space')
			message = ""
		elif "mup" == message.lower():
			mouse.move(0, -50, absolute=False, duration=1)
			message = ""
		elif "mdown" == message.lower():
			mouse.move(0, 50, absolute=False, duration=1)
			message = ""
		elif "mleft" == message.lower():
			mouse.move(-50, 0, absolute=False, duration=1)
			message = ""
		elif "mright" == message.lower():
			mouse.move(50, 0, absolute=False, duration=1)
			message = ""
		elif "click" == message.lower():
			mouse.click()
			message = ""
		elif "open" == message.lower():
			mouse.right_click()
			message = ""

		'''
		elif "vup" == message.lower():
			#mousemoving.offset_move(0, -200)
			message = ""
		elif "vdown" == message.lower():
			#mousemoving.offset_move(0, 200)
			message = ""
		elif "vright" == message.lower():
			#mousemoving.offset_move(200, 0)
			message = ""
		elif "vleft" == message.lower():
			#mousemoving.offset_move(-200, 0)
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