import api from '@/services'
import { Effect } from 'dva'
import { Reducer } from 'redux'

const { articleGetById } = api

export interface IArticle {
    articleContent: string
    articleId: number
    articleSubtitle: string
    articleTitle: string
    articleLike: number
    categoryId: number
    visible: boolean
    hasLike: boolean //TODO 字段待添加
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
            articleLike: 63,
            categoryId: 0,
            visible: true,
            hasLike: false
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
