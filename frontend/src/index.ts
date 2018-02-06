import * as React from "react";
import * as ReactDOM from "react-dom";
import {ReactNode, Component} from "react";
import App from "./components/App/App";

class AppCore {
  private routes: Array<{ path: string, component: Component }>;

  constructor() {
    this.init();
  }

  private init(): void {
    ReactDOM.render(
      App,
      document.getElementById("app"),
    );
  }
}
