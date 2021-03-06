import React, { PropTypes } from 'react'
import { Form, Input, Modal, Select } from 'antd'

const FormItem = Form.Item;
const Option = Select.Option;

const formItemLayout = {
  labelCol: {
    span: 6,
  },
  wrapperCol: {
    span: 16,
  },
};

const modal = ({
  visible,
  item = {},
  onOk,
  onCancel,
  form: {
    getFieldDecorator,
    validateFields,
    getFieldsValue,
  },
}) => {
  function handleOk () {
    validateFields((errors) => {
      if (errors) {
        return
      }
      const data = {
        ...getFieldsValue(),
        id: item.id,
      };
      onOk(data)
    })
  }

  const modalOpts = {
    title: '添加角色',
    visible,
    onOk: handleOk,
    onCancel,
    wrapClassName: 'vertical-center-modal',
    width: 600
  };
  return (
    <Modal {...modalOpts}>
      <Form layout="horizontal">
        <FormItem label="角色名称：" hasFeedback {...formItemLayout}>
          {getFieldDecorator('name', {
            initialValue: item.name,
            rules: [
              {
                required: true,
                message: '角色名称未填写',
              },
            ],
          })(<Input/>)}
        </FormItem>
        <FormItem label="角色描述：" {...formItemLayout}>
          {getFieldDecorator('desc', {
            initialValue: item.desc,
          })(<Input type="textarea" rows={3}/>)}
        </FormItem>
      </Form>
    </Modal>
  )
};

modal.propTypes = {
  form: PropTypes.object.isRequired,
  visible: PropTypes.bool,
  item: PropTypes.object,
  onCancel: PropTypes.func,
  onOk: PropTypes.func,
};

export default Form.create()(modal)
