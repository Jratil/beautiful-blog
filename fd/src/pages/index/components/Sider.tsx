import React, { useState, useEffect } from 'react'
import { Avatar, Divider, Button, Input } from 'antd'
import { connect } from 'dva'
import styles from '../index.less'
import { ICategory } from '@/pages/write/model'
import { connectState, connectProps } from '@/models/connect'

interface IProps extends connectProps {
    categories: ICategory[]
}

const { Search } = Input
const Sider: React.FC<IProps> = ({ categories }) => {
    const handleCategory = () => {}
    return (
        <div className={styles.sider_wrapper}>
            <Search onSearch={value => console.log(value)} />
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
