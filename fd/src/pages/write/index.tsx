import React, { useState, useEffect, useCallback, useMemo } from 'react'
import { connect, useDispatch } from 'dva'
import { Divider, Button, Form, Input, DatePicker, Select, List, Icon, Modal } from 'antd'
import moment, { Moment } from 'moment'
import BraftEditor, { EditorState, ExtendControlType } from 'braft-editor'
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
import showdown from 'showdown'
import { IArticle } from '../article/model'
const converter = new showdown.Converter()

type IType = 'add' | 'edit'

interface IProps extends connectProps {
    categories: ICategory[]
    authorId: number
    detail: IArticle
}

interface IDraft {
    title: string
    content: string
    time: string
    category: number
    saveTime?: string
}

type IDraftKey = keyof IDraft

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
const Write: React.FC<IProps> = ({ categories, detail, authorId }) => {
    const dispatch = useDispatch()
    const [hash, setHash] = useState<string>('')
    const [value, setValue] = useState<string>('')
    const [draft, setDraft] = useState<IDraft>(initDraft)
    const [drafts, setDrafts] = useLocalStorageState<IDrafts>(SAVE_KEY, {})
    const location = useLocation()
    const { articleId } = location.query

    const MD2BF: ExtendControlType = {
        key: 'custom-md2bf',
        type: 'button',
        title: '解析 markdown 文件',
        text: (
            <div style={{ width: 14 }}>
                <input type="file" id="file" name="file" accept=".md" style={{ width: 0, height: 0 }} />
                <Icon type="upload" />
            </div>
        ),
        onClick: () => {
            const file = document.getElementById('file')
            file?.click()

            file!.onchange = ({ target }) => {
                const reader = new FileReader()
                reader.onload = (res) => {
                    const transformHtml = converter.makeHtml(reader.result)
                    setValue(BraftEditor.createEditorState(transformHtml))
                }
                reader.readAsText(target.files[0], 'utf-8')
            }
        }
    }

    // const { run } = useDebounceFn(
    //     () => {
    //         localStorage.setItem(hash, value)
    //     },
    //     [value, hash],
    //     1000
    // )

    useEffect(() => {
        if (articleId) {
            //  编辑文章
            dispatch({ type: 'article/get', payload: { articleId } })
        } else {
            const hash = getHash()
            setHash(hash)
        }
    }, [])

    useEffect(() => {
        const tempDraft = drafts?.[hash]
        if (tempDraft) {
            const { content } = tempDraft
            const newValue = BraftEditor.createEditorState(content)
            setValue(newValue)
            setDraft(tempDraft)
        }
    }, [hash, drafts])

    useEffect(() => {
        const { articleContent, articleTitle, categoryId } = detail
        setDraft({ ...draft, title: articleTitle, category: categoryId })
        setValue(BraftEditor.createEditorState(articleContent))
    }, [detail])

    useEffect(() => {
        if (authorId !== 0) dispatch({ type: 'category/get', payload: { authorId } })
    }, [authorId])

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
        const callback = () => {
            const tmp = { ...drafts }
            delete tmp[hash]
            setDrafts(tmp)
            //TODO  可以返回文章id ，跳转到文章详情页 ，提升用户体验
            router.push('/')
        }
        dispatch({
            type: 'write/addArticle',
            payload: {
                authorId,
                articleTitle: title,
                articleContent: content,
                categoryId: category
            },
            callback
        })
    }

    const handleEdit = () => {
        const { title, category, content } = draft
        const callback = () => {
            // const tmp = { ...drafts }
            // delete tmp[hash]
            // setDrafts(tmp)
            //TODO  可以返回文章id ，跳转到文章详情页 ，提升用户体验
            router.push('/')
        }
        dispatch({
            type: 'write/edit',
            payload: {
                authorId,
                articleTitle: title,
                articleContent: content,
                categoryId: category,
                articleId: detail.articleId
            },
            callback
        })
    }

    const hasSaveDraft = () => {
        const curDraft = drafts[hash]
        if (!curDraft) return false
        return (Object.keys(draft) as IDraftKey[]).every((key) => key === 'saveTime' || draft[key] === curDraft[key])
    }

    const SelectOptions = useMemo(
        () =>
            categories.map(({ categoryId, categoryName }) => (
                <SelectOption key={categoryId} value={categoryId}>
                    {categoryName}
                </SelectOption>
            )),
        [categories]
    )

    const browseDraft = (hash: string) => {
        setHash(hash)
        router.replace(`/write?hash=${hash}`)
    }

    //  函数重载
    //  当 hash 为 string 删除该指定草稿
    //  当 hash 为 boolean 且为 true 时，删除全部草稿
    const delDraft = (hash: string | boolean) => {
        const isDelAllDrafts = typeof hash === 'boolean' && hash
        const modalTitle = isDelAllDrafts ? '是否删除全部草稿？' : '是否删除该草稿？'
        Modal.confirm({
            title: modalTitle,
            onOk: () => {
                if (isDelAllDrafts) {
                    setDrafts({})
                } else {
                    const { [hash as string]: draftToDel, ...restDrafts } = drafts
                    setDrafts(restDrafts)
                }
            }
        })
    }

    const listItemExtra = useCallback(
        (hash: string) => (
            <>
                <Icon
                    className={styles.list_icon}
                    component={BrowseSVG}
                    onClick={() => browseDraft(hash)}
                    title="查看"
                />
                <Icon className={styles.list_icon} component={DelSVG} onClick={() => delDraft(hash)} title="删除" />
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

    const { time, title, category } = draft

    return (
        <div className={styles.write_wrapper}>
            <div className={styles.editor_wrapper}>
                <BraftEditor
                    value={value}
                    onChange={handleChange}
                    extendControls={['separator', MD2BF]}
                    className={styles.bf_container}
                    contentClassName={styles.bf_content}
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
                {!articleId && (
                    <Button onClick={handleSave} style={{ marginRight: 12 }}>
                        保存
                    </Button>
                )}
                <Button type="primary" onClick={articleId ? handleEdit : handlePublish}>
                    {articleId ? '完成' : '发布'}
                </Button>

                {Object.keys(drafts).length > 0 && (
                    <>
                        <Divider>
                            本地已保存
                            <Button type="link" onClick={() => delDraft(true)}>
                                全部删除
                            </Button>
                        </Divider>
                        <List split={false}>{listItems}</List>
                    </>
                )}
            </div>
        </div>
    )
}

export default connect(({ app, article, category }: connectState) => ({
    authorId: app.userInfo.authorId,
    detail: article.detail,
    categories: category.categories
}))(Write)
