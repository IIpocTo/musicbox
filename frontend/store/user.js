import connector, {SERVICES} from '../util/connector';

export const state = () => ({
    user: null
});

export const mutations = {
    setUser(state, user) {
        state.user = user;
    }
};

export const getters = {
    user: state => state.user,
    authorized: state => !!state.user
};


export const actions = {
    async login({ commit, dispatch }, { username, password }) {
        const result = await connector().login(username, password);
        if (result) {
            await dispatch('getMe');

            this.$router.push({
                path: '/profile'
            });
        } else {
            throw new Error('Invalid credentials');
        }
        return result;
    },
    keepLoggedIn({ commit }) {
        // commit('auth', true);
    },
    async logout({ commit }) {
        const result = await connector().logout();
        if (result) {
            commit('setUser', null);
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
            await dispatch('login', formData);
        }
        return result;
    },
    getMe({ commit, dispatch }) {
        return connector().get(SERVICES.me, { auth: true }).then(result => {
            return result.json();
        }).then(user => {
            commit('setUser', user);
        });
    }
};
