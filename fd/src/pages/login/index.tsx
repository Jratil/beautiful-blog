import React from 'react'
import router from 'umi/router'
import { connect, useDispatch } from 'dva'
import { Form, Input, Icon, Button, message } from 'antd'
import styles from './index.less'
import { connectState, connectProps } from '@/models/connect'
import { useLocalStorageState } from '@umijs/hooks'

interface IProps extends connectProps {
    loading: boolean
}

const FormItem = Form.Item

const initLoginInfo = { account: '', password: '' }

const Login: React.FC<IProps> = ({ form, loading }) => {
    const dispatch = useDispatch()
    const [loginInfo, setLoginInfo] = useLocalStorageState('loginInfo', initLoginInfo)
    const { getFieldDecorator, validateFields } = form

    const successCallback = () => {
        message.success('登录成功！')
        dispatch({ type: 'app/getUserInfo', payload: { account: form.getFieldValue('account') } })
        router.push('/')
        window.location.reload()
    }

    const handleSubmit = (evt: React.FormEvent<HTMLFormElement>) => {
        evt.preventDefault()
        validateFields((err, payload) => {
            if (!err) {
                setLoginInfo(payload)
                dispatch({ type: 'login/login', payload, callback: successCallback })
            }
        })
    }

    const handleForget = () => {}

    return (
        <div className={styles.login_wrapper}>
            <Form onSubmit={handleSubmit}>
                <FormItem>
                    {getFieldDecorator('account', {
                        initialValue: loginInfo.account,
                        rules: [{ required: true, message: '请输入账号' }]
                    })(<Input prefix={<Icon type="user" />} placeholder="用户名" className={styles.inputHeight} />)}
                </FormItem>
                <FormItem>
                    {getFieldDecorator('password', {
                        initialValue: loginInfo.password,
                        rules: [{ required: true, message: '请输入密码' }]
                    })(
                        <Input.Password
                            prefix={<Icon type="lock" />}
                            placeholder="密码"
                            className={styles.inputHeight}
                        />
                    )}
                </FormItem>

                <div>
                    <Button type="primary" htmlType="submit" className={styles.loginBtn} loading={loading}>
                        登录
                    </Button>
                </div>
                <div className={styles.notes}>
                    <Button type="link" onClick={() => router.push('/registry')}>
                        注册
                    </Button>
                    <Button type="link" onClick={handleForget}>
                        忘记密码
                    </Button>
                </div>
            </Form>
        </div>
    )
}

export default connect(({ loading }: connectState) => ({
    loading: loading.effects['login/login']
}))(Form.create<IProps>()(Login))
