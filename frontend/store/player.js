export const state = () => ({
    visible: true,

    playlist: {
        title: 'True Beauty Is So Painful — Oomph!',
        id: '',
        content: [
            // {
            //     title: 'True Beauty Is So Painful',
            //     url: 'todo://music.mp3',
            //     id: '',
            //     artist: {
            //         title: 'Oomph!',
            //         id: ''
            //     },
            //     album: {
            //         title: 'True Beauty Is So Painful',
            //         id: ''
            //     }
            // }
        ]
    },

    repeatPlaylist: true,
    repeatSong: false,
    shuffle: false
});

export const getters = {
    hasNextSong(state) {
        return state.repeatPlaylist || state.position !== state.playlist.content.length - 1;
    },
    hasPrevSong(state) {
        return state.repeatPlaylist || state.position !== 0;
    },
    currentSong(state) {
        return state.playlist.content[state.position] || null;
    }
};

export const mutations = {
    setVisibility(state, value) {
        state.visible = value;
    }
};
