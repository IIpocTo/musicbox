if (process.browser) {
    window.onSpotifyWebPlaybackSDKReady = () => {
        // fetch('https://accounts.spotify.com/authorize/?client_id=5fe01282e44241328a84e7c5cc169165&response_type=code&redirect_uri=https%3A%2F%2Fexample.com%2Fcallback&scope=user-read-private%20user-read-email&state=34fFs29kd09')
        console.log('asdasd', global.Spotify);
    };
}

export default function () {
    return {
    };
}