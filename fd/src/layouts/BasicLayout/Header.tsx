import React, { memo, useContext } from 'react'
import { Link, router } from 'umi'
import { connect, useDispatch } from 'dva'
import { useFullscreen } from '@umijs/hooks'
import { Icon, Layout, Menu, Avatar, Dropdown } from 'antd'
import styles from './index.less'
import { ctx } from '../index'
import { PATH_WITH_LAYOUT } from '../../../config/routes.config'
import { connectState } from '@/models/connect'
import { XAvatar } from '@/components'

interface IProps {
    avatarSrc: string
    authorName: string
}

const { Header } = Layout
const MenuItem = Menu.Item

const CustomHeader: React.FC<IProps> = ({ avatarSrc, authorName, ...restProps }) => {
    const dispatch = useDispatch()
    const pathname = useContext(ctx)
    const { isFullscreen, toggleFull } = useFullscreen({ dom: document.body })

    const handleLogout = () => dispatch({ type: 'app/logout' })

    const handleLogoClick = () => {
        router.push('/')
    }

    const dropdownMenu = (
        <Menu>
            <MenuItem key="user">
                <Link to="/user">个人资料</Link>
            </MenuItem>
            <Menu.Divider />
            <MenuItem key="logout">
                <a onClick={handleLogout}>注销</a>
            </MenuItem>
        </Menu>
    )
    return (
        <Header {...restProps}>
            <div className={styles.logo} onClick={handleLogoClick}>
                Ratil Blog
            </div>
            <Dropdown overlay={dropdownMenu} placement="bottomRight">
                <div className={styles.avatar}>
                    <XAvatar avatarSrc={avatarSrc} name={authorName} />
                </div>
            </Dropdown>
            <Icon
                type={isFullscreen ? 'fullscreen-exit' : 'fullscreen'}
                onClick={toggleFull}
                className={styles.fullscreen}
            />
            <Menu theme="dark" mode="horizontal" selectedKeys={[pathname]} style={{ lineHeight: '64px' }}>
                {PATH_WITH_LAYOUT.filter((r) => !r.exclude).map((p) => (
                    <MenuItem key={p.path}>
                        <Link to={p.path}>{p.title}</Link>
                    </MenuItem>
                ))}
            </Menu>
        </Header>
    )
}

export default connect(({ app }: connectState) => ({
    avatarSrc: app.userInfo.authorAvatar,
    authorName: app.userInfo.authorName
}))(CustomHeader)
