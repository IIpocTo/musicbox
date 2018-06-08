export default async function (ctx) {
    const { redirect, route, store } = ctx;
    const isLoginPage = route.path === '/login';
    let isAuthenticated = store.getters['user/authorized'];

    if (!isAuthenticated) {
        await store.dispatch('user/getMe');
        isAuthenticated = store.getters['user/authorized'];
    }

    if (isLoginPage && isAuthenticated) {
        return redirect({
            path: '/'
        });
    }
    if (!isAuthenticated) {
        return redirect({
            path: '/login',
            query: { next: route.path }
        });
    }
};
