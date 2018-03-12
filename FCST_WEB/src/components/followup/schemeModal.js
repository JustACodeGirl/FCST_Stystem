import React, { PropTypes } from 'react'
import { Form, Input, Modal, Row, Col, Select, Button, Icon } from 'antd'
import FollowupPathInput from './followupPathInput'
import styles from './schemeModal.less'

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

const form2ItemLayout = {
  labelCol: {
    span: 3,
  },
  wrapperCol: {
    span: 20,
  },
};

const formItemLayoutWithOutLabel = {
  wrapperCol: {
    span: 21, offset: 3 ,
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
    getFieldValue,
    setFieldsValue,
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

  function add() {
    const paths = getFieldValue('paths');
    const nextPaths = paths.concat({id: paths.length+1, dateValue: 1, dateUnit: '1', todo: ["1", "2"]});
    setFieldsValue({
      paths: nextPaths,
    });
  }

  function remove(id) {
    const paths = getFieldValue('paths');
    if (paths.length > 1) {
      setFieldsValue({
        paths: paths.filter(path => path.id !== id),
      });
    }
  }

  function checkFollowupPath(rule, value, callback) {
    if (value.dateValue > 0) {
      callback();
      return;
    }
    callback('请输入日期!');
  }

  getFieldDecorator('paths', { initialValue: [{id: 1, dateValue: 1, dateUnit: '1', todo: ["1", "2"]}] });
  const paths = getFieldValue('paths');
  const pathNodes = paths.map((path, index) => {
    return (
      <FormItem
        {...(index === 0 ? form2ItemLayout : formItemLayoutWithOutLabel)}
        label={index === 0 ? '随访路径' : ''}
        key={path.id}
      >
        {getFieldDecorator('dateValue', {
          initialValue: {...path},
          rules: [{ validator: checkFollowupPath }],
        })(
          <FollowupPathInput />
        )}
        {
          index !== 0 && <Icon
            className={styles.dynamicDeleteButton}
            type="minus-circle-o"
            onClick={() => remove(path.id)}
          />
        }
      </FormItem>
    )
  });

  const modalOpts = {
    title: `${type === 'create' ? '添加方案' : '编辑方案'}`,
    visible,
    onOk: handleOk,
    onCancel,
    wrapClassName: 'vertical-center-modal',
    width: 800
  };
  return (
    <Modal {...modalOpts}>
      <Form layout="horizontal">
        <Row gutter={16}>
          <Col className="gutter-row" span={12}>
            <FormItem label="编号：" hasFeedback {...formItemLayout}>
              {getFieldDecorator('id', {
                initialValue: item.id,
                rules: [
                  {
                    required: true,
                    message: '方案名称未填写',
                  },
                ],
              })(<Input/>)}
            </FormItem>
          </Col>
          <Col className="gutter-row" span={12}>
            <FormItem label="方案名称：" hasFeedback {...formItemLayout}>
              {getFieldDecorator('name', {
                initialValue: item.name,
                rules: [
                  {
                    required: true,
                    message: '方案名称未填写',
                  },
                ],
              })(<Input />)}
            </FormItem>
          </Col>
        </Row>
        <FormItem label="描述：" {...form2ItemLayout}>
          {getFieldDecorator('desc', {
            initialValue: item.desc
          })(<Input type="textarea" rows={4}/>)}
        </FormItem>
        {pathNodes}
        <FormItem {...formItemLayoutWithOutLabel}>
          <Button type="dashed" onClick={add} style={{ width: '40%' }} icon="plus">添加</Button>
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
