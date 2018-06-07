import spotipy
import sys
from bson import ObjectId, DBRef
from datetime import datetime
from multiprocessing.pool import ThreadPool
from pymongo import MongoClient
from spotipy.oauth2 import SpotifyClientCredentials


def id(id):
    return ObjectId(id[:12].encode('utf-8'))


ids = [
    '58lV9VcRSjABbAbfWS6skp',  # Bon Jovi
    '27T030eWyCQRmDyuvr1kxY',  # Scorpions
    '6SLAMfhOi7UJI0fMztaK0m',  # Rainbow
    '568ZhdwyaiCyOGJRtNYhWf',  # Deep Purple
    '7oPftvlwr6VrsViSDV7fJY',  # Green Day
    '5LfGQac0EIXyAN8aUwmNAQ',  # The Offspring
    '5Pwc4xIPtQLFEnJriah9YJ',  # OneRepublic
    '5SHxzwjek1Pipl1Yk11UHv',  # Tom Grennan
    '4f9iBmdUOhQWeP7dcAn1pf',  # Rag'n'Bone Man
    '5ggxe6RGD8zHfaCbwTdtw5',  # Алиса
    '3mF9V7H7yo9AwMXHt08Q9f',  # Б.Т.Р.
    '0ICn3Cbc4mMeLmTvwnqXYu',  # Ария
    '68YlHHmtFXbIFOPp8pMutS',  # Щурците
    '3FUY2gzHeIiaesXtOAdB7A',  # Train
    '0zOcE3mg9nS6l3yxt1Y0bK',  # The Fray
    '4DToQR3aKrHQSSRzSz8Nzt',  # The Hives
    '6cmp7ut7okJAgJOSaMAVf3',  # Machinae Supremacy
    '3WrFJ7ztbogyGnTHbHJFl2',  # The Beatles
    '293zczrfYafIItmnmM3coR',  # Chuck Berry
    '6deZN1bslXzeGvOLaLMOIF',  # Nickelback
    '4dpARuHxo51G3z768sgnrY',  # Adele 21...
    '2mZITUvfEwrKlksoGpHTsM',  # Oomph!
    '64LCTpIu9Iji2EPaxxPpxF',  # Joe Dassin
    '76ptJV8617638xrpeoUtzl',  # The Rasmus
    '0R9s1t65CdGRCJx5OaaPE2',  # Нуки
    '3b8QkneNDz4JHKKKlLgYZg',  # Florida Georgia Line
    '4oUHIQIBe0LHzYfvXNW4QM',  # Morgan Wallen
    '1vCWHaC5f2uS3yhpwWbIA6',  # Avicii
    '5pL8Q5xbDLEDE6JvYMXX9u',  # Nu:logic
    '1wzBqAvtFexgKHjt7i3ena',  # Fred & V Graphics
    '5TgQ66WuWkoQ2xYxaSTnVP',  # Netsky
    '2xiIXseIJcq3nG7C8fHeBj',  # Three Days Grace
    '0RqtSIYZmd4fiBKVFqyIqD',  # Thirty Seconds to Mars
    '3EhbVgyfGd7HkpsagwL9GS',  # Alice Cooper
    '7Ey4PD4MYsKc5I2dolUwbH',  # Aerosmith
    '2eLrVxwm6t4QeaORq5w8VW',  # Григорий Лепс
    '06HL4z0CvFAxyc27GXpf02',  # Taylor Swift
    '53XhwfbYqKCa1cC15pYq2q',  # Imagine Dragons
    '3w4VAlllkAWI6m0AV0Gn6a',  # Hurts
    '4UXqAaa6dQYAk18Lv7PEgX'  # Fall Out Boy
]

number = 0


