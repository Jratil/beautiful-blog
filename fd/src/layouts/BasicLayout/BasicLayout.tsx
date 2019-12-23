import React from 'react'
import Header from './Header'

const BasicLayout = ({ children, ...restProps }) => {
    console.log(restProps)
    return (
        <>
            <Header></Header>
            {children}
        </>
    )
}

export default BasicLayout
