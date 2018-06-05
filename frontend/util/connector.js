import {getCookie} from './cooker';

export const SERVER = {
    HOST: process.env.NODE_ENV === 'production' ? 'shit.com' : '127.0.0.1',
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
    XSRF: 'XSRF-TOKEN',
    ACCESS: '_sessiondata'
};

if (process.browser) {
    require('../util/sPlayer');
}

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
            return fetch(`${server}/healthcheck`, {
                credentials: 'include'
            })
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
        login: (username, password) => {
            return fetch(`${server}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': getCookie(TOKENS.XSRF)
                },
                credentials: 'include',
                body: JSON.stringify({
                    username,
                    password
                })
            }).then(response => {
                if (response.status === 200) return true;
                return false;
            });
        },
        register: (username, password, email, phone) => {
            return fetch(`${server}/auth/register`, {
                method: 'POST',
                credentials: 'include',
                body: JSON.stringify({
                    username,
                    password,
                    email,
                    phone
                }),
                headers: {
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': getCookie(TOKENS.XSRF)
                }
            }).then(response => {
                return response.status === 201;
            });
        },
        logout: () => {
            return fetch(`${server}/auth/logout`, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'X-XSRF-TOKEN': getCookie(TOKENS.XSRF)
                }
            }).then(response => {
                return response.status === 201;
            });
        }
    };
}
