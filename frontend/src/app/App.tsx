import * as React from "react";
import Routes from "./router";

class App extends React.Component<{}, {}> {
  public render(): React.ReactNode {
    return (
      <div>
        <Routes/>
      </div>);
  }
}

export default App;
