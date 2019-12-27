export default {
    // auth
    authLogin: 'POST /auth/login',
    authLogout: 'DELETE /auth/logout',
    authDelete: 'DELETE /auth/delete',
    authRegistry: 'POST /auth/register',
    authUpdate: 'PUT /auth/update',
    authUpdatePassword: 'PUT /auth/update/password',

    // email
    emailSend: 'POST /email/send_code',
    articleGet: 'GET /article/main_page',

    // category
    categoryAdd: 'POST /category/add',
    categoryDelete: 'DELETE /category/delete/:categoryId',
    categoryGet: 'POST /category/page/:authorId',
    categoryUpdate: 'PUT /category/update'
}
