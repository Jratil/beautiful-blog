import { useCallback, useState } from 'react'

/**
 * 提供类组件中类似 this.setState 方法的钩子
 * @param initState 初始化 State
 * @returns [state, setState]
 */
function useSetState<T>(initState: T = {} as T): [T, (patch: Partial<T> | ((prevState: T) => Partial<T>)) => void] {
    const [state, set] = useState<T>(initState)
    const setState = useCallback(
        (patch) => {
            set((prevState) => ({ ...prevState, ...(patch instanceof Function ? patch(prevState) : patch) }))
        },
        [set]
    )
    return [state, setState]
}

export default useSetState
