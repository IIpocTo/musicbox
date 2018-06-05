import connector from '@/util/connector';

export const state = () => ({
    sidebar: false,
    backendAvailable: false
});

export const mutations = {
    setSidebar(state, value) {
        state.sidebar = value;
    },
    setBackendAvailable(state, value) {
        state.backendAvailable = value;
    }
};

export const actions = {
    async checkBackend({ commit }) {
        const result = await connector().check();
        commit('setBackendAvailable', result === true);
    }
};
