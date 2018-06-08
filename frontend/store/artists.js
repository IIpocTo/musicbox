import { default as connector, SERVICES } from '../util/connector';

export const actions = {
    async getArtists({ commit }) {
        const result = await connector().get(SERVICES.artists);
        console.log(result);
        commit('set', result.data || []);
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
