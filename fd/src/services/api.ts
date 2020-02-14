export default {
    // auth
    authLogin: 'POST /auth/login',
    authLogout: 'DELETE /auth/logout',
    authDelete: 'DELETE /auth/delete',
    authRegistry: 'POST /auth/register',
    authUpdate: 'PUT /auth',
    authUpdatePassword: 'PUT /auth/update/password',
    authQueryByAccount: 'GET /auth/account/:account',
    authQueryById: 'GET /auth/query/account/:id',

    // email
    emailSend: 'POST /email/send_code',
    articleGet: 'GET /article/page/:authorId',
    articleGetByCategory: 'GET /article/page/category/:categoryId',
    articleGetByArchive: 'GET /article/page/archive/:authorId/:month',

    // category
    categoryAdd: 'POST /category/add',
    categoryDelete: 'DELETE /category/delete/:categoryId',
    categoryGet: 'GET /category/page/:authorId',
    categoryUpdate: 'PUT /category/update',

    // archive
    archiveGet: 'GET /article/archives/:authorId',

    // article
    articleAdd: 'POST /article/add',
    articleDelete: 'POST /article/delete',
    articleLike: 'POST /article/like/:articleId',
    articleDislike: 'DELETE /article/like/:articleId',
    articleEdit: 'PUT /article/update/:articleId',
    articleGetById: 'GET /article/:articleId',

    //  comment
    commentAdd: 'POST /comment',
    commentQueryByArticleId: 'GET /comment/page/article/:articleId',
    commentQueryLikedByArticleId: 'GET /comment/like/:articleId',
    commentToggleLiked: 'put /comment/like/:commentId',
    commentQueryChild: 'GET /comment/page/comment/:commentId'
}
