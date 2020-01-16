import React, { useState, useEffect } from 'react'
import { connect, useDispatch } from 'dva'
import Sider from './components/Sider'
import styles from './index.less'
import { connectState, connectProps } from '@/models/connect'
import { IArticle } from './model'
import ArticleList from './components/ArticleList'

interface IProps extends connectProps {
    articles: IArticle[]
    authorId: number
}

interface IParams {
    page: number
    count: number
}

const initParams = {
    page: 1,
    count: 10
}

const Home: React.FC<IProps> = ({ articles, authorId }) => {
    const dispatch = useDispatch()
    const [params, setParams] = useState<IParams>(initParams)

    useEffect(() => {
        // getArticles()
        getCategories()
    }, [authorId])

    // useEffect(() => {
    //     getArticles()
    // }, [params])

    // const getArticles = (newParams: IParams = params) => {
    //     dispatch({ type: 'home/getArticles', payload: { authorId } })
    // }

    const getCategories = () => {
        dispatch({ type: 'category/get', payload: { authorId } })
    }

    return (
        <div className={styles.wrapper}>
            <ArticleList data={articles} />
            <div className={styles.side_wrapper}>
                <Sider />
            </div>
        </div>
    )
}

export default connect(({ home, app }: connectState) => ({
    authorId: app.userInfo.authorId,
    articles: home.articles
}))(Home)
