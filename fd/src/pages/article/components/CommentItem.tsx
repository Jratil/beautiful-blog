import React from 'react'
import { Avatar, Comment } from 'antd'

interface IProps {
    name: string
    avatarUrl: string
    content: string
    children?: string | React.ReactNode
}

const CommentItem: React.FC<IProps> = ({ name, avatarUrl, content, children }) => {
    return (
        <Comment
            actions={[<span key='comment-nested-reply-to'>回复</span>]}
            author={<a>{name}</a>}
            avatar={<Avatar src={avatarUrl} alt={name} />}
            content={<p>{content}</p>}
        >
            {children}
        </Comment>
    )
}

export default CommentItem
