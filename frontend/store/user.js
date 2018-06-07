import connector from '../util/connector';

export const state = () => ({
    user: null,
    authorized: false
});

export const mutations = {
    setUser(state, user) {
        state.user = user;
    },
    auth(state, isAuthorized) {
        state.authorized = isAuthorized;
    }
};

export const getters = {
    user: state => state.user,
    authorized: state => state.authorized
};


export const actions = {
    async login({ commit }, { username, password }) {
        const result = await connector().login(username, password);
        if (result) {
            commit('auth', true);
            this.$router.push({
                path: '/profile'
            });
        } else {
            throw new Error('Invalid credentials');
        }
        return result;
    },
    keepLoggedIn({ commit }) {
        commit('auth', true);
    },
    async logout({ commit }) {
        const result = await connector().logout();
        if (result) {
            commit('auth', false);
            this.$router.push({
                path: '/'
            });
        } else {
            console.log('ooops');
        }
        return result;
    },
    async register({ commit, dispatch }, formData) {
        const result = await connector().register(formData);
        if (result) {
            const result = await dispatch('login', formData);
            if (result) {
                commit('auth', true);
            }
        }
        return result;
    },
    getMe({ commit, dispatch }) {
        console.log('asda');
        return connector().get('user/profile/me', { auth: true }).then(result => {
            if (result) {
                commit('setUser', result);
            }
            return result;
        });
    }
};
