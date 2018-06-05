import connector from '@/util/connector';

export const state = () => ({
    sidebar: false,
    backendAvailable: false,
    cookieNotifier: false
});

export const mutations = {
    setSidebar(state, value) {
        state.sidebar = value;
    },
    setBackendAvailable(state, value) {
        state.backendAvailable = value;
    },
    setCookieNotifier(state, value) {
        state.cookieNotifier = value;
    }
};

export const actions = {
    async checkBackend({ commit }) {
        const result = await connector().check();
        commit('setBackendAvailable', result === true);
    },

    async hideCookieNotifier({ commit }) {
        // will always be run in browser
        window.localStorage.setItem('cookieNotifier', JSON.stringify(false));
        commit('setCookieNotifier', false);
    },
    async renderCookieNotifier({ commit }) {
        const cookieNotifier = JSON.parse(window.localStorage.getItem('cookieNotifier') || 'true');
        commit('setCookieNotifier', cookieNotifier);
    }
};
