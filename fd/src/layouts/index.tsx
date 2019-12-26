import React from 'react'
import { ConfigProvider } from 'antd'
import styles from './index.less'
import zhCN from 'antd/es/locale/zh_CN'
import BasicLayout from './BasicLayout'

export const ctx = React.createContext('')

export const PATH_WITH_LAYOUT = [
    {
        path: '/',
        type: 'home',
        title: '首页'
    },
    {
        path: '/dashboard',
        type: 'home',
        title: '总览'
    },
    {
        path: '/category',
        type: 'home',
        title: '分类'
    },
    {
        path: '/link',
        type: 'home',
        title: '友情链接'
    },
    {
        path: '/message',
        type: 'home',
        title: '留言板'
    },
    {
        path: '/about',
        type: 'rocket',
        title: '关于'
    },
    {
        path: '/write',
        type: 'rocket',
        title: '写文章'
    }
]

const PATH_WITHOUT_LAYOUT = ['/login', '/registry']

const Layout = ({ location, children }) => {
    const { pathname } = location
    if (PATH_WITHOUT_LAYOUT.includes(pathname)) {
        return <div className={styles.app_wrapper}>{children}</div>
    }

    return (
        <ctx.Provider value={location.pathname}>
            <ConfigProvider locale={zhCN}>
                <BasicLayout> {children} </BasicLayout>
            </ConfigProvider>
        </ctx.Provider>
    )
}

export default Layout
