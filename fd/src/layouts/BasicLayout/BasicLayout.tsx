import React from 'react'
import styles from './index.less'
import { Layout, Breadcrumb, Icon } from 'antd'
import Header from './Header'
const { Content, Footer, Sider } = Layout

const BasicLayout: React.FC<any> = ({ children, ...restProps }) => {
    return (
        <Layout>
            <Header className={styles.header} />
            <Content className={styles.layout_content_wrapper}>
                <Layout style={{ padding: '24px 0', background: '#fff' }}>{children}</Layout>
            </Content>
            <Footer className={styles.footer}>Ant Design ©2018 Created by Ant UED</Footer>
        </Layout>
    )
}

export default BasicLayout
