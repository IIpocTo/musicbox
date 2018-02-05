module.exports = {
  entry: './src/index.ts',
  output: {
    filename: 'bundle.js',
    path: __dirname,
    publicPath: '/public/'
  },
  module: {
    rules: [
      {
        test: /\.ts(x)?$/,
        loader: 'awesome-typescript-loader',
        options: {
          configFileName: 'tsconfig.json',
          appendTsSuffixTo: [/\.vue$/]
        }
      },
      {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: {
          loaders: {
            ts: 'awesome-typescript-loader'
          },
          esModule: true
        }
      },
      {
        test: /\.css/,
        loaders: ['style-loader', 'css-loader']
      }
    ]
  },
  devServer: {
    compress: true,
    port: 8001
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js', '.vue']
  },
  devtool: 'source-map'
};