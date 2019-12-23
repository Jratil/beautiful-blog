import React from 'react'
import BasicLayout from './BasicLayout'

export const ctx = React.createContext('')

const PATH_WITHOUT_LAYOUT = []

const Layout = ({ location, children }) => {
    const { pathname } = location
    if (PATH_WITHOUT_LAYOUT.includes(pathname)) {
        return <>{children}</>
    }

    return (
        <ctx.Provider value={location.pathname}>
            <BasicLayout> {children} </BasicLayout>
        </ctx.Provider>
    )
}

export default Layout
