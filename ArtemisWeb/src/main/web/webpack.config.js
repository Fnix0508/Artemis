var webpack = require('webpack');
var path =require('path');
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {

  entry: './index.jsx',

  output: {
    path: path.join(__dirname, '../resources'),
    filename: 'artemis.js'
  },

  module: {
    rules: [
      {
        test: /(\.jsx|\.js)$/,
        use: {
          loader: 'babel-loader'
        },
        exclude: /node_modules/
      },
      {
        test: /\.css$/,
        loaders: [
          'style-loader?sourceMap',
          'css-loader?modules&importLoaders=1&localIdentName=[local]___[hash:base64:5]'
        ]
      },
      {
        test: /\.(png|woff|svg|eot|ttf)$/,
        use: [
          {
            loader: 'url-loader',
            options: {
              name: '[name]__[hash].[ext]'
            }
          }
        ]
      }
    ]
  },

  resolve: {
    alias: {
      'pages': path.join(__dirname, './pages'),
      'component': path.join(__dirname, './component'),
      'resource': path.join(__dirname, './resource'),
      'service': path.join(__dirname, './service')
    }
  },

  plugins: [
    new HtmlWebpackPlugin({filename: 'index.html', template: './index_tpl.html'})
  ]
};