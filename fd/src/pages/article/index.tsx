import React, { useEffect, useMemo } from 'react'
// import { router } from 'umi'
import { connect, useDispatch } from 'dva'
import { Icon, Badge } from 'antd'
import { useParams } from 'react-router'
import BraftEditor, { EditorState } from 'braft-editor'
import moment from 'moment'
import { connectState, connectProps } from '@/models/connect'
import { IArticle, IComment } from './model'
import CommentItem from './components/CommentItem'
import styles from './index.less'
import { ICategory } from '../category/model'
import CollectSVG from 'icons/collection.svg'
import CommentReply from './components/CommentReply'
import classnames from 'classnames'

interface IProps extends connectProps {
    detail: IArticle
    comments: IComment[]
    categories: ICategory[]
    likedComments: number[]
}

const Page: React.FC<IProps> = ({ detail, categories, comments, likedComments }) => {
    const { articleId } = useParams()
    const dispatch = useDispatch()
    const { articleTitle, articleContent, categoryId, articleLike, hasLike } = detail
    useEffect(() => {
        dispatch({ type: 'article/get', payload: { articleId } })
        dispatch({ type: 'article/getLikedComments', payload: { articleId } })
        queryComments()
    }, [])

    const queryComments = () => dispatch({ type: 'article/getComments', payload: { articleId } })

    const categoryName = useMemo(() => categories.find((r) => r.id === categoryId)?.name, [categories, categoryId])

    const handleLike = () => {
        dispatch({ type: 'article/like', payload: { articleId } })
    }

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
                    <CommentReply />
                    {comments.map((item) => {
                        return <CommentItem data={item} key={item.commentId} parentCommentId={item.commentId} />
                    })}
                </div>
            </div>
            <div className={styles.article_suspended_panel}>
                <div onClick={handleLike}>
                    <Badge
                        style={{
                            backgroundColor: hasLike ? '#20a0ff' : '#ffffff',
                            color: hasLike ? '#ffffff' : '#999999'
                        }}
                        count={articleLike}
                        overflowCount={999}
                    >
                        <Icon
                            className={classnames(styles.panel_btn, hasLike ? styles.like_active : '')}
                            type="like"
                            theme={hasLike ? 'filled' : 'outlined'}
                        />
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
    comments: article.comments,
    likedComments: article.likedComments,
    categories: category.categories
}))(Page)
