export const genHash = () => {
    return Math.random()
        .toString(36)
        .slice(2)
}
