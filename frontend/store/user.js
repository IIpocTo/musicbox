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
    async getMe({ commit, dispatch }) {
        const result = await connector().get(SERVICES.me, { auth: true });
        const user = await result.json();
        commit('setUser', user);
    },

    async changePassword({ commit }, { password }) {
        // TODO
    },
    async updatePersonalData({ commit }, updatedData) {
        // TODO
    },
    async removeAccount({ commit }, { password }) {
        // TODO
        throw new Error('Вы никогда не сможете удалить свой аккаунт, ахахахаха');
    }
};
