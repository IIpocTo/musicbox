import * as React from "react";
import * as ReactDOM from "react-dom";
import Application from './app';

import "semantic-ui-css/semantic.min.css";
import {AppContainer} from "react-hot-loader";

export interface INodeModule extends NodeModule {
  hot: any;
}

const rootEl = (document as Document).getElementById("app");

ReactDOM.render(
  <AppContainer>
    <Application />
  </AppContainer>,
  rootEl
);

if ((module as INodeModule).hot) {
  (module as INodeModule).hot.accept('./app', () => {
    const NextApp = require("./app").default;
    ReactDOM.render(
      <AppContainer>
        <NextApp />
      </AppContainer>,
      rootEl
    );
  });
}