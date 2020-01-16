import React, { useState, useEffect, useCallback, useMemo } from 'react'
import { connect, useDispatch } from 'dva'
import { Divider, Button, Form, Input, DatePicker, Select, List, Icon, Modal } from 'antd'
import moment, { Moment } from 'moment'
import BraftEditor, { EditorState } from 'braft-editor'
import 'braft-editor/dist/index.css'
import styles from './index.less'
import { useLocalStorageState } from '@umijs/hooks'
import { genHash } from '@/utils/common'

import { connectState, connectProps } from '@/models/connect'
import { ICategory } from '@/models/category'
import { router } from 'umi'
import { useLocation } from 'react-router'
import DelSVG from 'icons/close.svg'
import BrowseSVG from 'icons/browse.svg'

interface IProps extends connectProps {
    categories: ICategory[]
    authorId: number
}

interface IDraft {
    title: string
    content: string
    time: string
    category: number
    saveTime?: string
}

interface IDrafts {
    [x: string]: IDraft
}

const FormItem = Form.Item
const ListItem = List.Item
const SelectOption = Select.Option
const SAVE_KEY = 'draft'
const initDraft: IDraft = {
    title: '',
    content: '',
    time: '',
    category: 0
}
const initDrafts = {}
const Write: React.FC<IProps> = ({ categories, authorId }) => {
    const dispatch = useDispatch()
    const [hash, setHash] = useState<string>('')
    const [value, setValue] = useState<string>('')
    const [draft, setDraft] = useState<IDraft>(initDraft)
    const [drafts, setDrafts] = useLocalStorageState<IDrafts>(SAVE_KEY, initDrafts)
    const location = useLocation()

    // const { run } = useDebounceFn(
    //     () => {
    //         localStorage.setItem(hash, value)
    //     },
    //     [value, hash],
    //     1000
    // )

    useEffect(() => {
        const hash = getHash()
        setHash(hash)
        const tempDraft = drafts && drafts[hash]
        if (tempDraft) {
            // const { content } = tempDraft
            // const newValue = BraftEditor.createEditorState(JSON.parse(content))
            // setValue(newValue)
            setDraft(tempDraft)
        }
    }, [])

    useEffect(() => {
        dispatch({ type: 'category/get', payload: { authorId } })
    }, [authorId])

    useEffect(() => {
        console.log(654)
        //     const { content } = draft
        //     let formatContent
        //     try {
        //         formatContent = JSON.parse(content)
        //     } catch (err) {
        //         //  忽略错误
        //     }
        const newValue = BraftEditor.createEditorState({
            blocks: [
                {
                    key: 'cip29',
                    text: 'awdqweqweqwdaweqsdaweasdaw',
                    type: 'unstyled',
                    depth: 0,
                    inlineStyleRanges: [],
                    entityRanges: [],
                    data: {}
                }
            ],
            entityMap: {}
        })
        // setValue(newValue)
    }, [draft])

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

    const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => setDraft({ ...draft, title: e.target.value })

    const handleCategoryChange = (category: number) => setDraft({ ...draft, category })

    const handleTimeChange = (_timeObj: Moment | null, time: string) => setDraft({ ...draft, time })

    const handleChange = (editorState: EditorState) => {
        const content = editorState.toRAW()
        setValue(content)
        setDraft({ ...draft, content })
    }

    const handleSave = () => {
        setDrafts({ ...drafts, [hash]: { ...draft, saveTime: moment().format('YYYY-MM-DD HH:mm:ss') } })
    }

    const handlePublish = () => {
        const { title, category, content } = draft
        dispatch({
            type: 'write/addArticle',
            payload: {
                authorId,
                articleTitle: title,
                articleContent: content,
                categoryId: category
            },
            callback: () => {
                const tmp = { ...drafts }
                delete tmp[hash]
                setDrafts(tmp)
                router.push('/')
            }
        })
    }

    const SelectOptions = categories.map(({ categoryId, categoryName }) => (
        <SelectOption key={categoryId} value={categoryId}>
            {categoryName}
        </SelectOption>
    ))

    const { time, content, title, category } = draft

    const browseDraft = useCallback((hash: string) => setDraft(drafts[hash]), [drafts])

    const delDraft = useCallback(
        (hash: string) => {
            Modal.confirm({
                title: '确定删除该草稿？',
                onOk: () => {
                    const { [hash]: draftToDel, ...restDrafts } = drafts
                    setDrafts(restDrafts)
                }
            })
        },
        [drafts]
    )

    const listItemExtra = useCallback(
        (hash: string) => (
            <>
                <Icon className={styles.list_icon} component={BrowseSVG} onClick={() => browseDraft(hash)} />
                <Icon className={styles.list_icon} component={DelSVG} onClick={() => delDraft(hash)} />
            </>
        ),
        [browseDraft, delDraft]
    )

    const listItems = useMemo(() => {
        return Object.keys(drafts).map((key) => {
            const { title, saveTime } = drafts[key]
            return (
                <ListItem key={key} extra={listItemExtra(key)}>
                    {`${title ? title : '无标题'}(${saveTime})`}
                </ListItem>
            )
        })
    }, [drafts])

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
                <Form labelAlign="left" labelCol={{ span: 4 }} wrapperCol={{ span: 20 }}>
                    <FormItem label="标题">
                        <Input value={title} onChange={handleTitleChange} />
                    </FormItem>
                    <FormItem label="分类">
                        <Select<number>
                            value={category || (categories[0] && categories[0].categoryId)}
                            onChange={handleCategoryChange}
                        >
                            {SelectOptions}
                        </Select>
                    </FormItem>
                    <FormItem label="时间">
                        <DatePicker
                            showTime
                            placeholder="Select Time"
                            value={(time && moment(time)) || moment()}
                            onChange={handleTimeChange}
                        />
                    </FormItem>
                </Form>
                <Divider />
                <Button onClick={handleSave} style={{ marginRight: 12 }}>
                    保存
                </Button>
                <Button type="primary" onClick={handlePublish}>
                    发布
                </Button>

                {Object.keys(drafts).length > 0 && (
                    <>
                        <Divider>
                            本地已保存
                            <Button type="link">全部删除</Button>
                        </Divider>
                        <List split={false}>{listItems}</List>
                    </>
                )}
            </div>
        </div>
    )
}

export default connect(({ app, category }: connectState) => ({
    authorId: app.userInfo.authorId,
    categories: category.categories
}))(Write)
