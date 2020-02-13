import React, { useState, useCallback } from 'react'
import { Comment, Form, Button, Input, message } from 'antd'
import { connect, useDispatch } from 'dva'
import { XAvatar } from '@/components'
import { connectState } from '@/models/connect'
import { useParams } from 'react-router'
import { IUserInfo } from '@/models/app'

interface IProps {
    userInfo: IUserInfo
    isSub?: boolean
    parentCommentId?: number
    replyCommentId?: number
    loading: boolean
    closeReply?(): void
}
const { TextArea } = Input
const CommentReply: React.FC<IProps> = ({ userInfo, isSub, parentCommentId, replyCommentId, loading, closeReply }) => {
    const { articleId } = useParams()
    const dispatch = useDispatch()
    const [value, setValue] = useState<string>('')
    const queryComments = () => dispatch({ type: 'article/getComments', payload: { articleId } })
    const handleChange = (e) => setValue(e.target.value)

    const handleSubmit = () => {
        if (!value) return
        const { authorId } = userInfo
        let payload: any = {
            authorId,
            articleId,
            commentLevel: isSub ? 2 : 1,
            content: value
        }
        if (parentCommentId) payload.parentCommentId = parentCommentId
        if (replyCommentId) payload.replyCommentId = replyCommentId
        dispatch({
            type: 'article/addComment',
            payload,
            callback: () => {
                setValue('')
                message.success('发表评论成功')
                queryComments()
                if (closeReply) closeReply()
            }
        })
    }

    const Editor = useCallback(
        ({ onChange, onSubmit, submitting, value }) => (
            <>
                <Form.Item>
                    <TextArea rows={4} onChange={onChange} value={value} />
                </Form.Item>
                <Form.Item>
                    <Button htmlType="submit" loading={submitting} onClick={onSubmit} type="primary">
                        Add Comment
                    </Button>
                </Form.Item>
            </>
        ),
        []
    )

    return (
        <Comment
            avatar={<XAvatar avatarSrc={userInfo.authorAvatar} name={userInfo.authorName} />}
            content={<Editor onChange={handleChange} onSubmit={handleSubmit} submitting={!!loading} value={value} />}
        />
    )
}

export default connect(({ app, loading }: connectState) => ({
    userInfo: app.userInfo,
    loading: loading.effects['article/addComment']
}))(CommentReply)
