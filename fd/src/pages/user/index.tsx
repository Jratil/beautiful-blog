import React, { useState, useEffect } from 'react'
import { connect, useDispatch } from 'dva'
import moment from 'moment'
import { Avatar, Form, Upload, message, Icon, Button, DatePicker, Input, Radio } from 'antd'
import { connectState, connectProps } from '@/models/connect'
import styles from './index.less'
// import { baseUrl } from '../../../.umirs.js'
import { IUserInfo } from '@/models/app.js'

interface IProps extends connectProps {
    userInfo: IUserInfo
}

type IType = 'view' | 'edit'

const FormItem = Form.Item
const { Dragger } = Upload
const RadioGroup = Radio.Group

const props = {
    name: 'avatar',
    action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
    onChange(info) {
        const { status } = info.file
        if (status !== 'uploading') {
            console.log(info.file, info.fileList)
        }
        if (status === 'done') {
            message.success(`${info.file.name} file uploaded successfully.`)
        } else if (status === 'error') {
            message.error(`${info.file.name} file upload failed.`)
        }
    }
}

const User: React.FC<IProps> = ({ form, userInfo }) => {
    const [type, setType] = useState<IType>('view')
    // const dispatch = useDispatch()
    // useEffect(() => {
    //     dispatch({type:'app/getUserInfo'})
    // }, [])
    const { getFieldDecorator } = form
    const { authorAccount, authorName, authorBirthday, authorGender } = userInfo
    return (
        <Form labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} labelAlign='right'>
            <FormItem label='头像'>
                {type === 'view' && (
                    <Avatar
                        size='large'
                        className={styles.avatar}
                        src='https://www.google.com/search?q=%E5%9C%A8%E7%BA%BF%E5%9B%BE%E7%89%87&sxsrf=ACYBGNTw4ltLePDQUoIwuAYe5CL9Wkt0Sg:1578044112163&tbm=isch&source=iu&ictx=1&fir=yBgcQ9zIBBwNjM%253A%252CvcCfV4vNn4w31M%252C_&vet=1&usg=AI4_-kR1uWIbgEZGU-N5FPfwpudLjQdmWA&sa=X&ved=2ahUKEwi18_q1kOfmAhUF2KQKHbZuD6YQ9QEwAnoECAoQBg#imgrc=yBgcQ9zIBBwNjM:'
                    />
                )}
                {type === 'edit' && (
                    <Upload {...props}>
                        <Button>
                            <Icon type='upload' /> 上传头像
                        </Button>
                    </Upload>
                )}
            </FormItem>
            <FormItem label='帐号'>
                <span>{authorAccount}</span>
            </FormItem>
            <FormItem label='昵称'>
                {type === 'view' && <span>{authorName}</span>}
                {type === 'edit' && getFieldDecorator('authorName')(<Input style={{ width: 300 }} />)}
            </FormItem>
            <FormItem label='生日'>
                {type === 'view' && <span>{authorBirthday}</span>}
                {type === 'edit' && <DatePicker style={{ width: 300 }}></DatePicker>}
            </FormItem>
            <FormItem label='性别'>
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
        </Form>
    )
}

export default connect(({ app }: connectState) => ({
    userInfo: app.userInfo
}))(Form.create()(User))
