import React, { useState, useEffect } from 'react'
import { connect, useDispatch } from 'dva'
import { Col, Row } from 'antd'
import { XPagination } from '@/components'
import Card from './components/Card'
import styles from './index.less'
import { connectState } from '@/models/connect'
import { IArticle } from './model'

interface IProps {
    articles: IArticle[]
}

interface IParams {
    page: number
    count: number
}

const initParams = {
    page: 1,
    count: 10
}

const Home: React.FC<IProps> = ({ articles }) => {
    const dispatch = useDispatch()
    const [params, setParams] = useState<IParams>(initParams)
    console.log(articles)

    useEffect(() => {
        getArticles()
    }, [])

    useEffect(() => {
        getArticles()
    }, [params])

    const getArticles = (newParams: IParams = params) => {
        dispatch({ type: 'home/getArticles', payload: newParams })
    }

    return (
        <div className={styles.wrapper}>
            <div style={{ width: 960 }}>
                <div className={styles.articles_wrapper}>
                    {articles.map(r => (
                        <Card data={r} />
                    ))}
                </div>
                <div></div>
            </div>
            <XPagination />
        </div>
    )
}

export default connect(({ home }: connectState) => ({
    articles: home.articles
}))(Home)
