const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8848,
    https: false
  },
})
// module.exports = {
//   devServer: {
//     assetsSubDirectory: 'static',
//     assetsPublicPath: '/',
//     proxyTables: {
//       '/api': {
//         target: 'http://139.196.40.5:8848',
//         changeOrigin: true,
//         pathRewrite: {
//           '^/api': ''
//         }
//       }
//     },
//     host: '139.196.40.5',
//     port: 8848,
//   }
// }
