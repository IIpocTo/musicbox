import * as React from "react";
import Button from "semantic-ui-react/dist/commonjs/elements/Button/Button";
import Image from "semantic-ui-react/dist/commonjs/elements/Image/Image";
import Container from "semantic-ui-react/dist/commonjs/elements/Container/Container";
import Segment from "semantic-ui-react/dist/commonjs/elements/Segment/Segment";
import Rail from "semantic-ui-react/dist/commonjs/elements/Rail/Rail";
import {RouteComponentProps} from "react-router";

// import * as mainImg from '../../../assets/img/main.jpg';
const mainImg = require('../../../assets/img/main.jpg');

type Props = RouteComponentProps<any>;

class Dashboard extends React.Component<Props> {
  public render(): React.ReactNode {
    return (
      <div>
        <Segment textAlign='center' style={{ margin: 0, padding: 0, border: 0 }}>
          <Image src={mainImg} fluid spaced={false} />

          <Rail internal position="left" style={{ width: '100%' }}>
            <Container textAlign="center" style={{ marginTop: '25%' }}>
              <h1 style={{ color: 'white' }}>Welcome</h1>
              <Button primary size="huge" onClick={() => { this.props.history.push("/docs"); }}>
                Get Started
              </Button>
            </Container>
          </Rail>
        </Segment>
        <Container>

          This is main page
        </Container>
      </div>
    );
  }
}

export default Dashboard;
