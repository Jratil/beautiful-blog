import api from '@/services'
import { Effect } from 'dva'
import { Reducer } from 'redux'

const { categoryGet } = api

export interface ICategory {
    categoryId: number
    categoryName: string
    categoryVisible: boolean
    authorId: number
    createTime: number
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

const Category: IModel = {
    namespace: 'category',
    state: {
        categories: []
    },
    effects: {
        *get({ payload }, { call, put }) {
            const categories = yield call(categoryGet, payload)
            yield put({ type: 'updateState', payload: { categories: categories.list } })
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

export default Category
