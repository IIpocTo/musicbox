import { default as connector, SERVICES } from '../util/connector';

export const actions = {
    async getArtists({ commit }, { page, limit }) {
        const result = await connector().get(SERVICES.artists, { query: `page=${page}&limit=${limit}` });
        const artists = await result.json();
        commit('set', artists || []);
    }
};

export const state = () => ({
    artistList: []
});

export const mutations = {
    set: (state, newState) => {
        state.artistList = newState;
    }
};

export const getters = {
    artists: state => state.artistList
};
