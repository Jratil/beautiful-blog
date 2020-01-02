import React from 'react'
import { Empty } from 'antd'
import Card from './Card'
import { IArticle } from '../model'
import styles from '../index.less'
import { XPagination } from '@/components'

interface IProps {
    data: IArticle[]
}

const ArticleList: React.FC<IProps> = ({ data }) => {
    const handleChange = (...props) => {
        console.log(props)
    }

    const handleSizeChange = (...props) => {
        console.log(props)
    }

    const ListContent = data.length === 0 ? <Empty /> : data.map(r => <Card data={r} key={r.articleId} />)

    return (
        <>
            <div className={styles.articles_wrapper}>
                {ListContent}
                <XPagination onChange={handleChange} onShowSizeChange={handleSizeChange} />
            </div>
        </>
    )
}

export default ArticleList
