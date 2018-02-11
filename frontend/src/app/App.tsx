import * as React from "react";
import {Component, ReactNode} from "react";
import Routes from "./router";

class App extends Component<{}, {}> {
  public render(): ReactNode {
    return (
      <div>
        <Routes/>
      </div>);
  }
}

export default App;
