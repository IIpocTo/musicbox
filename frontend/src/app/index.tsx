import App from "./App";
import {BrowserRouter as Router} from "react-router-dom";
import {Provider} from "react-redux";
import * as React from "react";
import configureStore from "../redux/store";

const store = configureStore();

export default () =>
  <Provider store={store}>
    <Router>
      <App/>
    </Router>
  </Provider>;