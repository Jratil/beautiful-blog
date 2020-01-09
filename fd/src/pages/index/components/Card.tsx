import React from 'react'
import styles from '../index.less'
import { Icon } from 'antd'
import { Link } from 'umi'
import { IArticle } from '../model'

interface IProps {
    data: IArticle
}

const Card: React.FC<IProps> = ({ data }) => {
    const { articleTitle, articleSubtitle, articleId, authorName, articleLike, lastUpdate } = data
    return (
        <Link to={`/article/${articleId}`} className={styles.article_wrapper}>
            <div className={styles.card_title}>{articleTitle}</div>
            <div className={styles.card_subtitle}>{articleSubtitle}</div>
            <div className={styles.card_footer}>
                <span className={styles.fl}>
                    <Icon type='user' className={styles.card_icon} />
                    {authorName}
                </span>
                <span className={styles.fl}>
                    <Icon type='like' className={styles.card_icon} />
                    {articleLike}
                </span>
                <span className={styles.fr}>
                    <Icon type='history' className={styles.card_icon} />
                    {lastUpdate}
                </span>
            </div>
        </Link>
    )
}

export default React.memo(Card)
