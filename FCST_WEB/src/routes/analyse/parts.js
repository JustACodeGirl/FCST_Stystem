import React, { PropTypes } from 'react'
import { routerRedux } from 'dva/router'
import { connect } from 'dva'
import PatientSyncList from '../../components/common/table'
import PartsQuery from '../../components/queryForm/partsQuery'

function Parts ({ location, dispatch, parts, loading }) {
  const { list, selectedRowKeys, partsQueryData, pagination } = parts
  const syncListProps = {
    dataSource: list,
    loading,
    location,
    pagination: false,
    isMotion: true,
    scroll: { x: 1800 },
    onDeleteItem (id) {
      dispatch({
        type: 'patientSync/delete',
        payload: id,
      })
    },
    onSyncItem (item) {
      dispatch({
        type: 'patientSync/sync',
        payload: {
          modalType: 'update',
          currentItem: item,
        },
      })
    },
    rowSelection: {
      selectedRowKeys,
    },
    columns: [
      {
        title: 'PARTID',
        dataIndex: 'partid',
        key: 'partid',
        width: 150,
        render: (value, row) => {
          const obj = {
            children: value,
            props: {},
          }
          if (row.index === 1) {
            obj.props.rowSpan = 2
          }
          if (row.index === 2) {
            obj.props.colSpan = 0
          }
          return obj
        },
      }, {
        title: 'Region',
        dataIndex: 'region',
        key: 'region',
        width: 100,
      },
    ],
  }
  if (partsQueryData.selectDates) {
    partsQueryData.selectDates.forEach(item => {
      syncListProps.columns.push({
        title: item,
        dataIndex: item,
        key: item,
        width: 100,
      })
    })
  }

  const partsProps = {
    selectedRowKeys,
    partsData: partsQueryData,
    onSearch (value) {
      dispatch({
        type: 'parts/query',
        payload: value,
      })
    },
    onSelect (value) {
      console.log('onChange ', value)
      dispatch({
        type: 'parts/select',
        payload: value,
      })
    },
    onSync (ids) {
      dispatch({
        type: 'patientSync/sync',
        payload: ids[0],
      })
    },
    onDeleteItems (ids) {
      dispatch({
        type: 'patientSync/delete',
        payload: ids[0],
      })
    },
    onReload () {
      const { query, pathname } = location
      dispatch(routerRedux.push({
        pathname,
        query: {
          ...query,
          page: pagination.current,
          pageSize: pagination.pageSize,
        },
      }))
    },
  }

  return (
    <div className="content-inner">
      <PartsQuery {...partsProps} />
      <PatientSyncList {...syncListProps} />
    </div>
  )
}

Parts.propTypes = {
  parts: PropTypes.object,
  location: PropTypes.object,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
}

export default connect(({ parts, loading }) => ({ parts, loading: loading.models.parts }))(Parts)
