import React from 'react'
import { Avatar } from 'antd'

interface IProps {
    avatarSrc: string
    name: string
}

const XPagination: React.FC<IProps> = ({ avatarSrc, name }) => {
    return avatarSrc ? (
        <Avatar src={avatarSrc} style={{ cursor: 'pointer' }} />
    ) : (
        <Avatar style={{ cursor: 'pointer' }}>{name.slice(0, 1)}</Avatar>
    )
}

export default XPagination
