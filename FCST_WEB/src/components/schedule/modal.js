import React, { PropTypes } from 'react'
import { Form, Input, Modal, DatePicker } from 'antd'

const FormItem = Form.Item;
const RangePicker = DatePicker.RangePicker;

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
  type,
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
      data.start = data.start.format('YYYY-MM-DD');
      data.end = data.end.format('YYYY-MM-DD');
      onOk(data)
    })
  }

  const modalOpts = {
    title: `${type === 'create' ? '添加日程' : '修改日程'}`,
    visible,
    onOk: handleOk,
    onCancel,
    wrapClassName: 'vertical-center-modal',
    width: 600,
  };
  return (
    <Modal {...modalOpts}>
      <Form layout="horizontal">
        <FormItem label="事件名称：" hasFeedback {...formItemLayout}>
          {getFieldDecorator('title', {
            initialValue: item.title,
            rules: [{ required: true, message: '事件名称未填写', },],
          })(<Input />)}
        </FormItem>
        <FormItem label="事件描述" hasFeedback {...formItemLayout}>
          {getFieldDecorator('desc', {
            initialValue: item.desc,
            rules: [{ required: true, message: '事件描述未填写', },],
          })(<Input type="textarea" rows={3}/>)}
        </FormItem>
        <FormItem label="选择日期" {...formItemLayout}>
          {getFieldDecorator('range-time-picker', {
            initialValue: [item.start, item.end],
            rules: [{ type: 'array', required: true, message: '请选择时间' }],
          })(<RangePicker showTime allowClear={false} format="YYYY-MM-DD HH:mm:ss" style={{width: 378}}/>)}
        </FormItem>
      </Form>
    </Modal>
  )
};

modal.propTypes = {
  form: PropTypes.object.isRequired,
  visible: PropTypes.bool,
  type: PropTypes.string,
  item: PropTypes.object,
  onCancel: PropTypes.func,
  onOk: PropTypes.func,
};

export default Form.create()(modal)
