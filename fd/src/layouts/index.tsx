import React from 'react'
import { ConfigProvider } from 'antd'
import styles from './index.less'
import zhCN from 'antd/es/locale/zh_CN'
import BasicLayout from './BasicLayout'
import { PATH_WITHOUT_LAYOUT } from '../../config/routes.config'

export const ctx = React.createContext('')

const Layout = ({ location, children }) => {
    const { pathname } = location
    if (PATH_WITHOUT_LAYOUT.map((r) => r.path).includes(pathname)) {
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
