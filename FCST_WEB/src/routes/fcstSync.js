import React, { PropTypes } from 'react'
import { routerRedux } from 'dva/router'
import { connect } from 'dva'
import { Upload, Icon, message, Spin } from 'antd'

const Dragger = Upload.Dragger

class FCSTSync extends React.Component {
  state = {
    loadingvisible: false,
  }

  handleChange = (info) => {
    console.log(this.state)
    const status = info.file.status
    if (status === 'uploading') {
      console.log(info.file, info.fileList)
      this.setState({ loadingvisible: true })
    }
    if (status === 'done') {
      message.success(`${info.file.name} 文件成功上传.`, 5)
      this.setState({ loadingvisible: false })
    } else if (status === 'error') {
      message.error(`${info.file.name} 文件上传失败.`, 5)
      this.setState({ loadingvisible: false })
    }
  }

  render () {
    const { loadingvisible } = this.state
    return (
      <div className="content-inner" style={{ height: 500 }}>
        <Dragger
          name="xlsfile"
          action="/api/upload"
          multiple="true"
          showUploadList="false"
          headers={{
            authorization: 'authorization-text',
          }}
          data={{
            method: 'POST',
          }}
          onChange={this.handleChange}
        >
          <p className="ant-upload-drag-icon">
            <Icon type="inbox" />
          </p>
          <p className="ant-upload-text">点击或拖拽excel文件到此处上传</p>
          <p className="ant-upload-hint">支持单个xls或xlsx文件上传。严禁上传其他非相关文件</p>
          {
            loadingvisible === true ? (
            <div>
              <p>文件上传中</p>
              <Spin />
            </div>
            ) : (null)
          }
        </Dragger>
      </div>
    )
  }
}

FCSTSync.propTypes = {
  fcstSync: PropTypes.object,
  location: PropTypes.object,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
}

export default FCSTSync
