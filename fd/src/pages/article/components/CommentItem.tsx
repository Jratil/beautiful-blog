import React, { useState } from 'react'
import { Comment, Icon, Button } from 'antd'
import { useDispatch } from 'dva'
import { XAvatar } from '@/components'
import styles from './commentItem.less'
import moment from 'moment'
import CommentReply from './CommentReply'
import { IComment } from '../model'

interface IProps {
    data: IComment
    isSub?: boolean
    parentCommentId?: number
}

const CommentItem: React.FC<IProps> = ({ data, isSub, parentCommentId }) => {
    const {
        commentId,
        authorName,
        createTime,
        praiseNum,
        content,
        childCommentList,
        hasLike,
        articleId,
        authorAvatar,
        replyCommentAuthorName,
        childCommentTotal
    } = data
    const dispatch = useDispatch()
    const [isReplay, setIsReply] = useState<boolean>(false)
    const [page, setPage] = useState<number>(1)
    const handleReply = () => setIsReply(!isReplay)
    const handleLike = () =>
        dispatch({
            type: 'article/commentLike',
            payload: { commentId },
            callback: () => {
                dispatch({ type: 'article/getComments', payload: { articleId } })
            }
        })
    const handleMore = () => {
        dispatch({
            type: 'article/getChildComments',
            payload: {
                commentId: parentCommentId,
                page,
                count: 10
            },
            callback: () => setPage(page + 1)
        })
    }
    return (
        <Comment
            actions={[
                <span key="time">{moment(createTime).format('YYYY-MM-DD HH:mm:ss')}</span>,
                <span key="like" onClick={handleLike}>
                    <Icon
                        type="like"
                        theme={hasLike ? 'filled' : 'outlined'}
                        className={hasLike ? styles.like_active : null}
                    />
                    {praiseNum > 0 && <span>{praiseNum}</span>}
                </span>,
                <span key="comment-nested-reply-to" onClick={handleReply}>
                    回复
                </span>
            ]}
            author={<a>{authorName}</a>}
            avatar={<XAvatar name={authorName} avatarSrc={authorAvatar} />}
            content={<p>{isSub ? `回复 ${replyCommentAuthorName}： ${content}` : content}</p>}
        >
            {isReplay && (
                <CommentReply
                    isSub={true}
                    parentCommentId={parentCommentId}
                    replyCommentId={commentId}
                    closeReply={() => setIsReply(false)}
                />
            )}
            {childCommentList?.map((data) => (
                <CommentItem data={data} key={data.commentId} isSub={true} parentCommentId={parentCommentId} />
            ))}
            {!isSub && childCommentTotal > childCommentList.length && (
                <Button type="link" style={{ margin: '0 0 16px 286px' }} onClick={handleMore}>
                    查看更多
                </Button>
            )}
        </Comment>
    )
}

export default CommentItem
