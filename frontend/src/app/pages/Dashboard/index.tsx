import * as React from "react";
import Button from "semantic-ui-react/dist/commonjs/elements/Button/Button";
import {Image} from "semantic-ui-react";
import {Container} from "semantic-ui-react";
import {RouteComponentProps} from "react-router";
import {Component, ReactNode} from "react";
import {Translate} from "react-redux-i18n";
import * as mainImg from '../../../assets/img/main.jpg';
import {ContainerWith25Margin, FullWidthRail, NullMarginSegment, WhiteH1} from "./Dashboard.style";

type Props = RouteComponentProps<any>;

class Dashboard extends Component<Props> {
  public render(): ReactNode {
    return (
      <div>
        <NullMarginSegment textAlign='center'>
          <Image src={mainImg} fluid spaced={false} />
          <FullWidthRail internal position="left">
            <ContainerWith25Margin textAlign="center">
              <WhiteH1><Translate value="welcome" /></WhiteH1>
              <Button primary size="huge" onClick={() => { this.props.history.push("/docs"); }}>
                <Translate value="getStarted" />
              </Button>
            </ContainerWith25Margin>
          </FullWidthRail>
        </NullMarginSegment>
        <Container>
          This is main page
        </Container>
      </div>
    );
  }
}

export default Dashboard;
