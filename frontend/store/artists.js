import {default as connector, SERVICES} from '../util/connector';

export const actions = {
    getArtists: (context) => connector().get(SERVICES.artists).then(res => context.commit('set', res))
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
