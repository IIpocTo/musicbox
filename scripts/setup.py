from setuptools import setup

setup(name='spotify_importer',
      version='1.0',
      description='Spotify import data',
      author='George Carpow',
      packages=['spotify_importer', 'google_music'],
      install_requires=[
          'pymongo',
          'spotipy',
          'gmusicapi'
      ],
      )
