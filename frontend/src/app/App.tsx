import * as React from "react";
import Routes from "./router";
import Menu from "semantic-ui-react/dist/commonjs/collections/Menu/Menu";
import Sticky from "semantic-ui-react/dist/commonjs/modules/Sticky/Sticky";
import {RouteComponentProps, withRouter} from "react-router";
import {Component, ReactNode} from "react";

type Props = RouteComponentProps<any>;

class App extends Component<Props> {
  public render(): ReactNode {
    return (
      <div>
        <Sticky>
          <Menu size="massive" inverted fluid style={{ margin: 0, borderRadius: 0 }}>
            <Menu.Item>
              Logo
            </Menu.Item>
            <Menu.Menu position="right">
              <Menu.Item onClick={() => { this.props.history.push("/"); }}>
                Home
              </Menu.Item>
            </Menu.Menu>
          </Menu>
        </Sticky>
        <Routes/>
      </div>);
  }
}

export default withRouter(App);
