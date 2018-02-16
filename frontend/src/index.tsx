import * as React from "react";
import * as ReactDOM from "react-dom";
import {Provider} from "react-redux";
import App from "./app/App";
import configureStore from "./redux/store";

import "semantic-ui-css/semantic.min.css";

const store = configureStore();

ReactDOM.render(
  <Provider store={store} key="provider">
    <App/>
  </Provider>,
  (document as Document).getElementById("app"),
);
