const path = require('path')
const resolve = dir => path.resolve(__dirname, dir)
export const baseUrl = 'http://120.79.172.32:8000/'

export default {
    urlLoaderExcludes: [/\.svg$/],
    chainWebpack(config) {
        config.module
            .rule('svg')
            .test(/\.svg$/)
            .use(['babel-loader', '@svgr/webpack'])
            .loader(require.resolve('@svgr/webpack'))
    },
    proxy: {
        '/api': {
            target: baseUrl,
            changeOrigin: true,
            pathRewrite: {
                '^/api': ''
            }
        }
    },
    plugins: [['umi-plugin-react', { dva: true, antd: true }]],
    hash: true,
    alias: {
        icons: resolve('src/assets/icons')
    }
}
