import React, { useState, useEffect } from 'react'
import { connect, useDispatch } from 'dva'
import { Col, Row } from 'antd'
import { XPagination } from '@/components'
import Card from './components/Card'
import SideCard from './components/SideCard'
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

    useEffect(() => {
        getArticles()
    }, [])

    useEffect(() => {
        getArticles()
    }, [params])

    const getArticles = (newParams: IParams = params) => {
        dispatch({ type: 'home/getArticles', payload: newParams })
    }

    const handleChange = (...props) => {
        console.log(props)
    }

    const handleSizeChange = (...props) => {
        console.log(props)
    }

    return (
        <div>
            <div className={styles.wrapper}>
                <div className={styles.articles_wrapper}>
                    {articles.map(r => (
                        <Card data={r} key={r.articleId} />
                    ))}
                </div>
                <div className={styles.side_wrapper}>
                    <SideCard>654</SideCard>
                </div>
            </div>
            <XPagination
                onChange={handleChange}
                onShowSizeChange={handleSizeChange}
            />
        </div>
    )
}

export default connect(({ home }: connectState) => ({
    articles: home.articles
}))(Home)
