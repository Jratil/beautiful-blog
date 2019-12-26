import React from 'react'
import { Card } from 'antd'

const SideCard = ({ children }) => {
    return <Card>{children}</Card>
}

export default React.memo(SideCard)