def collect_artists(artist):
    try:
        el = {}
        el['name'] = artist['name']
        el['genres'] = artist['genres']
        el['image'] = artist['images'][0]['url']
        el['_id'] = id(artist['id'])
        el['popularity'] = artist['popularity']
        albums_by_artist = sp.artist_albums(artist['id'], limit=50)['items']
        el['albums'] = list(map(
            lambda x: DBRef(collection='albums',
                            id=id(x['id'])),
            albums_by_artist
        ))
        global res_albums
        global res_tracks
        global number
        number = number + 1
        print('Artist: {}/{}...'.format(number, len(ids)))
        for j, album in enumerate(albums_by_artist):
            try:
                alb = dict()
                alb['_id'] = id(album['id'])
                alb['artists'] = list(map(
                    lambda x: DBRef(collection='artists',
                                    id=id(x['id'])),
                    album['artists']
                ))
                alb['type'] = album['album_type']
                alb['name'] = album['name']
                if album['images'] is not None:
                    alb['image'] = album['images'][0]['url'] if len(album['images']) > 0 else ""
                else:
                    alb['image'] = ""
                if album['release_date_precision'] == 'day':
                    alb['releaseDate'] = datetime.strptime(album['release_date'], '%Y-%m-%d')
                else:
                    if album['release_date_precision'] == 'year':
                        alb['releaseDate'] = datetime.strptime(album['release_date'], '%Y')
                    else:
                        alb['releaseDate'] = None
                track_by_albums = sp.album_tracks(album['id'])['items']
                alb['tracks'] = list(map(
                    lambda x: DBRef(collection='tracks',
                                    id=id(x['id'])),
                    track_by_albums
                ))
                res_albums.append(alb)
                print('...Album: {}/{} from artist {}'.format(
                    j + 1, len(albums_by_artist),
                    el['name']))
                for k, track in enumerate(track_by_albums):
                    try:
                        tr = dict()
                        tr['_id'] = id(track['id'])
                        tr['name'] = track['name']
                        tr['duration'] = track[
                            'duration_ms']
                        tr['number'] = track['track_number']
                        tr['album'] = DBRef(
                            collection='albums',
                            id=id(album['id']))
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
                            tr['timeSignature'] = features['time_signature']
                        res_tracks.append(tr)
                    except Exception:
                        continue
            except Exception:
                continue
        return el;
    except Exception:
        pass


if __name__ == '__main__':
    args = sys.argv
    if len(args) != 5 and len(args) != 6:
        print('Invalid argument number')
        sys.exit(1)
    if args.count('--client') == 0 | args.count('--secret') == 0:
        print('Not enough arguments')
        sys.exit(1)
    client = args[args.index('--client') + 1]
    secret = args[args.index('--secret') + 1]
    rebuild = False
    if args.count('--rebuild') != 0:
        rebuild = True
    client_credentials_manager = SpotifyClientCredentials(
        client,
        secret
    )
    sp = spotipy.Spotify(
        client_credentials_manager=client_credentials_manager
    )

    artists = sp.artists(ids)['artists']
    res_artists = []
    res_albums = []
    res_tracks = []
    thread_pool = ThreadPool()
    res_artists = thread_pool.map(collect_artists, artists)
    try:
        client = MongoClient()
        db = client.musicbox
        artists = db.artists
        albums = db.albums
        tracks = db.tracks
        if rebuild:
            artists.remove()
            albums.remove()
            tracks.remove()
            artists.insert_many(res_artists)
            albums.insert_many(res_albums)
            tracks.insert_many(res_tracks)
        else:
            for artist in res_artists:
                artist_without_id = dict(artist)
                del artist_without_id['_id']
                got_artist = artists.find(
                    {'_id': artist['_id']}).limit(1)
                if got_artist.count() != 0:
                    artists.update_one({'_id': artist['_id']}, {
                        '$set': artist_without_id})
                else:
                    artists.insert_one(artist)
            for album in res_albums:
                album_without_id = dict(album)
                del album_without_id['_id']
                got_album = albums.find(
                    {'_id': album['_id']}).limit(1)
                if got_album.count() != 0:
                    albums.update_one({'_id': album['_id']}, {
                        '$set': album_without_id})
                else:
                    albums.insert_one(album)
            for track in res_tracks:
                track_without_id = dict(track)
                del track_without_id['_id']
                got_track = tracks.find(
                    {'_id': track['_id']}).limit(1)
                if got_track.count() != 0:
                    tracks.update_one({'_id': track['_id']}, {
                        '$set': track_without_id})
                else:
                    tracks.insert_one(track)
                collection = db.collection_names(
                    include_system_collections=False)
                if len(collection) != 3:
                    print(collection)
                    sys.exit(3)
    except Exception as e:
        print(e)
        sys.exit(2)
