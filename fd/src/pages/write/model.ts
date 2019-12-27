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
    }
}

const { categoryGet } = api
const WriteModel: IModel = {
    namespace: 'write',
    state: {
        categories: []
    },
    effects: {
        *getCategory({ payload, callback }, { call, put }) {
            const categories = yield call(categoryGet, payload)
            yield put({ type: 'updateState', payload: { categories } })
            // if (res) {
            // router.push('/')
            // if (callback) callback()
            // }
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
