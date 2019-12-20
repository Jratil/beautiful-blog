import React from 'react'
import BasicLayout from './BasicLayout'

const PATH_WITHOUT_LAYOUT = []

const Layout = ({ location, children }) => {
    const { pathname } = location
    if (PATH_WITHOUT_LAYOUT.includes(pathname)) {
        return <>{children}</>
    }

    return <BasicLayout> {children} </BasicLayout>
}

export default Layout
