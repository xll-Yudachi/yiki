const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    open: true,
    host: 'localhost',
    port: 8080,
    proxy: {
      '/gateway': {
        target: 'http://localhost:12888/',//这里填入你要请求的接口的前缀
        ws: true,//代理websocked
        changeOrigin: true,//虚拟的站点需要更管origin
        pathRewrite: {
          '^/gateway': ''//重写路径
        },
      }
    }
  }
})
