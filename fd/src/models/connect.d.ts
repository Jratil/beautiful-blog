import { IHomeState } from '@/pages/index/model.ts'
import { WrappedFormUtils } from 'antd/lib/form/Form'

export interface Loading {
    global: boolean
    effects: { [key: string]: boolean }
    models: {
        menu?: boolean
    }
}

export interface connectState {
    loading: Loading
    home: IHomeState
}

export interface connectProps {
    form: WrappedFormUtils
}
