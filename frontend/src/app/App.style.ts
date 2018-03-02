import styled from "../utils/styled";
import {Menu} from "semantic-ui-react";

// interface IMenu {
//   size: 'mini' | 'tiny' | 'small' | 'large' | 'huge' | 'massive',
//   inverted: boolean;
//   fluid: boolean;
// }

// Menu has no Type parameters

export const MarginlessMenu = styled(Menu as any)`
  &&& {
    margin: 0;
    border-radius: 0;
  }
`;
