export default {
    // auth
    authLogin: 'POST /auth/login',
    authLogout: 'DELETE /auth/logout',
    authDelete: 'DELETE /auth/delete',
    authRegistry: 'POST /auth/register',
    authUpdate: 'PUT /auth/update',
    authUpdatePassword: 'PUT /auth/update/password',
    authQueryByAccount: 'GET /auth/account/:account',
    authQueryById: 'GET /auth/query/account/:id',

    // email
    emailSend: 'POST /email/send_code',
    articleGet: 'GET /article/page/:authorId',
    articleGetByCategory: 'GET /article/page/category/:categoryId',

    // category
    categoryAdd: 'POST /category/add',
    categoryDelete: 'DELETE /category/delete/:categoryId',
    categoryGet: 'GET /category/page/:authorId',
    categoryUpdate: 'PUT /category/update',

    // article
    articleAdd: 'POST /article/add',
    articleDelete: 'POST /article/delete',
    articleLike: 'POST /article/like/:articleId',
    articleDislike: 'DELETE /article/like/:articleId',
    articleEdit: 'PUT /article/update/:articleId',
    articleGetById: 'GET /article/query/:articleId'
}
