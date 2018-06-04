import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import sys
from pymongo import MongoClient
from bson import ObjectId, DBRef


def id(id):
    return ObjectId(id[:12].encode('utf-8'))


ids = [
    '58lV9VcRSjABbAbfWS6skp', # Bon Jovi
    '27T030eWyCQRmDyuvr1kxY', # Scorpions
    '6SLAMfhOi7UJI0fMztaK0m', # Rainbow
    '568ZhdwyaiCyOGJRtNYhWf', # Deep Purple
    '7oPftvlwr6VrsViSDV7fJY', # Green Day
    '5LfGQac0EIXyAN8aUwmNAQ', # The Offspring
    '5Pwc4xIPtQLFEnJriah9YJ', # OneRepublic
    '5SHxzwjek1Pipl1Yk11UHv', # Tom Grennan
    '4f9iBmdUOhQWeP7dcAn1pf', # Rag'n'Bone Man
    '5ggxe6RGD8zHfaCbwTdtw5', # Алиса
    '3mF9V7H7yo9AwMXHt08Q9f', # Б.Т.Р.
    '0ICn3Cbc4mMeLmTvwnqXYu', # Ария
    '68YlHHmtFXbIFOPp8pMutS', # Щурците
    '3FUY2gzHeIiaesXtOAdB7A', # Train
    '0zOcE3mg9nS6l3yxt1Y0bK', # The Fray
    '4DToQR3aKrHQSSRzSz8Nzt', # The Hives
    '6cmp7ut7okJAgJOSaMAVf3', # Machinae Supremacy
    '3WrFJ7ztbogyGnTHbHJFl2', # The Beatles
    '293zczrfYafIItmnmM3coR', # Chuck Berry
    '6deZN1bslXzeGvOLaLMOIF' # Nickelback
]


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

    artists = sp.artists(ids)
    res_artists = []
    res_albums = []
    res_tracks = []

    for i, artist in enumerate(artists['artists']):
        try:
            el = {}
            el['name'] = artist['name']
            el['genres'] = artist['genres']
            el['image'] = artist['images'][0]['url']
            el['_id'] = id(artist['id'])
            el['popularity'] = artist['popularity']
            albums_by_artist = sp.artist_albums(artist['id'])['items']
            el['albums'] = list(map(
                lambda x: DBRef(collection='albums', id=id(x['id'])),
                albums_by_artist
            ))
            res_artists.append(el)
            print('Artist: {}/{}...'.format(i+1, amount))
            for j, album in enumerate(albums_by_artist):
                try:
                    alb = {}
                    alb['_id'] = id(album['id'])
                    alb['artists'] = list(map(
                        lambda x: DBRef(collection='artists', id=id(x['id'])),
                        album['artists']
                    ))
                    alb['name'] = album['name']
                    alb['image'] = album['images'][0]['url'] if len(album['images']) > 0 else ""
                    alb['release_date'] = album['release_date']
                    track_by_albums = sp.album_tracks(album['id'])['items']
                    alb['tracks'] = list(map(
                        lambda x: DBRef(collection='tracks', id=id(x['id'])),
                        track_by_albums
                    ))
                    res_albums.append(alb)
                    print('...Album: {}/{}'.format(j+1, len(albums_by_artist)))
                    for k, track in enumerate(track_by_albums):
                        try:
                            tr = {}
                            tr['_id'] = id(track['id'])
                            tr['name'] = track['name']
                            tr['duration'] = track['duration_ms']
                            tr['number'] = track['track_number']
                            tr['album'] = DBRef(collection='albums', id=id(album['id']))
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
                        except Exception as e:
                            print(e)
                            continue
                except Exception as e:
                    print(e)
                    continue
        except Exception as e:
            print(e)
            continue

    try:
        client = MongoClient()
        db = client.musicbox
        artists = db.artists
        albums = db.albums
        tracks = db.tracks
	artists.remove()
	albums.remove()
	tracks.remove()
        artists.insert_many(res_artists)
        albums.insert_many(res_albums)
        tracks.insert_many(res_tracks)
        collection = db.collection_names(include_system_collections=False)
        if len(collection) != 3:
            print(collection)
            sys.exit(3)
    except Exception as e:
        print(e)
        sys.exit(2)
