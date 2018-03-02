import {Segment, Rail, Container, SemanticTEXTALIGNMENTS} from "semantic-ui-react";
import styled from "../../../utils/styled";
import {SemanticFLOATS} from "semantic-ui-react/dist/commonjs";

// TODO: donna know  why SegmentProps or RailProps are not valid

interface ISegment {
  textAlign?: SemanticTEXTALIGNMENTS | undefined;
}

export const NullMarginSegment = styled<ISegment>(Segment)`
  &&& {
    margin: 0;
    padding: 0;
    border: 0;
  }
`;

interface IRail {
  internal?: boolean;
  position: SemanticFLOATS;
}

export const FullWidthRail = styled<IRail>(Rail)`
  &&& {
    width: 100%;
    margin: 0;
    padding: 0;
  }
`;

export const ContainerWith25Margin = styled<ISegment>(Container)`
  &&& {
    margin-top: 25%;
  }
`;

export const WhiteH1 = styled.h1`
  color: white;
`;
