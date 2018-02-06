import { Component } from "vue-router/types/router";
import Register from "./components/Register/Register.vue";
import App from "./components/App/App.vue";

export default [
   {
       path: "/register",
       component: Register,
   },
   {
       path: "/",
       component: {
           template: "<div>asdasd</div>",
       },
   },
   {
       path: "*", component: {
           template: "<div></div>",
        },
    },
] as Array<{path: string, component: Component}>;
