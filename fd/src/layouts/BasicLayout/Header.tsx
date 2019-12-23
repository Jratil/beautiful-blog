import React, { memo, useContext } from 'react'
import { Link } from 'umi'
import { Icon } from 'antd'
import styles from './index.less'
import LOGO from '@/assets/logo.png'
import { ctx } from '../index'
import classnames from 'classnames'
const routes = [
    {
        path: '/',
        type: 'home',
        title: '首页'
    },
    {
        path: '/about',
        type: 'rocket',
        title: '关于'
    }
]

const activeStyle = {
    color: 'red'
}

const Header = props => {
    const pathname = useContext(ctx)
    const isActive = (path: string) => pathname === path
    return (
        <div className={styles.header_wrapper}>
            <img className={styles.logo} src={LOGO} alt='logo' />
            {routes.map(r => (
                <Link
                    key={r.path}
                    to={r.path}
                    className={classnames(
                        styles.menu,
                        isActive(r.path) ? styles.active : void 0
                    )}
                >
                    <Icon
                        type={r.type}
                        theme={isActive(r.path) ? 'filled' : 'twoTone'}
                    />
                    <span>{r.title}</span>
                </Link>
            ))}
        </div>
    )
}

export default memo(Header)
