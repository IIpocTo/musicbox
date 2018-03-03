import {Switch, Route} from "react-router-dom";
import Dashboard from "./pages/Dashboard/index";
import * as React from "react";
import NotFound from "./components/Common/NotFound";
import Docs from "./pages/Docs";
import Auth from "./pages/Auth";

const Routes: React.SFC<{}> = () => (
    <Switch>
      <Route exact path="/" component={Dashboard} />
      <Route exact path="/docs" component={Docs} />
      <Route exact path="/auth" component={Auth} />
      <Route path="*" component={NotFound} />
    </Switch>
);

export default Routes;