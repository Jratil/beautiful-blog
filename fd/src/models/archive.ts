import api from '@/services'
import { Effect } from 'dva'
import { Reducer } from 'redux'

const { archiveGet } = api

export interface IArchive {
    count: number
    archive: string
}

export interface IArchiveState {
    archives: IArchive[]
}

interface IModel {
    namespace: 'archive'
    state: IArchiveState
    effects: {
        get: Effect
    }
    reducers: {
        updateState: Reducer<IArchiveState>
    }
}

const Category: IModel = {
    namespace: 'archive',
    state: {
        archives: []
    },
    effects: {
        *get({ payload }, { call, put }) {
            const archives = yield call(archiveGet, payload)
            yield put({ type: 'updateState', payload: { archives } })
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
