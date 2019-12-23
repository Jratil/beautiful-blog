import React from 'react'
import styles from './index.less'
import { Layout, Breadcrumb, Icon } from 'antd'
import Header from './Header'
const { Content, Footer, Sider } = Layout

const BasicLayout = ({ children, ...restProps }) => {
    return (
        <Layout>
            <Header />
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
