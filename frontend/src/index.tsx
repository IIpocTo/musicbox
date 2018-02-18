import * as React from "react";
import * as ReactDOM from "react-dom";
import {Provider} from "react-redux";
import App from "./app/App";
import configureStore from "./redux/store";

import "semantic-ui-css/semantic.min.css";
import {BrowserRouter as Router} from "react-router-dom";

const store = configureStore();

ReactDOM.render(
  <Provider store={store} key="provider">
    <Router>
      <App/>
    </Router>
  </Provider>,
  (document as Document).getElementById("app"),
);
