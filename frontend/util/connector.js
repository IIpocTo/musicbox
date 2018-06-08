import {deleteCookie, getCookie} from './cooker';

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
    me: 'user/profile/me',
    tracks: 'tracks',
    search: 'search'
};

export const TOKENS = {
    XSRF: 'XSRF-TOKEN',
    AUTHORIZATION: 'Authorization',
    REFRESH: 'Refresh',
    ACCESS: '_sessiondata'
};

// if (process.browser) {
//    require('../util/sPlayer');
// }

const server = `${SERVER.PROTOCOL}://${SERVER.HOST}:${SERVER.PORT}/${SERVER.V_API}`;

function setTokens(response) {
    for (let pair of response.headers.entries()) {
        if (pair[0] === 'set-authorization') {
            global.localStorage.setItem(TOKENS.AUTHORIZATION, pair[1]);
        } if (pair[0] === 'set-refresh-token') {
            global.localStorage.setItem(TOKENS.REFRESH, pair[1]);
        }
    }
}

function post(route, params = {}) {
    if (SERVICES[route] === void 0) return void 0;
    return fetch(`${server}/${route}`, {
        method: 'POST',
        ...params,
        headers: {
            'X-XSRF-TOKEN': getCookie(TOKENS.XSRF),
            ...(params.headers || {}),
            ...(params.auth ? {
                'Authorization': global.localStorage.getItem(TOKENS.AUTHORIZATION)
            } : {})
        }
    }).then(res => refresh(res, post, route, params));
}

function check() {
    deleteCookie(TOKENS.XSRF);
    return fetch(`${server}/healthcheck`, {
        credentials: 'include'
    })
        .then(() => {
            return true;
        })
        .catch(() => {
            return false;
        });
}

function get(route, params = {}) {
    // if (SERVICES[route] === void 0) return void 0;
    if (route === void 0) return void 0;
    const q = params.query ? `?${params.query}` : '';
    return fetch(`${server}/${route}${q}`, {
        ...params,
        headers: {
            'X-XSRF-TOKEN': getCookie(TOKENS.XSRF),
            ...(params.auth ? {
                'Authorization': global.localStorage.getItem(TOKENS.AUTHORIZATION)
            } : {})
        }
    }).then(res => refresh(res, get, route, params));
}

function login(username, password) {
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
        setTokens(response);
        if (response.status === 200) return true;
        return false;
    });
}

function register({ username, password, email, phone }) {
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
}

function makeRefresh() {
    return fetch(`${server}/auth/refresh`, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Refresh-Token': global.localStorage.getItem(TOKENS.REFRESH)
        }
    });
}

function refresh(res, func, ...params) {
    console.log(res.status);
    if (res.status === 403) {
        return makeRefresh().then(response => {
            if (response.status === 403) return false;
            else {
                setTokens(response);
                return func(...params);
            }
        });
    } else {
        return res;
    }
}

function logout() {
    return fetch(`${server}/auth/logout`, {
        method: 'POST',
        credentials: 'include',
        headers: {
            'X-XSRF-TOKEN': getCookie(TOKENS.XSRF),
            'Authorization': global.localStorage.getItem(TOKENS.AUTHORIZATION)
        }
    }).then(response => {
        if (response.status === 200) {
            return true;
        } else {
            return refresh(response, logout);
        }
    });
}

export default function () {
    return {
        post,
        check,
        get,
        login,
        register,
        logout
    };
}
