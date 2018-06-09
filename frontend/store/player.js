export const state = () => ({
    visible: true,

    playlist: null,
    position: 0
});

export const getters = {
    currentSong(state) {
        if (!state.playlist) return null;
        return state.playlist.content[state.position] || null;
    }
};

export const mutations = {
    setVisibility(state, value) {
        state.visible = value;
    },

    play(state, { playlist, position = 0 }) {
        state.playlist = playlist;
        state.position = position;
    },

    setPosition(state, value) {
        state.position = value;
    }
};
