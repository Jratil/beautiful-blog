export default {
    proxy: {
        '/api': {
            target: 'http://120.79.172.32:8000/',
            changeOrigin: true,
            pathRewrite: {
                '^/api': ''
            }
        }
    },
    plugins: [['umi-plugin-react', { dva: true, antd: true }]],
    hash: true
}
