import { default as connector, SERVICES } from '../util/connector';

export const actions = {
    getTracks: (context) => connector().get(SERVICES.tracks).then(res => context.commit('set', res))
};

export const state = () => ({
    trackList: []
});

export const mutations = {
    set: (state, newState) => {
        state.trackList = newState;
    }
};

export const getters = {
    tracks: state => state.trackList
};
