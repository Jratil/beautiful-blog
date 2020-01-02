import React, { useState, useEffect } from 'react'
import { Avatar, Divider, Button } from 'antd'
import { connect } from 'dva'
import styles from '../index.less'
import { ICategory } from '@/pages/write/model'
import { connectState, connectProps } from '@/models/connect'

interface IProps extends connectProps {
    categories: ICategory[]
}

const Sider: React.FC<IProps> = ({ categories }) => {
    const handleCategory = () => {}
    return (
        <div className={styles.sider_wrapper}>
            <Avatar size='large' icon='user' />
            <span className={styles.name}>Micah</span>
            <Divider />
            <div className={styles.tag_wrapper}>
                {categories.map(r => (
                    <Button shape='round' key={r.id} className={styles.tag} onClick={handleCategory}>
                        {r.name}
                    </Button>
                ))}
            </div>
        </div>
    )
}

export default connect(({ category }: connectState) => ({
    categories: category.categories
}))(Sider)
