const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  //transpileDependencies: true,
  devServer: {
    proxy: {
      '/api':{
        //target:"http://34.129.9.77:8080",
        target:"http://localhost:8001",
        changeOrigin:true,
        pathRewrite: { '^/api': '' },
        }
      }
    }
})

