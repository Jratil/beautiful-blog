import api from '@/services'
import { Effect } from 'dva'
import { Reducer } from 'redux'

const { articleGet, articleGetByCategory, articleGetByArchive } = api

export interface IArticle {
    articleId: number
    articleTitle: string
    articleSubtitle: string
    articleLike: number
    comments: number
    read: number
    author_id: number
    authorName: string
    createTime: number
    lastUpdate: string
    hasLike: boolean
}

export interface IHomeState {
    articles: { total: number; list: IArticle[] }
}

interface IModel {
    namespace: 'home'
    state: IHomeState
    effects: {
        getArticles: Effect
        getArticlesByCategory: Effect
        getArticlesByArchive: Effect
    }
    reducers: {
        updateState: Reducer<IHomeState>
    }
}

const Main: IModel = {
    namespace: 'home',
    state: {
        articles: { total: 0, list: [] }
    },
    effects: {
        *getArticles({ payload }, { call, put }) {
            const articles = yield call(articleGet, payload)
            yield put({ type: 'updateState', payload: { articles } })
        },
        *getArticlesByCategory({ payload }, { call, put }) {
            const articles = yield call(articleGetByCategory, payload)
            yield put({ type: 'updateState', payload: { articles } })
        },
        *getArticlesByArchive({ payload }, { call, put }) {
            const articles = yield call(articleGetByArchive, payload)
            yield put({ type: 'updateState', payload: { articles } })
        }
    },
    reducers: {
        updateState(state, { payload }) {
            return {
                ...state,
                ...payload
            }
        }
    }
}

export default Main
