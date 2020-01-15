import { Model, Effect } from 'dva'
import Cookies from 'js-cookie'
import api, { AUTHORIZATION_KEY } from '@/services'
import { router } from 'umi'
import { Reducer } from 'redux'

export interface IUserInfo {
    authorAccount: string
    authorAvatar: string
    authorBirthday: number
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
    reducers: {
        updateState: Reducer
    }
}

const { authQueryByAccount, authLogout } = api

const AppModal: IAppModal = {
    namespace: 'app',
    state: {
        userInfo: {
            authorAccount: '123123@qq.com',
            authorAvatar: '',
            authorBirthday: 1575988696,
            authorGender: 0,
            authorId: 0,
            authorName: 'Micah',
            authorPassword: ''
        }
    },
    effects: {
        *getUserInfo({ payload }, { call, put }) {
            const userInfo = yield call(authQueryByAccount, payload)
            yield put({ type: 'updateState', payload: { userInfo } })
        },
        *logout(_, { call }) {
            yield call(authLogout)
            Cookies.remove(AUTHORIZATION_KEY)
            router.push('/login')
        }
    },
    reducers: {
        updateState(state, { payload }) {
            return {
                ...state,
                ...payload
            }
        }
    },
    subscriptions: {
        setup({ dispatch }) {
            const loginInfo = window.localStorage.getItem('loginInfo')
            dispatch({ type: 'app/getUserInfo', payload: { account: JSON.parse(loginInfo!).account } })
        }
    }
}

export default AppModal
