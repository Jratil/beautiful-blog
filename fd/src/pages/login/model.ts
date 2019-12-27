import { Model, Effect } from 'dva'
import api from '@/services'
import { router } from 'umi'

interface IModel extends Model {
    namespace: 'login'
    state: any
    effects: {
        login: Effect
    }
}

const { authLogin } = api
const LoginModel: IModel = {
    namespace: 'login',
    state: {},
    effects: {
        *login({ payload, callback }, { call, put }) {
            const res = yield call(authLogin, payload)
            if (res) {
                router.push('/')
                if (callback) callback()
            }
        }
    }
}

export default LoginModel
