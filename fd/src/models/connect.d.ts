import { IHomeState } from '@/pages/index/model.ts'
import { ICategoryState } from '@/pages/category/model.ts'
import { IWriteState } from '@/pages/write/model.ts'
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
    category: ICategoryState
    write: IWriteState
}

export interface connectProps {
    form: WrappedFormUtils
}
