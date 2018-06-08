import { default as connector, SERVICES } from '../util/connector';

export const actions = {
    getArtist: (context) => connector().get(SERVICES.artist).then(res => context.commit('set', res))
};

export const state = () => ({
    artist: {}
});

export const mutations = {
    set: (state, newState) => {
        state.artist = newState;
    }
};

export const getters = {
    artist: state => state.artist
};
