import { Model, Effect } from 'dva'
import Cookies from 'js-cookie'
import api, { AUTHORIZATION_KEY } from '@/services'

interface IAppModal extends Model {
    namespace: 'app'
    state: {
        userInfo: {
            authorAccount: string
            authorAvatar: string
            authorBirthday: string
            authorGender: number
            authorId: number
            authorName: string
            authorPassword: string
        }
    }
    effects: {
        getUserInfo: Effect
        logout: Effect
    }
}

const { authQueryByAccount, authLogout } = api

const AppModal: IAppModal = {
    namespace: 'app',
    state: {
        userInfo: {
            authorAccount: '',
            authorAvatar: '',
            authorBirthday: '',
            authorGender: 0,
            authorId: 0,
            authorName: '',
            authorPassword: ''
        }
    },
    effects: {
        *getUserInfo({ payload }, { call, put }) {
            const res = yield call(authQueryByAccount, payload)
            console.log(res)
        },
        *logout({ payload }, { call, put }) {
            yield call(authLogout)
            Cookies.remove(AUTHORIZATION_KEY)
        }
    }
}

export default AppModal
