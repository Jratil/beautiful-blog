import api from '@/services'
import { Effect } from 'dva'
import { Reducer } from 'redux'

const { articleGetById } = api

export interface IArticle {
    articleContent: string
    articleId: number
    articleSubtitle: string
    articleTitle: string
    categoryId: number
    visible: boolean
}

export interface IArticleState {
    detail: IArticle
}

interface IModel {
    namespace: 'article'
    state: IArticleState
    effects: {
        get: Effect
    }
    reducers: {
        updateState: Reducer<IArticleState>
    }
}

const Main: IModel = {
    namespace: 'article',
    state: {
        detail: {
            articleContent: '',
            articleId: 0,
            articleSubtitle: '',
            articleTitle: '',
            categoryId: 0,
            visible: true
        }
    },
    effects: {
        *get({ payload, callback }, { call, put }) {
            const detail = yield call(articleGetById, payload)
            yield put({ type: 'updateState', payload: { detail } })
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
