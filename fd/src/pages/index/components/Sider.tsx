import React, { useState, useEffect } from 'react'
import { Avatar, Divider, Button, Input } from 'antd'
import { connect, useDispatch } from 'dva'
import styles from '../index.less'
import { ICategory } from '@/models/category'
import { connectState, connectProps } from '@/models/connect'

interface IProps extends connectProps {
    categories: ICategory[]
}

const { Search } = Input
const Sider: React.FC<IProps> = ({ categories }) => {
    const dispatch = useDispatch()
    const [selectId, setSelectId] = useState<number>(0)
    const handleCategory = (categoryId: number) => {
        setSelectId(categoryId)
        dispatch({ type: 'home/getArticlesByCategory', payload: { categoryId } })
    }
    return (
        <div className={styles.sider_wrapper}>
            <Search onSearch={(value) => console.log(value)} />
            <Divider>分类</Divider>
            <div className={styles.tag_wrapper}>
                {categories.map((r) => (
                    <Button
                        shape="round"
                        key={r.categoryId}
                        className={styles.tag}
                        onClick={() => handleCategory(r.categoryId)}
                        type={r.categoryId === selectId ? 'primary' : 'default'}
                    >
                        {r.categoryName}
                    </Button>
                ))}
            </div>
        </div>
    )
}

export default connect(({ category }: connectState) => ({
    categories: category.categories
}))(Sider)
