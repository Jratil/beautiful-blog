export const baseUrl = 'http://120.79.172.32:8000/'

export default {
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
    hash: true
}
