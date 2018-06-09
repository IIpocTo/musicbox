import { default as connector, SERVICES } from '../util/connector';

function getById(arr, id, idName = 'id') {
    for (let i = 0; i < arr.length; i++) {
        if (arr[i][idName] === id) return [arr[i], i];
    }
    return [null, -1];
}

const hash = {
    artists: { single: 'artist', qId: 'id' },
    albums: { single: 'album', qId: 'id' },
    tracks: { single: 'track', qId: 'tid' }
};

export const state = () => ({
    artists: [],
    albums: [],
    tracks: []
});

export const mutations = {
    mergeNew(state, { name, entities, idName = 'id' }) {
        const collection = state[name];
        const existing = collection.map(e => e[idName]);
        for (let i = 0; i < entities.length; i++) {
            const e = entities[i];
            if (existing.includes(e[idName])) {
                continue;
            } else {
                collection.push(e);
            }
        }
    }
};

export const actions = {
    async list({ commit, state }, { name, page, limit }) {
        const result = await connector().get(SERVICES[name], {
            query: `page=${page}&limit=${limit}`
        });
        const data = await result.json();
        // const oldLen = state[name].length;
        commit('mergeNew', { name, entities: data });
        // return state[name].length - oldLen;
        return data.length;
    },

    async getById({ commit, state }, { name, id }) {
        const [cached] = getById(state[name], id);
        if (cached) return cached;
        const result = await connector().get(
            SERVICES[hash[name].single],
            {
                query: `${hash[name].qId}=${id}`
            }
        );
        if (result) {
            const data = await result.json();
            commit('mergeNew', { name, entities: [data] });
            return data;
        } else {
            throw new Error('Fetch error for ' + name + ':' + id);
        }
    }
};
