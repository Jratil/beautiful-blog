import React from 'react'
import { Card, Select, Divider } from 'antd'
import { ICategory } from '@/pages/category/model'

interface IProps {
    categories: ICategory[]
}

const SelectOption = Select.Option
const SideOptions: React.FC<IProps> = ({ categories }) => {
    const options = categories.map(({ id, name }) => (
        <SelectOption key={id} value={id}>
            {name}
        </SelectOption>
    ))
    return (
        <Card title="分类" style={{ marginBottom: 20 }}>
            <Select>{options}</Select>
        </Card>
    )
}

export default React.memo(SideOptions)
