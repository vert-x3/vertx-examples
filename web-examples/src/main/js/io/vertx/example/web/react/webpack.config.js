// webpack.config.js
var webpack = require('webpack');

module.exports = {
  devtool: 'source-map',
  entry: './app-client.js',
  output: {
    path: __dirname + '/webroot/dist',
    filename: 'bundle.js',
    publicPath: '/dist/'
  },
  module: {
    loaders: [{
      test: /\.js$/,
      loaders: ['babel'],
      exclude: /node_modules/
    }]
  }
};
