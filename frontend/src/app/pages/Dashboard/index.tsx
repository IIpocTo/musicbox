import {Component, ReactNode} from "react";
import * as React from "react";
import Button from "semantic-ui-react/dist/commonjs/elements/Button/Button";
import Image from "semantic-ui-react/dist/commonjs/elements/Image/Image";
import Menu from "semantic-ui-react/dist/commonjs/collections/Menu/Menu";
import Container from "semantic-ui-react/dist/commonjs/elements/Container/Container";
// import * as mainImg from '../../../assets/img/main.jpg'; WHY DIS SHIT AINT WORKIN?!!!!
const mainImg = require('../../../assets/img/main.jpg');

class Dashboard extends Component {
  render(): ReactNode {
    return (
      <div>
        <Menu>
          <Menu.Menu position="right">
            <Menu.Item>
              Home
            </Menu.Item>
          </Menu.Menu>
        </Menu>
        <Image src={mainImg} fluid />
        <Container>
          <Button primary size="huge">
            Get Started
          </Button>
          This is main page
        </Container>
      </div>
    );
  }
}

export default Dashboard;
