from gmusicapi import Musicmanager
import sys

if __name__ == '__main__':
    Musicmanager.perform_oauth()
    client = Musicmanager()
    client.login()
    audio = client.download_song('Bo4b2a7yotth3jk55qb24kxruxm')
    print(audio)
    sys.exit(0)
