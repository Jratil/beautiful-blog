import React from 'react'
import { Pagination } from 'antd'
import { PaginationProps } from 'antd/lib/pagination'

const pageSizeOptions = ['10', '20', '30']

const XPagination: React.FC<PaginationProps> = ({ total, ...restProps }) => {
    const showTotal = () => `共 ${total} 条`

    return <Pagination showQuickJumper showSizeChanger showTotal={showTotal} total={total} pageSizeOptions={pageSizeOptions} {...restProps} />
}

export default XPagination
