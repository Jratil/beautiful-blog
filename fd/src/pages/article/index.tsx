import React, { useState, useEffect, useMemo, useCallback } from 'react'
// import { router } from 'umi'
import { connect, useDispatch } from 'dva'
import { Icon, Badge, Comment, Form, Input, Button, Avatar, message } from 'antd'
import { useParams } from 'react-router'
import BraftEditor, { EditorState } from 'braft-editor'
import moment from 'moment'
import { connectState, connectProps } from '@/models/connect'
import { IArticle } from './model'
import CommentItem from './components/CommentItem'
import styles from './index.less'
import { ICategory } from '../category/model'
import CollectSVG from 'icons/collection.svg'

interface IProps extends connectProps {
    detail: IArticle
    categories: ICategory[]
}

const { TextArea } = Input

const Page: React.FC<IProps> = ({ detail, categories }) => {
    const { articleId } = useParams()
    const dispatch = useDispatch()
    const [value, setValue] = useState<string>('')
    const [submitting, setSubmitting] = useState<boolean>(false)
    const [comments, setComments] = useState<any>([])
    const { articleTitle, articleContent, categoryId, articleLike, hasLike } = detail
    useEffect(() => {
        dispatch({ type: 'article/get', payload: { articleId } })
        // dispatch({ type: 'category/get', payload: { articleId } })
    }, [])

    const categoryName = useMemo(() => categories.find((r) => r.id === categoryId)?.name, [categories, categoryId])

    const handleChange = useCallback((e) => setValue(e.target.value), [setValue])

    const handleSubmit = () => {
        if (!value) return
        message.info('帅气后端小哥还没有实现评论功能哦~')
        setSubmitting(true)

        setTimeout(() => {
            setSubmitting(false)
            setValue('')
            setComments([
                {
                    author: 'Han Solo',
                    avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
                    content: <p>{value}</p>,
                    datetime: moment().fromNow()
                },
                ...comments
            ])
        }, 1000)
    }

    const Editor = ({ onChange, onSubmit, submitting, value }) => (
        <div>
            <Form.Item>
                <TextArea rows={4} onChange={onChange} value={value} />
            </Form.Item>
            <Form.Item>
                <Button htmlType="submit" loading={submitting} onClick={onSubmit} type="primary">
                    Add Comment
                </Button>
            </Form.Item>
        </div>
    )
    return (
        <div className={styles.view}>
            <div className={styles.article_area}>
                <div className={styles.article_title}>{articleTitle}</div>
                <div className={styles.article_desc}>{categoryName}</div>
                {/* <article></article> */}
                {/* TODO: 后期取消 BraftEditor 展示，改为 article 标签，有利于 SEO */}
                <BraftEditor
                    readOnly
                    controls={[]}
                    value={BraftEditor.createEditorState(articleContent)}
                    contentClassName={styles.article_content}
                />
                <div className={styles.comment_list}>
                    <Comment
                        avatar={
                            <Avatar
                                src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                                alt="Han Solo"
                            />
                        }
                        content={
                            <Editor
                                onChange={handleChange}
                                onSubmit={handleSubmit}
                                submitting={submitting}
                                value={value}
                            />
                        }
                    />
                    {/* TODO: key 不用 index */}
                    {comments.map(({ author, avatar, content }, index) => (
                        <CommentItem name={author} avatarUrl={avatar} content={content} key={index} />
                    ))}
                </div>
            </div>
            <div className={styles.article_suspended_panel}>
                <div>
                    <Badge
                        style={{
                            backgroundColor: hasLike ? '#20a0ff' : '#ffffff',
                            color: hasLike ? '#ffffff' : '#999999'
                        }}
                        count={articleLike}
                        overflowCount={999}
                    >
                        <Icon className={styles.panel_btn} type="like" />
                    </Badge>
                </div>
                <Icon component={CollectSVG} className={styles.panel_btn} />
                <div className={styles.tip}>分享</div>
                <Icon className={styles.panel_btn} type="weibo" />
                <Icon className={styles.panel_btn} type="qq" />
                <Icon className={styles.panel_btn} type="wechat" />
            </div>
            <div className={styles.catalog}>
                <div className={styles.catalog_title}>目录</div>
                <div className={styles.catalog_body}>目录</div>
            </div>
        </div>
    )
}

export default connect(({ article, category }: connectState) => ({
    detail: article.detail,
    categories: category.categories
}))(Page)
