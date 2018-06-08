import { default as connector, SERVICES } from '../util/connector';

export const actions = {
    getAlbums: (context) => connector().get(SERVICES.albums).then(res => context.commit('set', res))
};

export const state = () => ({
    albumList: []
});

export const mutations = {
    set: (state, newState) => {
        state.albumList = newState;
    }
};

export const getters = {
    albums: state => state.albumList
};
