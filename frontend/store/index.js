import connector from '@/util/connector';

export const state = () => ({
    sidebar: false,
    backendAvailable: false,
    cookieNotifier: false,
    dark: false,

    snackbar: {
        visible: false,
        color: 'info',
        text: ''
    }
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
    },

    showSnackbar(state, visible) {
        state.snackbar.visible = visible;
    },
    showError(state, { error = null, text = 'Ошибка!' } = {}) {
        state.snackbar.visible = true;
        state.snackbar.color = 'error';
        let message = text;
        if (error) {
            message += ' ' + (error.message || String(error));
        }
        state.snackbar.text = message;
    },
    showSuccess(state, { text = 'Успешно' } = {}) {
        state.snackbar.visible = true;
        state.snackbar.color = 'success';
        state.snackbar.text = text;
    },

    setDark(state, value) {
        state.dark = value;
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
