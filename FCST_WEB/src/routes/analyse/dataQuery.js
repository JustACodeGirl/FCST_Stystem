import React, { PropTypes } from 'react'
import { connect } from 'dva'
import QueryList from '../../components/common/list'
import SearchForm from '../../components/common/searchForm'

function DataQuery ({ location, dispatch, dataQuery }) {
  const { list, pagination, queryData, queryInfo } = dataQuery

  const searchFormProps = {
    queryData: queryData,
    onSearch (data) {
      dispatch({
        type: 'dataQuery/query',
        payload: data,
      })
    },
    onExport (data) {
      dispatch({
        type: 'dataQuery/exportTable',
        payload: data,
      })
    },
    onCancel () {
      dispatch({
        //type: 'patients/hideModal',
      })
    },
  }
  const queryListProps = {
    dataSource: list,
    location,
    pagination,
    scroll: { x: 1800, y: 600 },
    onPageChange (data) {
      let query = Object.assign({}, queryInfo)
      query.pageNo = data.current
      query.pageSize = data.pageSize
      dispatch({
        type: 'dataQuery/query',
        payload: query,
      })
    },
    columns: [
      {
        title: 'Region',
        dataIndex: 'region',
        key: 'region',
        width: 100,
        sorter: (a, b) => a.region - b.region,
      }, {
        title: 'Forecast Week',
        dataIndex: 'fcstWeek',
        key: 'fcstWeek',
        width: 150,
        sorter: (a, b) => a.fcstWeek > b.fcstWeek,
      }, {
        title: 'Customer',
        dataIndex: 'customer',
        key: 'customer',
        width: 100,
      }, {
        title: 'MI/CM',
        dataIndex: 'mi',
        key: 'mi',
        width: 180,
      }, {
        title: 'Disti',
        dataIndex: 'disti',
        key: 'disti',
        width: 70,
        sorter: (a, b) => a.birth > b.birth,
      }, {
        title: 'Reseller',
        dataIndex: 'reseller',
        key: 'reseller',
        width: 150,
      }, {
        title: 'PARTID',
        dataIndex: 'partId',
        key: 'partId',
        width: 180,
      }, {
        title: 'Project',
        dataIndex: 'project',
        key: 'project',
        width: 150,
      }, {
        title: 'Account Manager',
        dataIndex: 'accountManager',
        key: 'accountManager',
        width: 160,
      }, {
        title: 'Market',
        dataIndex: 'market',
        key: 'market',
        width: 70,
      }, {
        title: 'Note',
        dataIndex: 'note',
        key: 'note',
        width: 200,
      }, {
        title: 'Qty',
        dataIndex: 'qty',
        key: 'qty',
        width: 100,
      }, {
        title: 'Asp',
        dataIndex: 'asp',
        key: 'asp',
        width: 100,
      }, {
        title: 'Bucket Date',
        dataIndex: 'bucketDate',
        key: 'bucketDate',
        width: 150,
      }, {
        title: 'ConfidenceLevel',
        dataIndex: 'confidenceLevel',
        key: 'confidenceLevel',
        width: 150,
      }, {
        title: 'Revenue',
        dataIndex: 'revenue',
        key: 'revenue',
        width: 100,
      }, {
        title: 'OVT QBR',
        dataIndex: 'qtr',
        key: 'qtr',
        width: 100,
      },
    ],
  }

  return (
    <div className="content-inner">
      <SearchForm {...searchFormProps} />
      <QueryList {...queryListProps} />
    </div>
  )
}

DataQuery.propTypes = {
  dataQuery: PropTypes.object,
  location: PropTypes.object,
  dispatch: PropTypes.func,
}

export default connect(({ dataQuery }) => ({ dataQuery }))(DataQuery)
