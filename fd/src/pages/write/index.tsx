import React, { useState, useEffect } from 'react'
import { connect, useDispatch } from 'dva'
import { Row, Col, Divider, Button, Form, Input, DatePicker, Select } from 'antd'
import moment from 'moment'
import BraftEditor, { EditorState } from 'braft-editor'
import 'braft-editor/dist/index.css'
import styles from './index.less'
import { useLocalStorageState, useDebounceFn } from '@umijs/hooks'
import { genHash } from '@/utils/common'

import { connectState, connectProps } from '@/models/connect'
import { ICategory } from './model'
import { router } from 'umi'

interface IProps extends connectProps {
    categories: ICategory[]
}

interface IDraft {
    title: string
    content: string
    time: string
    category: number
}

interface IDrafts {
    [x: string]: IDraft
}

const FormItem = Form.Item
const SelectOption = Select.Option
const SAVE_KEY = 'draft'
const initDraft: IDraft = {
    title: '',
    content: '',
    time: '',
    category: 0
}
const initDrafts = {}
const Write: React.FC<IProps> = ({ categories, form, location }) => {
    const dispatch = useDispatch()
    const { getFieldDecorator, validateFields } = form
    const [hash, setHash] = useState<string>('')
    const [value, setValue] = useState<string>('')
    const [draft, setDraft] = useState<IDraft>(initDraft)
    const [drafts, setDrafts] = useLocalStorageState<IDrafts>(SAVE_KEY, initDrafts)

    // const { run } = useDebounceFn(
    //     () => {
    //         localStorage.setItem(hash, value)
    //     },
    //     [value, hash],
    //     1000
    // )

    useEffect(() => {
        dispatch({
            type: 'write/getCategory'
        })
        const hash = getHash()
        setHash(hash)
        const tempDraft = drafts && drafts[hash]
        if (tempDraft) {
            const { title, content, time } = tempDraft
            const newValue = BraftEditor.createEditorState(JSON.parse(content))
            setValue(newValue)
            setDraft(tempDraft)
        }
    }, [])

    const getHash = () => {
        const { pathname, query } = location
        const { hash } = query
        if (!hash) {
            const newHash = genHash()
            setHash(newHash)
            router.replace(`${pathname}?hash=${newHash}`)
            return newHash
        }
        return hash
    }

    const handleChange = (editorState: EditorState) => {
        const content = editorState.toRAW()
        setValue(content)
        setDraft({ ...draft, content })
    }

    const handleSave = () => {
        setDrafts({ ...drafts, [hash]: draft })
    }

    const handlePublish = () => {
        validateFields((err, values) => {
            if (err) return
            dispatch({
                type: 'write/addArticle',
                payload: { ...values, content: value },
                callback: () => {
                    const tmp = { ...drafts }
                    delete tmp[hash]
                    setDrafts(tmp)
                    router.push('/')
                }
            })
        })
    }

    const SelectOptions = categories.map(({ id, name }) => (
        <SelectOption key={id} value={id}>
            {name}
        </SelectOption>
    ))

    const { time, content, title, category } = draft

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
                    <Form labelAlign="left" labelCol={{ span: 4 }} wrapperCol={{ span: 20 }}>
                        <FormItem label="标题">
                            {getFieldDecorator('title', {
                                initialValue: title
                            })(<Input></Input>)}
                        </FormItem>
                        <FormItem label="分类">
                            {getFieldDecorator('category', {
                                initialValue: category || (categories[0] && categories[0].id)
                            })(<Select>{SelectOptions}</Select>)}
                        </FormItem>
                        <FormItem label="时间">
                            {getFieldDecorator('time', {
                                initialValue: time || moment()
                            })(<DatePicker showTime placeholder="Select Time" />)}
                        </FormItem>
                    </Form>
                </div>
                <Divider />
                <Button onClick={handleSave} style={{ marginRight: 8 }}>
                    保存
                </Button>
                <Button type="primary" onClick={handlePublish}>
                    发布
                </Button>
            </div>
        </div>
    )
}

export default connect(({ write }: connectState) => ({
    categories: write.categories
}))(Form.create()(Write))
