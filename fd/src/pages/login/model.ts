import { Model, Effect } from 'dva'
import api from '@/services'

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
            // console.log(payload)
            yield call(authLogin, payload)
        }
    }
}

export default LoginModel
