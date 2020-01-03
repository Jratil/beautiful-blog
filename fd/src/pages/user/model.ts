// import { Model, Effect } from 'dva'
// import api from '@/services'

interface IModel extends Model {
    namespace: 'user'
    state: any
    // effects: {
    //     getVerifyCode: Effect
    //     registry: Effect
    // }
}

// const { authRegistry, emailSend } = api
const LoginModel: IModel = {
    namespace: 'user',
    state: {},
    // effects: {
    //     *getVerifyCode({ payload, callback }, { call, put }) {
    //         const res = yield call(emailSend, payload)
    //         if (res && callback) callback()
    //     },
    //     *registry({ payload, callback }, { call, put }) {
    //         const res = yield call(authRegistry, payload)
    //         if (res && callback) callback()
    //     }
    // }
}

export default LoginModel
