import { Model, Effect } from 'dva'
import api from '@/services'

interface IModel extends Model {
    namespace: 'registry'
    state: any
    effects: {
        getVerifyCode: Effect
        registry: Effect
    }
}

const { authRegistry, smsSend } = api
const LoginModel: IModel = {
    namespace: 'registry',
    state: {},
    effects: {
        *getVerifyCode({ payload, callback }, { call }) {
            const res = yield call(smsSend, { ...payload, type: 0 })
            if (res && callback) callback()
        },
        *registry({ payload, callback }, { call }) {
            const res = yield call(authRegistry, payload)
            if (res && callback) callback()
        }
    }
}

export default LoginModel
