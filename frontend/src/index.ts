import Vue from 'vue'
import MainComponent from "./components/App/App";

class AppCore {
  private instance: Vue;

  private init() {
    this.instance = new Vue({
      el: '#app',
      render: h => h(MainComponent),
    })
  }

  constructor() {
    this.init();
  }
}

new AppCore();