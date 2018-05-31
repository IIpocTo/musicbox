import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import json
import sys
import subprocess
import time

amount = 20

if __name__ == '__main__':
    args = sys.argv
    if len(args) != 5:
        print('Invalid argument number')
        sys.exit(1)
    if args.count('--client') == 0 | args.count('--secret') == 0:
        print('Not enough arguments')
        sys.exit(1)
    client = args[args.index('--client') + 1]
    secret = args[args.index('--secret') + 1]
    client_credentials_manager = SpotifyClientCredentials(
        client,
        secret
    )
    sp = spotipy.Spotify(
        client_credentials_manager=client_credentials_manager
    )

    # get list of artists
    artists = sp.search(
        q='band',
        limit=amount,
        type='artist'
    )['artists']
    res_artists = []
    res_albums = []
    res_tracks = []

    for i, artist in enumerate(artists['items']):
        try:
            el = {}
            el['name'] = artist['name']
            el['genres'] = artist['genres']
            el['image'] = artist['images'][0]['url']
            el['_id'] = {'$oid': artist['id']}
            el['popularity'] = artist['popularity']
            albums_by_artist = sp.artist_albums(artist['id'])['items']
            el['albums'] = list(map(
                lambda x: {
                    '$ref': 'albums',
                    '$id': {'$oid': x['id']}
                },
                albums_by_artist
            ))
            res_artists.append(el)
            print('Artist: {}/{}...'.format(i+1, amount))
            for j, album in enumerate(albums_by_artist):
                try:
                    alb = {}
                    alb['_id'] = {'$oid': album['id']}
                    alb['artists'] = list(map(
                        lambda x: {
                            '$ref': 'artists',
                            '$id': {'$oid': x['id']}
                        },
                        album['artists']
                    ))
                    alb['name'] = album['name']
                    alb['image'] = album['images'][0]['url'] if len(album['images']) > 0 else ""
                    alb['release_date'] = album['release_date']
                    track_by_albums = sp.album_tracks(album['id'])['items']
                    alb['tracks'] = list(map(
                        lambda x: {
                            '$ref': 'tracks',
                            '$id': {'$oid': x['id']}
                        },
                        track_by_albums
                    ))
                    res_albums.append(alb)
                    print('...Album: {}/{}'.format(j+1, len(albums_by_artist)))
                    for k, track in enumerate(track_by_albums):
                        try:
                            tr = {}
                            tr['_id'] = {'$oid': track['id']}
                            tr['name'] = track['name']
                            tr['duration'] = track['duration_ms']
                            tr['number'] = track['track_number']
                            tr['album'] = {
                                '$ref': 'albums',
                                '$id': {'$oid': album['id']}
                            }
                            tr['content'] = track['href']
                            features = sp.audio_features([track['id']])[0]
                            if features is not None:
                                tr['loudness'] = features['loudness']
                                tr['instrumentalness'] = features['instrumentalness']
                                tr['tempo'] = features['tempo']
                                tr['acousticness'] = features['acousticness']
                                tr['mode'] = features['mode']
                                tr['energy'] = features['energy']
                                tr['speechiness'] = features['speechiness']
                                tr['danceability'] = features['danceability']
                                tr['key'] = features['key']
                                tr['valence'] = features['valence']
                                tr['liveness'] = features['liveness']
                                tr['time_signature'] = features['time_signature']
                            res_tracks.append(tr)
                        except Exception:
                            continue
                except Exception:
                    continue
        except Exception:
            continue


    out_artists = json.dumps(
        res_artists,
        sort_keys=True,
        indent=2
    )
    file = open('artists.json', 'w')
    file.write(out_artists)
    file.close()

    out_albums = json.dumps(
        res_albums,
        sort_keys=True,
        indent=2
    )
    file = open('albums.json', 'w')
    file.write(out_albums)
    file.close()

    out_tracks = json.dumps(
        res_tracks,
        sort_keys=True,
        indent=2
    )
    file = open('tracks.json', 'w')
    file.write(out_tracks)
    file.close()

    try:
        mongod = subprocess.Popen(['mongod'])
        if mongod.poll() != 0:
            print('Error occured while calling mongod')
            sys.exit(1)
        subprocess.call('mongoimport --jsonArray -c artists -d musicbox --mode merge --file artists.json')
        subprocess.call('mongoimport --jsonArray -c albums -d musicbox --mode merge --file albums.json')
        subprocess.call('mongoimport --jsonArray -c tracks -d musicbox --mode merge --file tracks.json')
    except FileNotFoundError:
        print('There is no mongod service started on your machine')
        sys.exit(2)
