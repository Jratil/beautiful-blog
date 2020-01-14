export const PATH_WITH_LAYOUT = [
    {
        path: '/',
        type: 'home',
        title: '首页',
        component: './index/index'
    },
    // {
    //     path: '/dashboard',
    //     type: 'home',
    //     title: '总览'
    // },
    // {
    //     path: '/category',
    //     type: 'home',
    //     title: '分类'
    // },
    // {
    //     path: '/link',
    //     type: 'home',
    //     title: '友情链接'
    // },
    // {
    //     path: '/message',
    //     type: 'home',
    //     title: '留言板'
    // },
    // {
    //     path: '/about',
    //     type: 'rocket',
    //     title: '关于'
    // },
    {
        path: '/write',
        type: 'rocket',
        title: '写文章',
        component: './write/index'
    }
]

export const PATH_WITHOUT_LAYOUT = [
    { path: '/login', title: '登录', component: './login' },
    { path: '/registry', title: '注册', component: './registry' }
]

export default [
    {
        path: '/',
        component: '../layouts',
        routes: [...PATH_WITH_LAYOUT, ...PATH_WITHOUT_LAYOUT]
    }
]
