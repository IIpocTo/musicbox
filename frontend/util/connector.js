import fetch from 'node-fetch';

export const SERVER = {
    HOST: process.env.NODE_ENV === 'production' ? 'shit.com' : 'localhost',
    PORT: 9000,
    PROTOCOL: 'http'
};

export const SERVICES = {
    albums: 'albums',
    artists: 'artists',
    users: 'users',
    tracks: 'tracks'
};

let mocked = void 0;

function init() {
    fetch(`${SERVER.PROTOCOL}://${SERVER.HOST}:${SERVER.PORT}`)
        .then(() => {
            mocked = false;
        })
        .catch(() => {
            mocked = true;
        });
}

init();

function mocker(service, params) {
    switch (service) {
    case SERVICES.artists:
        return [
            {
                id: 'bau',
                name: 'Б.А.У.',
                image: require('@/assets/image/bau.jpeg'),
                tags: ['Метал', 'Пародия', 'Гроул']
            },
            {
                id: 'iron',
                name: 'Iron Maiden',
                image: require('@/assets/image/iron.jpg'),
                tags: ['Хэви-Метал', 'Метал', 'Рок']
            },
            {
                id: 'rise',
                name: 'Rise of Tyrant',
                artist: {
                    name: 'Arch Enemy'
                },
                image: require('@/assets/image/rise.jpg'),
                tags: ['Метал', 'Мелодик-Дэт']
            }
        ];
    default: return [];
    }
}

export default function () {
    return {
        post: (route, params) => {
            if (SERVICES[route] === void 0) return void 0;
            if (mocked) return Promise.resolve(true);
            return fetch(`${SERVER.PROTOCOL}://${SERVER.HOST}:${SERVER.PORT}/${route}`, {
                method: 'POST',
                ...params
            });
        },
        get: (route, params) => {
            if (SERVICES[route] === void 0) return void 0;
            if (mocked) return Promise.resolve(mocker(route, params));
            return fetch(`${SERVER.PROTOCOL}://${SERVER.HOST}:${SERVER.PORT}/${route}`, {
                method: 'GET',
                ...(params || {})
            });
        }
    };
}
