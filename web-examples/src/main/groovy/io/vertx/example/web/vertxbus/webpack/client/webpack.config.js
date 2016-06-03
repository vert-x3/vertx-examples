var path = require('path');
var HtmlwebpackPlugin = require('html-webpack-plugin');
var merge = require('webpack-merge');
var webpack = require('webpack');
var Clean = require('clean-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

var pkg = require('./package.json');

var TARGET = process.env.TARGET;

var ROOT_PATH = path.resolve(__dirname);

var common = {
  entry: path.resolve(ROOT_PATH, 'app/main'),
  resolve: {
    extensions: ['', '.js', '.jsx']
  },
  output: {
    path: path.resolve(ROOT_PATH, '../webroot'),
    filename: 'bundle.js'
  },
  module: {
    loaders: [
      {
        test: /\.(woff|woff2|eot|ttf)$/,
        loader: 'url?limit=100000'
      }, {
        test: /\.(png|jpg|svg)$/,
        loader: 'url?limit=25000'
      }
    ]
  },
  plugins: [
    new HtmlwebpackPlugin({
      title: 'Vertx3 WebPack SPA'
    })
  ]
};

if (TARGET === 'dev') {
  module.exports = merge(common, {
    devtool: 'eval',
    module: {
      loaders: [
        {
          test: /\.css$/,
          loaders: ['style', 'css'],
          include: path.resolve(ROOT_PATH, 'app')
        },
        {
          test: /\.jsx?$/,
          loaders: ['react-hot', 'babel?stage=1'],
          include: [
            path.resolve(ROOT_PATH, 'app')
          ]

        }
      ]
    }
  });
}
if(TARGET === 'build') {
  module.exports = merge(common, {
    entry: {
      app: path.resolve(ROOT_PATH, 'app/main'),
      vendor: Object.keys(pkg.dependencies)
    },
    output: {
      path: path.resolve(ROOT_PATH, '../webroot'),
      filename: 'app.[chunkhash].js'
    },
    module: {
      loaders: [
        {
          test: /\.css$/,
          loader: ExtractTextPlugin.extract('style', 'css'),
          include: path.resolve(ROOT_PATH, 'app')
        },
        {
          test: /\.jsx?$/,
          loaders: ['babel?stage=1'],
          include: path.resolve(ROOT_PATH, 'app')
        }
      ]
    },
    plugins: [
      new ExtractTextPlugin('styles.css'),
      new Clean(['build/assets']),
      new webpack.optimize.CommonsChunkPlugin(
          'vendor',
          'vendor.[chunkhash].js'
      ),
      new webpack.DefinePlugin({
        'process.env': {
          // This affects react lib size
          'NODE_ENV': JSON.stringify('production')
        }
      }),
      new webpack.optimize.UglifyJsPlugin({
        compress: {
          warnings: false
        }
      })
    ]
  });
}
