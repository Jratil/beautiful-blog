import React, { useState, useEffect } from 'react'
import { connect, useDispatch } from 'dva'
import moment from 'moment'
import { Avatar, Form, Upload, message, Icon, Button, DatePicker, Input, Radio } from 'antd'
import { connectState, connectProps } from '@/models/connect'
import styles from './index.less'
import { UploadProps } from 'antd/lib/upload'
import { IUserInfo } from '@/models/app.js'
import cookies from 'js-cookie'
import { AUTHORIZATION_KEY } from '@/services'
import { XAvatar } from '@/components'

interface IProps extends connectProps {
    userInfo: IUserInfo
}

type IType = 'view' | 'edit'

const FormItem = Form.Item
const { Dragger } = Upload
const RadioGroup = Radio.Group

const props: UploadProps = {
    name: 'avatar',
    action: '/api/upload/pic',
    headers: {
        Authorization: cookies.get(AUTHORIZATION_KEY) as string
    },
    beforeUpload(file) {
        if (file.size > 2 * 1024 * 1024) {
            message.error('上传的文件不应大于 2M')
            return false
        }
        return true
    }
    // onChange(info) {
    //     const { status } = info.file
    //     if (status !== 'uploading') {
    //         console.log(info.file, info.fileList)
    //     }
    //     if (status === 'done') {
    //         message.success(`${info.file.name} file uploaded successfully.`)
    //     } else if (status === 'error') {
    //         message.error(`${info.file.name} file upload failed.`)
    //     }
    // }
}

const User: React.FC<IProps> = ({ form, userInfo }) => {
    const dispatch = useDispatch()
    const [type, setType] = useState<IType>('edit')
    const { getFieldDecorator } = form
    const { authorAccount, authorName, authorBirthday, authorGender, authorAvatar, authorId } = userInfo

    const handleEdit = () => setType('edit')

    const handleSubmit = () => {
        form.validateFields((err, values) => {
            const payload = {
                ...values,
                authorId,
                authorAccount,
                authorBirthday: Number(values.authorBirthday.toDate())
            }
            dispatch({
                type: 'user/update',
                payload,
                callback: () => {
                    message.success('修改成功')
                    setType('view')
                    dispatch({ type: 'app/getUserInfo', payload: { account: authorAccount } })
                }
            })
        })
    }
    return (
        <Form labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} labelAlign="right">
            <FormItem label="头像">
                <XAvatar name={authorName} avatarSrc={authorAvatar} className={styles.avatar} size="large" />
                {type === 'edit' && (
                    <Upload {...props}>
                        <Button>
                            <Icon type="upload" /> 上传头像
                        </Button>
                    </Upload>
                )}
            </FormItem>
            <FormItem label="帐号">
                <span>{authorAccount}</span>
            </FormItem>
            <FormItem label="昵称">
                {type === 'view' && <span>{authorName}</span>}
                {type === 'edit' &&
                    getFieldDecorator('authorName', { initialValue: authorName })(<Input style={{ width: 300 }} />)}
            </FormItem>
            <FormItem label="生日">
                {type === 'view' && <span>{moment(authorBirthday * 1000).format('YYYY-MM-DD')}</span>}
                {type === 'edit' &&
                    getFieldDecorator('authorBirthday', { initialValue: moment(authorBirthday * 1000) })(
                        <DatePicker style={{ width: 300 }} />
                    )}
            </FormItem>
            <FormItem label="性别">
                {type === 'view' && <span>{authorGender === 0 ? '靓仔' : '靓女'}</span>}
                {type === 'edit' &&
                    getFieldDecorator('authorGender', {
                        initialValue: authorGender
                    })(
                        <RadioGroup>
                            <Radio value={0}>靓仔</Radio>
                            <Radio value={1}>靓女</Radio>
                        </RadioGroup>
                    )}
            </FormItem>
            <FormItem label="操作">
                {type === 'view' && (
                    <Button type="primary" onClick={handleEdit}>
                        修改资料
                    </Button>
                )}
                {type === 'edit' && (
                    <>
                        <Button type="primary" style={{ marginRight: 8 }} onClick={handleSubmit}>
                            完成
                        </Button>
                        <Button onClick={() => setType('view')}>取消</Button>
                    </>
                )}
            </FormItem>
        </Form>
    )
}

export default connect(({ app }: connectState) => ({
    userInfo: app.userInfo
}))(Form.create()(User))
