var publicDir = __dirname + '/public';
module.exports = [{
  entry: [
    './src/main/webapp/index.js'
  ],
  output: {
    path: publicDir,
    publicPath: '/',
    filename: 'bundle.js'
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
