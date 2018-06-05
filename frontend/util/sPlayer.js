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
        fetch(url, {
        })
            .then(res => console.log(res) || res.json())
            .then(res => console.log(res))
            .catch(err => console.log(err));
        const token = 'BQBdWCzfGgdNID-YUTBVRzSpaFUeisRvISjq15dfanOSgcrm3pKYA4DRn_oyalqUBIo0je5iYDb1rwCMk-zVgErme5wkPH3CDRwAmdgspDKuwHswhmOgZ-dGeUN4hK86kFgFewBJFgZmgDsoRds_WL_Qrmw';
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
