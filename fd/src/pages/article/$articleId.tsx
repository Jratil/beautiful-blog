import React, { useState, useEffect, useMemo } from 'react'
import { router } from 'umi'
import { connect, useDispatch } from 'dva'
import { useParams } from 'react-router'
import BraftEditor, { EditorState } from 'braft-editor'
import { connectState, connectProps } from '@/models/connect'
import { IArticle } from './model'
import styles from './index.less'
import { ICategory } from '../category/model'

interface IProps extends connectProps {
    detail: IArticle
    categories: ICategory[]
}

const Page: React.FC<IProps> = ({ detail, categories }) => {
    const { articleId } = useParams()
    const dispatch = useDispatch()
    const { articleTitle, articleContent, categoryId } = detail
    useEffect(() => {
        dispatch({ type: 'article/get', payload: { articleId } })
        dispatch({ type: 'category/get', payload: { articleId } })
    }, [])

    const categoryName = useMemo(() => categories.find(r => r.id === categoryId)?.name, [categories, categoryId])

    return (
        <div className={styles.article_wrapper}>
            <div className={styles.article_title}>{articleTitle}</div>
            <div className={styles.article_desc}>{categoryName}</div>
            <BraftEditor
                readOnly
                controls={[]}
                value={BraftEditor.createEditorState(articleContent)}
                contentClassName={styles.article_content}
            ></BraftEditor>
        </div>
    )
}

export default connect(({ article, category }: connectState) => ({ detail: article.detail, categories: category.categories }))(Page)
