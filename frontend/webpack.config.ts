import {Configuration} from "webpack";

const webpackConfig: Configuration = {
  entry: "./src/index.ts",
  output: {
    filename: "bundle.js",
    path: __dirname,
    publicPath: "/",
  },
  module: {
    rules: [
      {
        test: /\.ts$/,
        exclude: /node_modules/,
        enforce: "pre",
        loader: "tslint-loader",
      },
      {
        test: /\.ts$/,
        loader: "awesome-typescript-loader",
        exclude: /node_modules/,
        options: {
          configFileName: "tsconfig.json",
          appendTsSuffixTo: [/\.vue$/],
        },
      },
      {
        test: /\.vue$/,
        loader: "vue-loader",
        options: {
          loaders: {
            ts: "awesome-typescript-loader",
          },
          esModule: true,
        },
      },
      {
        test: /\.css$/,
        loaders: ["style-loader", "css-loader"],
      },
    ],
  },
  resolve: {
    extensions: [".tsx", ".ts", ".js", ".vue"],
  },
  devtool: "source-map",
};

export default webpackConfig;
