import React, { useState, useEffect, useRef } from 'react'
import { connect, useDispatch } from 'dva'
import { Spin, Empty } from 'antd'
import Sider from './components/Sider'
import { XPagination } from '@/components'
import styles from './index.less'
import { connectState, connectProps } from '@/models/connect'
import { IArticle } from './model'
import Card from './components/Card'
// import ArticleList from './components/ArticleList'

interface IProps extends connectProps {
    authorId: number
    loading: boolean
    articles: {
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

const Home: React.FC<IProps> = ({ articles, authorId, loading }) => {
    const dispatch = useDispatch()
    const [params, setParams] = useState<IParams>(initParams)
    const [categoryId, setCategoryId] = useState<number>(0)
    const { list = [], total = 0 } = articles
    useEffect(() => {
        if (authorId !== 0) {
            getCategories()
            getArticles()
        }
    }, [authorId])

    useEffect(() => {
        if (categoryId) {
            getArticlesByCategory()
        } else {
            getArticles()
        }
    }, [params])

    useEffect(() => {
        if (categoryId) {
            getArticlesByCategory({ page: 1, count: 10 })
        } else {
            getArticles({ page: 1, count: 10 })
        }
        setParams({ page: 1, count: 10 })
    }, [categoryId])

    const handleChange = (page: number, count?: number) => {
        setParams({ page, count })
    }

    const handleSizeChange = (current: number, size: number) => {
        setParams({ page: current, count: size })
    }

    const getArticles = (newParams: IParams = params) => {
        dispatch({ type: 'home/getArticles', payload: { ...newParams, authorId } })
    }

    const getArticlesByCategory = (newParams: IParams = params) => {
        dispatch({ type: 'home/getArticlesByCategory', payload: { ...newParams, categoryId } })
    }

    const getCategories = () => {
        dispatch({ type: 'category/get', payload: { authorId } })
    }

    return (
        <div className={styles.wrapper}>
            <div className={styles.articles_wrapper}>
                <Spin spinning={!!loading}>
                    {list.length === 0 ? <Empty /> : list.map((r) => <Card data={r} key={r.articleId} />)}
                </Spin>
                <XPagination onChange={handleChange} total={total} {...params} onShowSizeChange={handleSizeChange} />
            </div>
            <div className={styles.side_wrapper}>
                <Sider categoryId={categoryId} changeCategoryId={setCategoryId} />
            </div>
        </div>
    )
}

export default connect(({ home, app, loading }: connectState) => ({
    loading: loading.effects['home/getArticles'] || loading.effects['home/getArticlesByCategory'],
    authorId: app.userInfo.authorId,
    articles: home.articles
}))(Home)
