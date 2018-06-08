const isProtectedRoute = route => {
    if (route.path.endsWith('profile')) return true;
    return false;
};

export default function (ctx) {
    const { req, redirect, route, store } = ctx;
    if (process.server && !req) return;
    const isLoginPage = route.path.indexOf('login') !== -1;
    const isAuthenticated = !!store.state.user;

    if (isLoginPage && isAuthenticated) {
        return redirect({
            path: '/'
        });
    }
    if (isProtectedRoute(route) && !isAuthenticated) {
        return redirect({
            path: '/login',
            query: { next: route.path }
        });
    }
};
