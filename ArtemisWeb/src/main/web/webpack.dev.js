var config = require('./webpack.config');

var devConfig = {
  devServer: {
    port: 8080,
    inline: true,
    disableHostCheck: true,
    proxy: {
      '/service/*': {
        target: 'http://127.0.0.1:8011',
        secure: false
      },
      '/login/*': {
        target: 'http://127.0.0.1:8011',
        secure: false
      }
    }
  }
}

config.devServer = devConfig.devServer;
config.devtool = 'inline-source-map';
module.exports = config;