import React from 'react'
import { Link } from 'umi'
import styles from './index.less'
import { PATH_WITH_LAYOUT } from '../index'
import { Layout, Menu, Breadcrumb, Icon } from 'antd'

const { Header, Content, Footer, Sider } = Layout
const MenuItem = Menu.Item

const BasicLayout = ({ children, ...restProps }) => {
    return (
        <Layout>
            <Header>
                <div className={styles.logo} />
                <Menu
                    theme='dark'
                    mode='horizontal'
                    defaultSelectedKeys={['/']}
                    style={{ lineHeight: '64px' }}
                >
                    {PATH_WITH_LAYOUT.map(p => (
                        <MenuItem key={p.path}>
                            <Link to={p.path}>{p.title}</Link>
                        </MenuItem>
                    ))}
                </Menu>
            </Header>
            <Content style={{ padding: '0 50px' }}>
                <Breadcrumb style={{ margin: '16px 0' }}>
                    <Breadcrumb.Item>Home</Breadcrumb.Item>
                    <Breadcrumb.Item>List</Breadcrumb.Item>
                    <Breadcrumb.Item>App</Breadcrumb.Item>
                </Breadcrumb>
                <Layout style={{ padding: '24px 0', background: '#fff' }}>
                    {children}
                </Layout>
            </Content>
            <Footer style={{ textAlign: 'center' }}>
                Ant Design ©2018 Created by Ant UED
            </Footer>
        </Layout>
    )
}

export default BasicLayout
