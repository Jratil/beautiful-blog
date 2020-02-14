import { Model, Effect } from 'dva'
import api from '@/services'

interface IModel extends Model {
    namespace: 'user'
    state: any
    effects: {
        update: Effect
    }
}

const { authUpdate } = api
const UserModel: IModel = {
    namespace: 'user',
    state: {},
    effects: {
        *update({ payload, callback }, { call, put }) {
            const res = yield call(authUpdate, payload)
            if (res && callback) callback()
        }
    }
}

export default UserModel
