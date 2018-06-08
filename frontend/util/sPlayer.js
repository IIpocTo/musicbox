// import * as Spotify from 'spotify-web-api-js';
/* eslint-disable camelcase */
if (process.browser) {
    window.onSpotifyWebPlaybackSDKReady = () => {
        // const s = new Spotify();
        // console.log(s.getAccessToken());
        // s.setAccessToken('');
        // s.play({ uris: ['spotify:track:4iV5W9uYEdYUVa79Axb7Rh'] });
        // fetch('https://api.spotify.com/v1/me/player/play', {
        //     method: 'PUT',
        //     headers: {
        //         'Content-Type': 'application/json',
        //         'Authorization': 'Bearer '
        //     },
        //     body: JSON.stringify({ uris: [
        //         'spotify:track:4iV5W9uYEdYUVa79Axb7Rh', 'spotify:track:1301WleyT98MSxVHPZCA6M']
        //     })
        // });

        const token = '';
        const player = new global.Spotify.Player({
            name: 'Web Playback SDK Quick Start Player',
            getOAuthToken: cb => { cb(token); }
        });

        // Error handling
        player.addListener('initialization_error', ({ message }) => { console.error(message); });
        player.addListener('authentication_error', ({ message }) => { console.error(message); });
        player.addListener('account_error', ({ message }) => { console.error(message); });
        player.addListener('playback_error', ({ message }) => { console.error(message); });

        // Playback status updates
        player.addListener('player_state_changed', state => { console.log(state); });

        // Ready
        player.addListener('ready', ({ device_id }) => {
            console.log('Ready with Device ID', device_id);
        });

        // Not Ready
        player.addListener('not_ready', ({ device_id }) => {
            console.log('Device ID has gone offline', device_id);
        });

        // Connect to the player!
        player.connect();

        const play = (
            {
                spotify_uri,
                playerInstance: {
                    _options: {
                        getOAuthToken,
                        id
                    }
                }
            }) => {
            getOAuthToken(access_token => {
                fetch(`https://api.spotify.com/v1/me/player/play?device_id=${id}`, {
                    method: 'PUT',
                    body: JSON.stringify({ uris: [spotify_uri] }),
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${access_token}`
                    }
                });
            });
        };

        play({
            playerInstance: player,
            spotify_uri: 'spotify:track:7xGfFoTpQ2E7fRF5lN10tr'
        });
    };
}

/* eslint-enable */

export default function () {
    // const s = new Spotify();
    // console.log(s);
    return {
    };
}
