import {init} from '../util/connector';

export default async function ({ app }) {
    if (!app.mixins) {
        app.mixins = [];
    }
    app.mixins.push({
        beforeCreate() {
            init();
            // setInterval(init, 3000);
        }
    });
}
