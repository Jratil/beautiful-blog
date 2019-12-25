export default {
    proxy: {
        '/api': {
            target: 'http://api.jratil.co:8866/',
            changeOrigin: true,
            pathRewrite: {
                '^/api': ''
            }
        }
    },
    plugins: [['umi-plugin-react', { dva: true, antd: true }]],
    hash: true
}
