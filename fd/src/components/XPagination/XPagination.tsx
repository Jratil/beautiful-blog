import React from 'react'
import { Pagination } from 'antd'
import { PaginationProps } from 'antd/lib/pagination'

const XPagination: React.FC<PaginationProps> = ({ total, ...restProps }) => {
    const showTotal = (total: number) => `共 ${total} 条`
    return (
        <Pagination
            {...restProps}
            showQuickJumper
            showSizeChanger
            showTotal={showTotal}
        />
    )
}

export default XPagination
