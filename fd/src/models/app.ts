import { Model, Effect } from 'dva'
import Cookies from 'js-cookie'
import api, { AUTHORIZATION_KEY } from '@/services'
import { router } from 'umi'

export interface IUserInfo {
    authorAccount: string
    authorAvatar: string
    authorBirthday: string
    authorGender: number
    authorId: number
    authorName: string
    authorPassword: string
}

export interface IAppState {
    userInfo: IUserInfo
}

interface IAppModal extends Model {
    namespace: 'app'
    state: IAppState
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
            authorAccount: '123123@qq.com',
            authorAvatar: '',
            authorBirthday: '2019-02-23',
            authorGender: 0,
            authorId: 0,
            authorName: 'Micah',
            authorPassword: ''
        }
    },
    effects: {
        *getUserInfo({ payload }, { call, put }) {
            const res = yield call(authQueryByAccount, payload)
            console.log(res)
        },
        *logout(_, { call }) {
            yield call(authLogout)
            Cookies.remove(AUTHORIZATION_KEY)
            router.push('/login')
        }
    }
}

export default AppModal
