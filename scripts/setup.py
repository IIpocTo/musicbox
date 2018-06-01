from setuptools import setup

setup(name='spotify_importer',
	version='1.0',
	description='Spotify import data',
	author='George Carpow',
	packages=['spotify_importer'],
	install_requires=['spotipy', 'pymongo'],
)
