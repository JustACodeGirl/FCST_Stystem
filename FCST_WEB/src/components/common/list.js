import React, { PropTypes } from 'react'
import { Table, Modal } from 'antd'
import styles from './list.less'
import classnames from 'classnames'
import TableBodyWrapper from './TableBodyWrapper'

const confirm = Modal.confirm

function list ({ loading, dataSource, pagination, onPageChange, rowSelection, isMotion, location, columns, scroll }) {

  const onRowClick = (record, index) => {
    let savedIndex = rowSelection.selectedRowKeys.indexOf(record.id)
    savedIndex >= 0 ? rowSelection.selectedRowKeys.splice(savedIndex, 1) : rowSelection.selectedRowKeys.push(record.id)

    rowSelection.onChange(rowSelection.selectedRowKeys)
  }

  const getBodyWrapperProps = {
    page: location.query.page,
    current: pagination.current,
  }

  const getBodyWrapper = body => { return isMotion ? <TableBodyWrapper {...getBodyWrapperProps} body={body} /> : body }
  return (
    <div style={{ maxHeight: 500, marginTop: 20 }}>
      <Table
        className={classnames({ [styles.table]: true, [styles.motion]: isMotion })}
        bordered
        scroll={scroll}
        columns={columns}
        dataSource={dataSource}
        loading={loading}
        onChange={onPageChange}
        onRowClick={onRowClick}
        pagination={pagination}
        simple
        size="small"
        rowKey={record => record.id}
        getBodyWrapper={getBodyWrapper}
      />
    </div>
  )
}

list.propTypes = {
  loading: PropTypes.bool,
  dataSource: PropTypes.array,
  scroll: PropTypes.object,
  pagination: PropTypes.object,
  rowSelection: PropTypes.object,
  onPageChange: PropTypes.func,
  onDeleteItem: PropTypes.func,
  onEditItem: PropTypes.func,
  isMotion: PropTypes.bool,
  location: PropTypes.object,
  columns: PropTypes.array,
}

export default list
