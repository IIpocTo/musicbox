import * as React from "react";
import Routes from "./router";
import Menu from "semantic-ui-react/dist/commonjs/collections/Menu/Menu";
import Sticky from "semantic-ui-react/dist/commonjs/modules/Sticky/Sticky";
import {RouteComponentProps, withRouter} from "react-router";
import {Component, ReactNode} from "react";
import {hot} from "react-hot-loader";
import {Translate} from "react-redux-i18n";

type Props = RouteComponentProps<any>;

class App extends Component<Props> {
  public render(): ReactNode {
    return (
      <div>
        <Sticky>
          <Menu size="massive" inverted fluid style={{ margin: 0, borderRadius: 0 }}>
            <Menu.Item>
              <Translate value="logo" />
            </Menu.Item>
            <Menu.Menu position="right">
              <Menu.Item onClick={() => { this.props.history.push("/"); }}>
                <Translate value="home" />
              </Menu.Item>
              <Menu.Item onClick={() => { this.props.history.push("/auth"); }}>
                <Translate value="signIn" />
              </Menu.Item>
            </Menu.Menu>
          </Menu>
        </Sticky>
        <Routes/>
          {/*<div className="ui inverted vertical footer segment" style={{ bottom: '0px', position: 'absolute' }}>*/}
            {/*This is footer*/}
          {/*</div>*/}
      </div>);
  }
}

export default hot(module)(withRouter(App));
