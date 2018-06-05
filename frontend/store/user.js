import connector from '../util/connector';

export const state = () => ({
    user: null
});

export const mutations = {
    setUser(state, user) {
        state.user = user;
    }
};

export const actions = {
    async login({ commit }, { username, password }) {
        const result = await connector().login(username, password);
        commit('setUser', { username });
        this.$router.push({
            path: '/profile'
        });
        return result;
    },
    async logout({ commit }) {
        await connector().logout();
        commit('setUser', null);
    },
    async register({ commit, dispatch }, formData) {
        const result = await connector().register(formData);
        commit('setUser', { username: formData.username });
        await dispatch('login', formData);
        return result;
    }
};
