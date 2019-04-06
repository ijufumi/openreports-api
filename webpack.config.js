var publicDir = __dirname + '/public';
module.exports = [{
  entry: [
    './websrc/index.js'
  ],
  output: {
    path: publicDir,
    publicPath: '/',
    filename: './src/main/webapp/index.js'
  },
  module: {
    rules: [{
      exclude: '/node_modules'
    }]
  },
  resolve: {
    extensions: ['.js', '.jsx']
  },
  devServer: {
    historyApiFallback: true
  }
}];
