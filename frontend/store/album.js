import { default as connector, SERVICES } from '../util/connector';

export const actions = {
    getAlbum: (context) => connector().get(SERVICES.album).then(res => context.commit('set', res))
};

export const state = () => ({
    album: {}
});

export const mutations = {
    set: (state, newState) => {
        state.album = newState;
    }
};

export const getters = {
    album: state => state.album
};
