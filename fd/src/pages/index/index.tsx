import React, { useEffect } from 'react'
import { connect, useDispatch } from 'dva'
import styles from './index.css'

export default function() {
    const dispatch = useDispatch()
    useEffect(() => {
        dispatch({ type: 'home/getArticles', payload: { a: 1 } })
    },[])
    return (
        <div className={styles.normal}>
            <h1>Page index</h1>
        </div>
    )
}
