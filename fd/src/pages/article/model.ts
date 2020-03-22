import api from '@/services'
import { Effect } from 'dva'
import { Reducer } from 'redux'

const {
    articleGetById,
    articleToggleLike,
    articleDelete,
    commentAdd,
    commentQueryByArticleId,
    commentToggleLiked,
    commentQueryChild
} = api

export interface IArticle {
    authorId: number
    articleContent: string
    articleId: number
    articleSubtitle: string
    articleTitle: string
    articleLike: number
    categoryId: number
    visible: boolean
    hasLike: boolean
}

export interface IComment {
    commentId: number
    authorId: number
    authorName: string
    articleId: number
    parentCommentId: number
    parentCommentAuthorId: number
    replyCommentId: number
    replyCommentAuthorId: number
    replyCommentAuthorName: null
    commentLevel: number
    content: string
    deleteStatus: boolean
    praiseNum: number
    topStatus: boolean
    createTime: string
    childCommentList: IComment[]
    hasLike: boolean
    authorAvatar: string
    childCommentTotal: number
}

export interface IArticleState {
    detail: IArticle
    comments: IComment[]
    likedComments: number[]
}

interface IModel {
    namespace: 'article'
    state: IArticleState
    effects: {
        get: Effect
        addComment: Effect
        getComments: Effect
        like: Effect
        commentLike: Effect
        getChildComments: Effect
        delete: Effect
    }
    reducers: {
        updateState: Reducer<IArticleState>
        updateChildComment: Reducer
        updateLike: Reducer
        updateCommentLike: Reducer
    }
}

const article: IModel = {
    namespace: 'article',
    state: {
        detail: {
            authorId: 0,
            articleContent: '',
            articleId: 0,
            articleSubtitle: '',
            articleTitle: '',
            articleLike: 0, //  后端未添加
            categoryId: 0,
            visible: true,
            hasLike: false
        },
        comments: [],
        likedComments: []
    },
    effects: {
        *get({ payload, callback }, { call, put }) {
            const detail = yield call(articleGetById, payload)
            yield put({ type: 'updateState', payload: { detail } })
        },
        *addComment({ payload, callback }, { call, put }) {
            const res = yield call(commentAdd, payload)
            if (res && callback) callback()
        },
        *getComments({ payload, callback }, { call, put }) {
            const res = yield call(commentQueryByArticleId, { ...payload, page: 1, count: 10 })
            yield put({ type: 'updateState', payload: { comments: res.list } })
        },
        *like({ payload, callback }, { call, put }) {
            const res = yield call(articleToggleLike, payload)
            yield put({ type: 'updateLike', payload: { count: typeof res === 'boolean' ? 0 : res } })
        },
        *commentLike({ payload, callback }, { call, put }) {
            const res = yield call(commentToggleLiked, payload)
            yield put({ type: 'updateCommentLike', payload: { ...payload, count: typeof res === 'boolean' ? 0 : res } })
        },
        *getChildComments({ payload, callback }, { call, put }) {
            const { commentId } = payload
            const res = yield call(commentQueryChild, payload)
            yield put({ type: 'updateChildComment', payload: { commentId, list: res.list } })
        },
        *delete({ payload, callback }, { call, put }) {
            const res = yield call(articleDelete, payload)
            yield put({ type: 'updateLike', payload })
            if (res && callback) callback()
        }
    },
    reducers: {
        updateState(state, { payload }) {
            return {
                ...state,
                ...payload
            }
        },
        updateChildComment(state, { payload }) {
            const { commentId, list } = payload
            const { comments } = state
            comments.forEach((r: any) => {
                if (r.commentId === commentId) r.childCommentList = list
            })
            return {
                ...state,
                comments: [...comments]
            }
        },
        updateLike(state, { payload }) {
            const { count } = payload
            const { detail } = state
            const { hasLike } = detail
            return {
                ...state,
                detail: {
                    ...detail,
                    hasLike: !hasLike,
                    articleLike: count
                }
            }
        },
        updateCommentLike(state, { payload }) {
            const { commentId, count } = payload
            const { comments } = state
            const signLike = () => {
                for (let r of comments) {
                    if (r.commentId === commentId) {
                        r.praiseNum = count
                        return
                    }
                    if (r.childCommentList.length > 0) {
                        for (let s of r.childCommentList) {
                            if (s.commentId === commentId) {
                                s.praiseNum = count
                                return
                            }
                        }
                    }
                }
            }
            signLike()
            return {
                ...state
            }
        }
    }
}

export default article
