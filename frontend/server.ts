import * as path from "path";
import * as webpack from "webpack";
import * as webpackMiddleware from "webpack-dev-middleware";
import * as webpackHotMiddleware from "webpack-hot-middleware";
import * as express from "express";

import devConfig from "./webpack.config";

let config: webpack.Configuration;
process.env.NODE_ENV === "development"
  ? config = devConfig
  : config = {};

const app: express.Application = express();
const compiler: webpack.Compiler = webpack(config);

app.use(webpackMiddleware(compiler, {
  publicPath: config.output !== undefined
    ? (config.output.publicPath !== undefined)
      ? config.output.publicPath
      : ""
    : "",
}));

if (process.env.NODE_ENV === "development") {
  app.use(webpackHotMiddleware(compiler));
}

app.use(express.static(path.join(__dirname, "../public")));

app.listen(3000, () => {
  // tslint:disable-next-line:no-console
  console.log("Server is listening on port 3000!");
});
