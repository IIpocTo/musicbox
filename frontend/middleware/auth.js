export default function (ctx) {
    const { redirect, route, store } = ctx;
    const isLoginPage = route.path === '/login';
    const isAuthenticated = store.getters['user/authorized'];

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

    console.log('FUCK', isAuthenticated, isLoginPage);
};
