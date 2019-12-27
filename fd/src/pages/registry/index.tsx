import React, { useState, useEffect } from 'react'
import { router } from 'umi'
import { connect, useDispatch } from 'dva'
import { Form, Input, Button, message, Icon } from 'antd'
import { connectState, connectProps } from '@/models/connect'
import { useLocalStorageState } from '@umijs/hooks'

import styles from './index.less'

interface IProps extends connectProps {
    loading: boolean
}

interface IFormItems {
    key: 'name' | 'account' | 'password'
    iconType: string
    placeholder: string
}
const FormItem = Form.Item

const rules = {
    name: [{ required: true, message: '请输入昵称' }],
    account: [{ required: true, message: '请输入昵称' }],
    password: [{ required: true, message: '请输入密码' }],
    verifyCode: [{ required: true, message: '请输入验证码' }]
}

const formItems: IFormItems[] = [
    {
        key: 'name',
        iconType: 'fire',
        placeholder: '昵称'
    },
    {
        key: 'account',
        iconType: 'user',
        placeholder: '帐号'
    },
    {
        key: 'password',
        iconType: 'lock',
        placeholder: '密码'
    }
]

const initLoginInfo = { account: '', password: '' }

const Register: React.FC<IProps> = ({ form, loading }) => {
    const dispatch = useDispatch()
    const { getFieldDecorator } = form
    const [_loginInfo, setLoginInfo] = useLocalStorageState('loginInfo', initLoginInfo)
    const [sendStatus, setSendStatus] = useState<boolean>(false)
    const [sendText, setSendText] = useState<string>('发送验证码')

    const submitCallback = (payload: any) => {
        message.success('注册成功，正在自动登录...')
        setLoginInfo(payload)
        dispatch({ type: 'login/login', payload })
    }

    const handleSubmit = (evt: React.FormEvent<HTMLFormElement>) => {
        evt.preventDefault()
        console.log(666)
        form.validateFields((err, payload) => {
            const { account, password } = payload
            if (!err) {
                //  调用注册接口
                dispatch({
                    type: 'registry/registry',
                    payload,
                    callback: () => submitCallback({ account, password })
                })
            }
        })
    }

    let timer: any = null

    useEffect(() => () => clearInterval(timer), [])

    const sendCallback = () => {
        let countDown = 60
        setSendStatus(true)
        timer = setInterval(() => {
            if (countDown === 0) {
                clearInterval(timer)
                setSendStatus(false)
                setSendText('再次发送')
            } else {
                setSendText(`${countDown--}S`)
            }
        }, 1000)
    }

    const handleSend = (evt: React.MouseEvent<HTMLElement, MouseEvent>) => {
        evt.preventDefault()
        form.validateFields(['account'], (err, values) => {
            if (!err) {
                dispatch({
                    type: 'registry/getVerifyCode',
                    payload: values,
                    callback: sendCallback
                })
            }
        })
    }

    return (
        <div className={styles.registry_wrapper}>
            <Form onSubmit={handleSubmit} className={styles.formWrapper}>
                {formItems.map(({ key, iconType, placeholder }) => (
                    <FormItem key={key}>
                        {getFieldDecorator(key, {
                            rules: rules[key]
                        })(<Input prefix={<Icon type={iconType} />} placeholder={placeholder} className={styles.inputHeight}></Input>)}
                    </FormItem>
                ))}
                <FormItem>
                    <div className={styles.sendWrapper}>
                        {getFieldDecorator('verifyCode', {
                            rules: rules.verifyCode
                        })(<Input prefix={<Icon type='code' />} placeholder='验证码' className={styles.inputHeight}></Input>)}
                        <Button onClick={handleSend} className={styles.sendBtn} disabled={sendStatus}>
                            {sendText}
                        </Button>
                    </div>
                </FormItem>

                <Button type='primary' htmlType='submit' block loading={loading} className={styles.registryBtn}>
                    注册
                </Button>
            </Form>
        </div>
    )
}

export default connect(({ loading }: connectState) => ({
    loading: loading.effects['register/register']
}))(Form.create()(Register))
