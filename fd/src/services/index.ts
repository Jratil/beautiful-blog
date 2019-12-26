import axios from 'axios'
import { notification } from 'antd'
import api from './api'

const instance = axios.create({
    // baseURL: 'http://api.jratil.co:8866/'
})

const codeMessage = {
    200: '服务器成功返回请求的数据',
    201: '新建或修改数据成功',
    202: '一个请求已经进入后台排队（异步任务）',
    204: '删除数据成功',
    400: '请求有错误，服务器没有进行新建或修改数据的操作',
    401: '用户没有权限',
    403: '用户得到授权，但是访问是被禁止的',
    404: '请求地址不存在',
    406: '请求的格式不可得',
    410: 'license已失效', // 和 410 的本意不同。410在本项目中认为 license 已失效，需要重新授权
    422: '当创建一个对象时，发生一个验证错误',
    500: '服务器发生错误，请检查服务器',
    502: '网关错误',
    503: '服务不可用，服务器暂时过载或维护',
    504: '网关超时'
}

instance.interceptors.response.use(
    res => res.data,
    err => {
        const { status, statusText } = err.response
        const errorText = codeMessage[status] || statusText

        notification.error({
            message: `请求错误 ${status}`,
            description: errorText
        })

        // const error = new Error(errorText)
        // error.name = `${status} \n ${res}`
        // throw error
    }
)

const gen = (params: string) => {
    const [method, url] = params.split(' ')

    return (opt: any) => {
        const options = method === 'GET' ? { params: opt } : { data: opt }
        return instance({ method, url, ...options })
    }
}

const APIFunction: any = {}
for (const key in api) {
    if (api.hasOwnProperty(key)) {
        APIFunction[key] = gen(api[key])
    }
}

export default APIFunction
