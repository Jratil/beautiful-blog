import { Model, Effect } from 'dva'
import api from '@/services'
import { Reducer } from 'redux'

export interface IWriteState {}

interface IModel extends Model {
    namespace: 'write'
    state: any
    effects: {
        addArticle: Effect
    }
    reducers: {
        updateState: Reducer
    }
}

const { articleAdd } = api
const WriteModel: IModel = {
    namespace: 'write',
    state: {},
    effects: {
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
