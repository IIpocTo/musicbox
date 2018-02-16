import * as React from "react";
import {Button} from "semantic-ui-react";

class Dashboard extends React.Component {
  public render(): React.ReactNode {
    return (
      <div>
        <Button primary size="huge">
          Get Started
        </Button>
        This is main page
      </div>
    );
  }
}

export default Dashboard;
