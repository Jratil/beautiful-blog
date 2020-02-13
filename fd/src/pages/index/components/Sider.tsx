import React from 'react'
import { Divider, Button, Input } from 'antd'
import { connect } from 'dva'
import styles from '../index.less'
import { ICategory } from '@/models/category'
import { IArchive } from '@/models/archive'
import { connectState } from '@/models/connect'
import classnames from 'classnames'

interface IProps {
    categories: ICategory[]
    archives: IArchive[]
    loading: boolean
    categoryId: number
    currentArchive: string
    changeCategoryId(id: number): void
    changeArchive(archive: string): void
}

const { Search } = Input
const Sider: React.FC<IProps> = ({
    categories,
    categoryId,
    loading,
    changeCategoryId,
    archives,
    currentArchive,
    changeArchive
}) => {
    const handleCategory = (id: number) => {
        changeCategoryId(id === categoryId ? 0 : id)
    }

    const handleArchive = (current: string) => {
        changeArchive(current === currentArchive ? '' : current)
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
            <Divider>归档</Divider>
            <div className={classnames(styles.tag_wrapper, styles.archive_tag_wrapper)}>
                {archives.map((r) => (
                    <Button
                        shape="round"
                        key={r.archive}
                        className={styles.tag}
                        onClick={() => handleArchive(r.archive)}
                        type={r.archive === currentArchive ? 'primary' : 'default'}
                    >
                        {`${r.archive}（${r.count}）`}
                    </Button>
                ))}
            </div>
        </div>
    )
}

export default connect(({ category, loading, archive }: connectState) => ({
    categories: category.categories,
    archives: archive.archives,
    loading: loading.effects['home/getArticlesByCategory']
}))(Sider)
