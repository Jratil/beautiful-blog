import React, { memo, useContext } from 'react'
import { Link } from 'umi'
import { useFullscreen } from '@umijs/hooks'
import { Icon, Layout, Menu } from 'antd'
import styles from './index.less'
import { ctx } from '../index'
import { PATH_WITH_LAYOUT } from '../index'

const { Header } = Layout
const MenuItem = Menu.Item

const CustomHeader = ({ ...restProps }) => {
    const pathname = useContext(ctx)
    const { isFullscreen, toggleFull } = useFullscreen({ dom: document.body })
    return (
        <Header {...restProps}>
            <div className={styles.logo} />
            <Icon type={isFullscreen ? 'fullscreen-exit' : 'fullscreen'} onClick={toggleFull} className={styles.fullscreen} />
            <Menu theme='dark' mode='horizontal' selectedKeys={[pathname]} style={{ lineHeight: '64px' }}>
                {PATH_WITH_LAYOUT.map(p => (
                    <MenuItem key={p.path}>
                        <Link to={p.path}>{p.title}</Link>
                    </MenuItem>
                ))}
            </Menu>
        </Header>
    )
}

export default memo(CustomHeader)
