import {BrowserRouter as Router, Switch, Route} from "react-router-dom";
import Dashboard from "./pages/Dashboard/index";
import * as React from "react";
import NotFound from "./components/Common/NotFound";

const Routes: React.SFC<{}> = () => (
  <Router>
    <Switch>
      <Route exact path="/" component={Dashboard} />
      <Route path="*" component={NotFound} />
    </Switch>
  </Router>
);

export default Routes;
