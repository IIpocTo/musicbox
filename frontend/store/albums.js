import { default as connector, SERVICES } from '../util/connector';

export const actions = {
    async getAlbums({ commit }) {
        const result = await connector().get(SERVICES.albums);
        commit('set', result.data || []);
    }
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
