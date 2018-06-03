import fetch from 'node-fetch';
import {getCookie, setCookie} from './cooker';

export const SERVER = {
    HOST: process.env.NODE_ENV === 'production' ? 'shit.com' : 'localhost',
    PORT: 9000,
    PROTOCOL: 'http',
    V_API: 'v1'
};

export const SERVICES = {
    albums: 'albums',
    artists: 'artists',
    users: 'users',
    tracks: 'tracks',
    search: 'search'
};

export const TOKENS = {
    XSRF: 'XSRF-TOKEN'
};

// function mocker(service, params) {
//     switch (service) {
//     case SERVICES.artists:
//         return [
//             {
//                 id: 'bau',
//                 name: 'Б.А.У.',
//                 image: require('@/assets/image/bau.jpeg'),
//                 tags: ['Метал', 'Пародия', 'Гроул']
//             },
//             {
//                 id: 'iron',
//                 name: 'Iron Maiden',
//                 image: require('@/assets/image/iron.jpg'),
//                 tags: ['Хэви-Метал', 'Метал', 'Рок']
//             },
//             {
//                 id: 'rise',
//                 name: 'Rise of Tyrant',
//                 artist: {
//                     name: 'Arch Enemy'
//                 },
//                 image: require('@/assets/image/rise.jpg'),
//                 tags: ['Метал', 'Мелодик-Дэт']
//             }
//         ];
//     case SERVICES.search:
//         return {
//             albums: [],
//             tracks: [],
//             artists: [
//                 {
//                     id: 'bau',
//                     name: 'Б.А.У.',
//                     image: require('@/assets/image/bau.jpeg'),
//                     tags: ['Метал', 'Пародия', 'Гроул']
//                 }
//             ]
//         };
//     default: return [];
//     }
// }

export default function () {
    const server = `${SERVER.PROTOCOL}://${SERVER.HOST}:${SERVER.PORT}/${SERVER.V_API}`;
    return {
        post: (route, params = {}) => {
            if (SERVICES[route] === void 0) return void 0;
            return fetch(`${server}/${route}`, {
                method: 'POST',
                ...params,
                headers: {
                    'X-XSRF-TOKEN': getCookie(TOKENS.XSRF),
                    ...(params.headers || {})
                }
            });
        },
        check: () => {
            return fetch(`${server}/healthcheck`)
                .then(() => {
                    return true;
                })
                .catch(() => {
                    return false;
                });
        },
        get: (route, params = {}) => {
            if (SERVICES[route] === void 0) return void 0;
            const q = params.query ? `?${params.query}` : '';
            return fetch(`${server}/${route}${q}`, params || {});
        },
        login: (login, password) => {
            return fetch(`${server}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': getCookie(TOKENS.XSRF)
                },
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
                    refresh_token: refreshToken
                } = res;
                setCookie('access_token', accessToken);
                setCookie('refresh_token', refreshToken);
                return true;
            });
        },
        register: (login, password, email, phone) => {
            return fetch(`${server}/auth/register`, {
                method: 'POST',
                body: {
                    login,
                    password,
                    email,
                    phone
                },
                headers: {
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': getCookie(TOKENS.XSRF)
                }
            }).then(response => {
                return response.status === 201;
            });
        },
        logout: () => {}
    };
}
