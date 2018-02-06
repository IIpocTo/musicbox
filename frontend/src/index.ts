import Vue from "vue";
import MainComponent from "./components/App/App.vue";

class AppCore {
  private instance: Vue;

  constructor() {
    this.init();
  }

  private init(): void {
    this.instance = new Vue({
      render: (h) => h(MainComponent),
    }).$mount("#app");
  }
}

// tslint:disable-next-line:no-unused-expression
const a = new AppCore();
