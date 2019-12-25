import api from '@/services'
import { Effect } from 'dva'
import { Reducer } from 'redux'

const { articleGet } = api

interface IArticle {
    title: string
    desc: string
    like: number
    comments: number
    read: number
    author_id: number
    author_name: string
}

interface IHomeState {
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
            // yield put({ articles })
            console.log(articles)
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
