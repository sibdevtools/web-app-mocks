const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  mode: 'development',
  entry: './src/index.tsx',
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: 'ts-loader',
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },
    ],
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js'],
  },
  output: {
    filename: 'bundle.js',
    publicPath: '/web/app/mocks/ui/',
    path: path.resolve(__dirname, '../resources/web/app/mocks/static'),
  },
  devServer: {
    historyApiFallback: true,
    static: {
      directory: path.join(__dirname, '../resources/web/app/mocks/static'), // Serve from 'dist' directory
    },
    open: true, // Automatically open the browser
    port: 3000, // You can choose any port
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      minify: {
        removeComments: true,
        collapseWhitespace: true,
      },
    }),
  ],
};
