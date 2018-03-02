import {BundleAnalyzerPlugin} from "webpack-bundle-analyzer";
import {Configuration} from "webpack";
import * as webpack from "webpack";
import * as path from "path";

const webpackConfig: Configuration = {
  entry: ["react-hot-loader/patch", "./src/index.tsx"],
  output: {
    filename: "bundle.js",
    path: path.join(__dirname, './public'),
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
        use: [
          {
            loader: 'babel-loader',
            options: {
              babelrc: true,
              plugins: ['react-hot-loader/babel'],
            },
          },
          "awesome-typescript-loader"],
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        loaders: ["style-loader", "css-loader" ],
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
  devServer: {
    historyApiFallback: true,
    contentBase: './public',
    hot: true,
    port: 3000,
  },
};

if (process.argv.length > 2 && process.argv[2] === "--analyze") webpackConfig.plugins.push(new BundleAnalyzerPlugin());

export default webpackConfig;
