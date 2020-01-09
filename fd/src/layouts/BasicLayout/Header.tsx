import React, { memo, useContext } from 'react'
import { Link } from 'umi'
import { connect, useDispatch } from 'dva'
import { useFullscreen } from '@umijs/hooks'
import { Icon, Layout, Menu, Avatar, Dropdown } from 'antd'
import styles from './index.less'
import { ctx } from '../index'
import { PATH_WITH_LAYOUT } from '../index'
import { connectState } from '@/models/connect'

interface IProps {
    avatarSrc: string
}

const { Header } = Layout
const MenuItem = Menu.Item

const CustomHeader: React.FC<IProps> = ({ avatarSrc, dispatch: _dispatch, ...restProps }) => {
    const dispatch = useDispatch()
    const pathname = useContext(ctx)
    const { isFullscreen, toggleFull } = useFullscreen({ dom: document.body })

    const handleLogout = () => dispatch({ type: 'app/logout' })

    const dropdownMenu = (
        <Menu>
            <MenuItem>
                <Link to='/user'>个人资料</Link>
            </MenuItem>
            <Menu.Divider />
            <MenuItem>
                <a onClick={handleLogout}>注销</a>
            </MenuItem>
        </Menu>
    )
    return (
        <Header {...restProps}>
            <div className={styles.logo} />
            <Dropdown overlay={dropdownMenu} placement='bottomRight'>
                <Avatar className={styles.avatar} src={avatarSrc} />
            </Dropdown>
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

export default connect(({ app }: connectState) => ({
    avatarSrc: app.userInfo.authorAvatar
}))(CustomHeader)
