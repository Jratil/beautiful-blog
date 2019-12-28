import { Model, Effect } from 'dva'
import api from '@/services'
import { router } from 'umi'

export interface ICategory {
    id: number
    name: string
}

export interface IWriteState {
    categories: ICategory[]
}

interface IModel extends Model {
    namespace: 'write'
    state: any
    effects: {
        getCategory: Effect
        addArticle: Effect
    }
}

const { categoryGet, articleAdd } = api
const WriteModel: IModel = {
    namespace: 'write',
    state: {
        categories: []
    },
    effects: {
        *getCategory({ payload }, { call, put }) {
            const categories = yield call(categoryGet, payload)
            yield put({ type: 'updateState', payload: { categories } })
        },
        *addArticle({ payload, callback }, { call }) {
            const res = yield call(articleAdd, payload)
            if (res && callback) callback()
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

export default WriteModel
