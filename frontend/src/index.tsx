import * as React from "react";
import * as ReactDOM from "react-dom";
import {StatelessComponent} from "react-redux";
import Application from './app';

import "semantic-ui-css/semantic.min.css";
import {AppContainer} from "react-hot-loader";

interface INodeModule extends NodeModule {
  hot: any;
}

const render = (Component: StatelessComponent<{}>) =>
  ReactDOM.render(
    <AppContainer>
      <Component/>
    </AppContainer>,
  (document as Document).getElementById("app"),
);

render(Application);

const mod = module as INodeModule;

if (mod.hot) {
  console.log("asdasd");
  mod.hot.accept('./app', () => {
    console.log("sdfsdf");
    render(Application);
  });
}