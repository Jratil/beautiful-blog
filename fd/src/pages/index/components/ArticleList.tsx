import React, { useState, useEffect } from 'react'
import { Empty } from 'antd'
import { connect, useDispatch } from 'dva'
import Card from './Card'
import { IArticle } from '../model'
import styles from '../index.less'
import { XPagination } from '@/components'
import { connectState } from '@/models/connect'

interface IProps {
    articleData: {
        list: IArticle[]
        total: number
    }
}
interface IParams {
    page: number
    count?: number
}

const initParams = {
    page: 1,
    count: 10
}

const ArticleList: React.FC<IProps> = ({ articleData }) => {
    const dispatch = useDispatch()
    const [params, setParams] = useState<IParams>(initParams)
    const { list = [], total = 0 } = articleData

    useEffect(() => {
        _getArticles()
    }, [params])

    const handleChange = (page: number, count?: number) => setParams({ page, count })

    const ListContent = list.length === 0 ? <Empty /> : list.map(r => <Card data={r} key={r.articleId} />)

    const _getArticles = (newParams: IParams = params) => {
        dispatch({ type: 'home/getArticles', payload: newParams })
    }

    return (
        <>
            <div className={styles.articles_wrapper}>
                {ListContent}
                <XPagination onChange={handleChange} total={total + 20} {...params} />
            </div>
        </>
    )
}

export default connect(({ home }: connectState) => ({ articleData: home.articles }))(ArticleList)
