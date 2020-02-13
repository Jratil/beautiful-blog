import { IAppState } from '@/models/app.ts'
import { ICategoryState } from '@/models/category'
import { IArchiveState } from '@/models/archive'
import { IArticleState } from '@/pages/article/model'
import { IHomeState } from '@/pages/index/model.ts'
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
    app: IAppState
    article: IArticleState
    archive: IArchiveState
    home: IHomeState
    category: ICategoryState
    write: IWriteState
}

export interface connectProps {
    form: WrappedFormUtils
}
