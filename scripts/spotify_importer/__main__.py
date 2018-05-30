import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import json
import sys

if __name__ == '__main__':
    args = sys.argv
    if len(args) != 5:
        sys.exit(1)
    if args.count('--client') == 0 | args.count('--secret') == 0:
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
        limit=20,
        type='artist'
    )['artists']
    res_artists = []
    res_albums = []
    res_tracks = []

    for i, artist in enumerate(artists['items']):
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
        for j, album in enumerate(albums_by_artist):
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
            for k, track in enumerate(track_by_albums):
                tr = {}
                tr['name'] = track['name']
                tr['duration'] = track['duration_ms']
                tr['number'] = track['track_number']
                tr['album'] = {
                    '$ref': 'albums',
                    '$id': {'$oid': album['id']}
                }
                tr['content'] = track['href']
                res_tracks.append(tr)
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
