import React from 'react'
import { Divider, Button, Input, Modal, message } from 'antd'
import { connect, useDispatch } from 'dva'
import styles from '../index.less'
import { ICategory } from '@/models/category'
import { IArchive } from '@/models/archive'
import { connectState } from '@/models/connect'
import classnames from 'classnames'

interface IProps {
    authorId: number
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
    authorId,
    categories,
    categoryId,
    loading,
    changeCategoryId,
    archives,
    currentArchive,
    changeArchive
}) => {
    const dispatch = useDispatch()

    const handleCategory = (id: number) => {
        changeCategoryId(id === categoryId ? 0 : id)
    }

    const handleArchive = (current: string) => {
        changeArchive(current === currentArchive ? '' : current)
    }

    const handleAddCategory = () => {
        let newCategory = ''
        Modal.confirm({
            title: '添加类目',
            content: <Input onChange={(e) => (newCategory = e.target.value)} allowClear />,
            onOk: () => {
                dispatch({
                    type: 'category/add',
                    payload: { categoryName: newCategory },
                    callback: () => {
                        message.success('添加类目成功')
                        dispatch({ type: 'category/get', payload: { authorId } })
                    }
                })
            }
        })
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

                <Button shape="round" key="$new" className={styles.tag} onClick={handleAddCategory} type="dashed">
                    添加类目 +
                </Button>
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

export default connect(({ app, category, loading, archive }: connectState) => ({
    authorId: app.userInfo.authorId,
    categories: category.categories,
    archives: archive.archives,
    loading: loading.effects['home/getArticlesByCategory']
}))(Sider)
