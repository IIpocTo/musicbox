if (process.browser) {
    window.onSpotifyWebPlaybackSDKReady = () => {
        const url = new URL('https://accounts.spotify.com/authorize');
        const params = {
            client_id: '10d670bf7f6a46daa134f1c0d197254e',
            response_type: 'token',
            redirect_uri: 'http://localhost:3000/callback',
            state: '34fFs29kd09'
        };
        url.search = new URLSearchParams(params);
        fetch(url).then(res => console.log(res));
        // const clientId = '86e0507752d24428bbe832e27044c281';
        // const secretId = 'a6da5cd275d84571899c708c53bd10b6';
        // url.search = new URLSearchParams(params);
        // const clientStr = `${clientId}:${secretId}`;
        // fetch('https://accounts.spotify.com/authorize')
        // fetch('https://accounts.spotify.com/api/token', {
        //    headers: {
        //        'Authorization': `Basic ${btoa(clientStr)}`
        //    },
        //    method: 'POST',
        //    body: JSON.stringify({
        //        gran_type: 'client_credentials'
        //    })
        // })
        //    .then(res => console.log(res.status) || res.json())
        //    .then(res => console.log(res));
        const token = 'Z2VvcmdlY2FycG93QGdtYWlsLmNvbTpBR0RwYU4wNWJybkRyMTAvZFNHdGZDYklzVVBobmRZdw';
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
        // eslint-disable-next-line camelcase
        player.addListener('ready', ({ device_id }) => {
            console.log('Ready with Device ID', device_id);
        });

        // Not Ready
        // eslint-disable-next-line camelcase
        player.addListener('not_ready', ({ device_id }) => {
            console.log('Device ID has gone offline', device_id);
        });

        // Connect to the player!
        player.connect();
    };
}

export default function () {
    return {
    };
}
