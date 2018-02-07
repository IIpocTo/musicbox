import {Router, Switch, Route} from "react-router";
import Dashboard from "./pages/Dashboard/index";
import * as React from "react";
import NotFound from "./components/Common/NotFound";
import {SFC} from "react";

const Routes: SFC<{}> = () => (
  <Router>
    <Switch>
      <Route exact path="/" component={Dashboard} />
      <Route path="*" component={NotFound} />
    </Switch>
  </Router>
);

export default Routes;
