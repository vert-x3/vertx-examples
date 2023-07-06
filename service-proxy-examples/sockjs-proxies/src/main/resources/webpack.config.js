// webpack.config.js
const path = require('path');

const webpack = require("webpack");

module.exports = {

  entry: {
    app: path.resolve(__dirname, "client/main.ts")
  },
  resolve: {
    extensions: [".js", ".ts"]
  },
  output: {
    path: path.resolve(__dirname, "webroot"),
    filename: "[name].js"
  },

  module: {
    rules: [
      {
        test: /\.ts$/,
        loader: "ts-loader"
      }
    ]
  },

  devtool: "source-map"
};
