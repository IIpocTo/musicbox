import {BundleAnalyzerPlugin} from "webpack-bundle-analyzer";
import {Configuration} from "webpack";
import * as webpack from "webpack";

const webpackConfig: Configuration = {
  entry: ["react-hot-loader/patch", "./src/index.tsx"],
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
        test: /\.(ts|tsx)$/,
        loaders: ["awesome-typescript-loader"],
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        loaders: ["style-loader", "css-loader"],
      },
      {
        test: /\.(woff(2)?|ttf|eot|svg)(\?v=\d+\.\d+\.\d+)?$/,
        use: "url-loader?limit=10000",
      },
      {
        test: /\.(jpg|png|gif)$/,
        use: "file-loader",
      },
    ],
  },
  resolve: {
    extensions: [".tsx", ".ts", ".js"],
  },
  plugins: [
    new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
    new webpack.NamedModulesPlugin(),
    new webpack.HotModuleReplacementPlugin(),
  ],
  devtool: "inline-source-map",
};

if (process.argv.length > 2 && process.argv[2] === "--analyze") webpackConfig.plugins.push(new BundleAnalyzerPlugin());

export default webpackConfig;
