import * as React from "react";
import * as ReactDOM from "react-dom";
import Application from './app';

import "semantic-ui-css/semantic.min.css";
import "./styles.css";

const rootEl = (document as Document).getElementById("app");

ReactDOM.render(
    <Application />,
  rootEl
);