import api from '@/services'
import { Effect } from 'dva'
import { Reducer } from 'redux'

const { categoryGet } = api


export interface ICategory {
    id: number
    name: string
}

export interface ICategoryState {
    categories: ICategory[]
}

interface IModel {
    namespace: 'category'
    state: ICategoryState
    effects: {
        get: Effect
    }
    reducers: {
        updateState: Reducer<ICategoryState>
    }
}

const Main: IModel = {
    namespace: 'category',
    state: {
        categories: []
    },
    effects: {
        *get({ payload }, { call, put }) {
            const categories = yield call(categoryGet, payload)
            yield put({ type: 'updateState', payload: { categories } })
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
