import React, { PropTypes } from 'react'
import { Form, Input, Button, Row, Col, Modal, Upload, message, Icon } from 'antd';
const FormItem = Form.Item;
const search = ({ selectedRowKeys, onSync, onDeleteItems, onReload }) => {

const props = {
  name: 'xlsfile',
  action: '/api/upload',
  headers: {
    authorization: 'authorization-text',
  },
  data: {
    method: 'POST',
  },
  onChange (info) {
    console.log(info)
    if (info.file.status !== 'uploading') {
      console.log(info.file, info.fileList)
    }
    if (info.file.status === 'done') {
      message.success(`${info.file.name} file uploaded successfully`)
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} file upload failed.`)
    }
  },
}

  function showConfirm() {
    selectedRowKeys.length > 0 && Modal.confirm({
      title: '确定要删除当前选中的报表信息吗?',
      content: '点击确定后，报表信息删除。',
      onOk() {
        onDeleteItems(selectedRowKeys);
        return new Promise((resolve, reject) => {
          setTimeout(Math.random() > 0.5 ? resolve : reject, 1000);
        }).catch(() => {});
      },
      onCancel() {},
    });
  }

  return (
    <Row gutter={24}>
      <Col lg={16} md={16} sm={16} xs={24} style={{ marginBottom: 16}}>
        <Form layout="inline" onSubmit={onSync}>
          <FormItem label="导入报表">
            <Input placeholder="输入报表编号" style={{width: 300}}/>
          </FormItem>
          <FormItem>
            <Upload {...props}>
              <Button>
                <Icon type="upload" /> 点击上传
              </Button>
            </Upload>
          </FormItem>
          <FormItem>
            <Button type="primary" htmlType="submit" icon="swap">同步 </Button>
            <Button size="large" type="danger" icon="delete" style={{marginLeft: 6}} onClick={showConfirm}>删除</Button>
          </FormItem>
        </Form>

      </Col>
      <Col lg={{ offset: 2, span: 6 }} md={8} sm={8} xs={24} style={{ marginBottom: 16, textAlign: 'right' }}>
        <Button size="large" icon="reload" onClick={onReload}>刷新</Button>
      </Col>
    </Row>
  )
}

search.propTypes = {
  form: PropTypes.object.isRequired,
  onSyncItems: PropTypes.func,
  onDeleteItems: PropTypes.func,
  onReload: PropTypes.func,
}

export default Form.create()(search)
