import fetch from 'node-fetch';
import { setCookie } from './cooker';

export const SERVER = {
    HOST: process.env.NODE_ENV === 'production' ? 'shit.com' : 'localhost',
    PORT: 9000,
    PROTOCOL: 'http',
    V_API: 'api/v1'
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
    const server = `${SERVER.PROTOCOL}://${SERVER.HOST}:${SERVER.PORT}/${SERVER.V_API}`;
    return {
        post: (route, params) => {
            if (SERVICES[route] === void 0) return void 0;
            if (mocked) return Promise.resolve(true);
            return fetch(`${server}/${route}`, {
                method: 'POST',
                ...params
            });
        },
        get: (route, params) => {
            if (SERVICES[route] === void 0) return void 0;
            if (mocked) return Promise.resolve(mocker(route, params));
            return fetch(`${server}/${route}`, {
                method: 'GET',
                ...(params || {})
            });
        },
        login: (login, password) => {
            if (mocked) return Promise.resolve(false);
            return fetch(`${server}/auth/login`, {
                method: 'POST',
                body: {
                    login,
                    password
                }
            }).then(response => {
                if (response.status !== 201) return false;
                else return response.json();
            }).then(res => {
                const {
                    // eslint-disable-nex-line camelcase
                    access_token: accessToken,
                    // eslint-disable-nex-line camelcase
                    refresh_token: refreshToken,
                    // eslint-disable-nex-line camelcase
                    csrf_token: csrfToken
                } = res;
                setCookie('access_token', accessToken);
                setCookie('refresh_token', refreshToken);
                setCookie('csrf_token', csrfToken);
                return true;
            });
        },
        register: (login, password, email, phone) => {
            if (mocked) return Promise.resolve(false);
            return fetch(`${server}/auth/register`, {
                method: 'POST',
                body: {
                    login,
                    password,
                    email,
                    phone
                }
            }).then(response => {
                return response.status === 201;
            });
        },
        logout: () => {}
    };
}
