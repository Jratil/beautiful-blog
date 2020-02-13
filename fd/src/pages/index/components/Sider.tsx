import React from 'react'
import { Divider, Button, Input } from 'antd'
import { connect } from 'dva'
import styles from '../index.less'
import { ICategory } from '@/models/category'
import { connectState } from '@/models/connect'

interface IProps {
    categories: ICategory[]
    loading: boolean
    categoryId: number
    changeCategoryId(id: number): void
}

const { Search } = Input
const Sider: React.FC<IProps> = ({ categories, categoryId, loading, changeCategoryId, GetArticleRef }) => {
    const handleCategory = (id: number) => {
        changeCategoryId(id === categoryId ? 0 : id)
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
                        type={r.categoryId === categoryId ? 'primary' : 'default'}
                    >
                        {r.categoryName}
                    </Button>
                ))}
            </div>
        </div>
    )
}

export default connect(({ category, loading }: connectState) => ({
    categories: category.categories,
    loading: loading.effects['home/getArticlesByCategory']
}))(Sider)
