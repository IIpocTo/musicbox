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

export const StyledFooter = styled.footer`
	width: 100%;
	position: absolute;
	display: flex;
	align-items: center;
	background-color: black;
	color: white;
	height: 72px;
	min-height: 72px;
	.footer-inner {
		padding: 15px 1rem;
	}
`;

export const Wrapper = styled.div`
  min-height: 100%;
  margin-bottom: -72px;
`;

export const Checker = styled.div`
  height: 72px;
`;
