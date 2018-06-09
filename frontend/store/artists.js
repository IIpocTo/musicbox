import { default as connector, SERVICES } from '../util/connector';

export const actions = {
    async getArtists({ commit }, { page, limit, append }) {
        const result = await connector().get(SERVICES.artists, { query: `page=${page}&limit=${limit}` });
        const artists = await result.json();
        if (append) {
            commit('add', artists || []);
        } else {
            commit('set', artists || []);
        }
        return artists.length;
    }
};

export const state = () => ({
    artistList: []
});

export const mutations = {
    set: (state, newState) => {
        state.artistList = newState;
    },
    add(state, moreArtists) {
        state.artistList = state.artistList.concat(moreArtists);
    }
};

export const getters = {
    artists: state => state.artistList
};
