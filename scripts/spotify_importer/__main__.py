import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import json

if __name__ == '__main__':
    client_credentials_manager = SpotifyClientCredentials(
        '10d670bf7f6a46daa134f1c0d197254e',
        'cfa1cd4cb76a4dc9be55d64f5cc0f898'
    )
    sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)

    artists = sp.search(q='band', limit=50, type='artist')['artists']
    res = []
    for i, artist in enumerate(artists['items']):
        el = {}
        el['name'] = artist['name']
        el['genres'] = artist['genres']
        el['image'] = artist['images'][0]['url']
        el['_id'] = artist['id']
        el['popularity'] = artist['popularity']
        res.append(el)
    js = json.dumps(res, sort_keys=True, indent=2)
    file = open('result.json', 'w')
    file.write(js)
    file.close()