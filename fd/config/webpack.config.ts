import path from 'path'

const resolve = (dir: string) => path.resolve(__dirname, dir)

export default {
    alias: {
        icons: resolve('../src/assets/icons/')
    },
    urlLoaderExcludes: [/\.svg$/],
    chainWebpack(config: any) {
        config.module
            .rule('svg')
            .test(/\.svg$/)
            .use(['babel-loader', '@svgr/webpack'])
            .loader(require.resolve('@svgr/webpack'))
    }
}
