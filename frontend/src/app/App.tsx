import * as React from "react";
import Routes from "./router";
import {Menu} from "semantic-ui-react";
import {Sticky} from "semantic-ui-react";
import {RouteComponentProps, withRouter} from "react-router";
import {Component, ReactNode} from "react";
import {hot} from "react-hot-loader";
import {Translate} from "react-redux-i18n";
import {Checker, MarginlessMenu, StyledFooter, Wrapper} from "./App.style";

type Props = RouteComponentProps<any>;

class App extends Component<Props> {
  public render(): ReactNode {
    return (
      <div style={{ height: '100%', margin: 0 }}>
        <Wrapper>
          <Sticky>
            <MarginlessMenu size="massive" inverted fluid>
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
            </MarginlessMenu>
          </Sticky>
          <Routes/>
          <Checker />
        </Wrapper>
        <StyledFooter>
          This is footer
        </StyledFooter>
      </div>);
  }
}

export default hot(module)(withRouter(App));
