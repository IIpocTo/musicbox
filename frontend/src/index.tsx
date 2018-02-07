import * as React from "react";
import * as ReactDOM from "react-dom";
import App from "./app/App";
import "semantic-ui-css/semantic.min.css";
import configureStore from './redux/store';
import {Provider} from "react-redux";

const store = configureStore();

ReactDOM.render(
  <Provider store={store} key="provider">
    <App/>
  </Provider>,
  document.getElementById("app"),
);
