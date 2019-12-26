import api from '@/services'
import { Effect } from 'dva'
import { Reducer } from 'redux'

const { articleGet } = api

export interface IArticle {
    articleId: number
    articleTitle: string
    articleSubtitle: string
    articleLike: number
    comments: number
    read: number
    author_id: number
    authorName: string
    lastUpdate: string
}

export interface IHomeState {
    articles: IArticle[]
}

interface IModel {
    namespace: 'home'
    state: IHomeState
    effects: {
        getArticles: Effect
    }
    reducers: {
        updateState: Reducer<IHomeState>
    }
}

const Main: IModel = {
    namespace: 'home',
    state: {
        articles: []
    },
    effects: {
        *getArticles({ payload }, { call, put }) {
            const articles = yield call(articleGet, payload)
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
