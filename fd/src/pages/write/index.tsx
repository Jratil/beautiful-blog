import React, { useState, useEffect } from 'react'
import { connect, useDispatch } from 'dva'
import { Row, Col, Divider, Button, Form, Input, DatePicker, Select } from 'antd'
import BraftEditor, { EditorState } from 'braft-editor'
import 'braft-editor/dist/index.css'
import styles from './index.less'

import { connectState, connectProps } from '@/models/connect'
import { ICategory } from './model'

interface IProps extends connectProps {
    categories: ICategory[]
}

const FormItem = Form.Item
const SelectOption = Select.Option
const Write: React.FC<IProps> = ({ categories, form }) => {
    const dispatch = useDispatch()
    const { getFieldDecorator } = form
    const [value, setValue] = useState<string>('')

    useEffect(() => {
        dispatch({
            type: 'write/getCategory'
        })
    }, [])

    const handleChange = (editorState: EditorState) => {
        console.log(editorState.toRAW())
    }

    const SelectOptions = categories.map(({ id, name }) => (
        <SelectOption key={id} value={id}>
            {name}
        </SelectOption>
    ))

    return (
        <div className={styles.write_wrapper}>
            <div className={styles.editor_wrapper}>
                <BraftEditor
                    value={value}
                    onChange={handleChange}
                    // onSave={this.submitContent}
                />
            </div>
            <div className={styles.sider_wrapper}>
                <div>
                    <Form labelAlign='left' labelCol={{ span: 4 }} wrapperCol={{ span: 20 }}>
                        <FormItem label='标题'>{getFieldDecorator('title')(<Input></Input>)}</FormItem>
                        <FormItem label='分类'>
                            {getFieldDecorator('category', {
                                initialValue: categories[0] && categories[0].id
                            })(<Select>{SelectOptions}</Select>)}
                        </FormItem>
                        <FormItem label='时间'>
                            <DatePicker showTime placeholder='Select Time' />
                        </FormItem>
                    </Form>
                </div>
                <Divider />
                <Button>保存</Button>
                <Button type='primary'>发布</Button>
            </div>
        </div>
    )
}

export default connect(({ write }: connectState) => ({
    categories: write.categories
}))(Form.create()(Write))
