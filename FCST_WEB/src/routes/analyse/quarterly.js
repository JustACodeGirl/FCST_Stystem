import React, { PropTypes } from 'react'
import { routerRedux } from 'dva/router'
import { connect } from 'dva'
import { Select, Button, Tabs, Icon } from 'antd'
import { LineChart, Line, BarChart, Bar, PieChart, Pie, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts'
import PatientSyncList from '../../components/common/list'
import QuarterlyQuery from '../../components/queryForm/quarterlyQuery'

function Quarterly ({ location, dispatch, quarterly, loading }) {
  const { list, pagination, selectedRowKeys, quarterlyQueryData } = quarterly

  const syncListProps = {
    dataSource: list,
    loading,
    pagination: false,
    location,
    onPageChange (page) {
      const { query, pathname } = location
      dispatch(routerRedux.push({
        pathname,
        query: {
          ...query,
          page: page.current,
          pageSize: page.pageSize,
        },
      }))
    },
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
      onChange: (selectedRowKeys) => {
        dispatch({
          type: 'patientSync/selectRowKeys',
          payload: selectedRowKeys,
        })
      },
    },
    columns: [
      {
        title: 'Confidence Level',
        dataIndex: 'confidencelevel',
        key: 'confidencelevel',
        width: 150,
        render: (value, row, index) => {
          const obj = {
            children: value,
            props: {},
          }
          if (row.index === 2) {
            obj.props.rowSpan = 2
          }
          if (row.index === 0) {
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

  const barX = []
  const data = []
  if (quarterlyQueryData.selectDates) {
    quarterlyQueryData.selectDates.forEach(item => {
      barX.push(item)
      syncListProps.columns.push({
        title: item,
        dataIndex: item,
        key: item,
      })
    })
    syncListProps.columns.push({
      title: '总计',
      dataIndex: 'total',
      key: 'total',
    })
  }

  if (list.length > 0) {
    barX.forEach(quarter => {
      data.push({ name: quarter, basePm: list[0][quarter], baseSales: list[1][quarter], upsidePm: list[2][quarter], upsideSales: list[3][quarter] })
    })
  }

  const quarterlyProps = {
    selectedRowKeys,
    quarterlyData: quarterlyQueryData,
    onSearch (value) {
      dispatch({
        type: 'quarterly/query',
        payload: value,
      })
    },
    onSelect (value) {
      console.log('onChange ', value)
      dispatch({
        type: 'quarterly/select',
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
      <QuarterlyQuery {...quarterlyProps} />
      <Tabs defaultActiveKey="1" tabPosition="right" style={{ marginTop: 20 }}>
        <Tabs.TabPane tab={<span><Icon type="search" /> 检索结果</span>} key="1">
          <PatientSyncList {...syncListProps} />
        </Tabs.TabPane>
        <Tabs.TabPane tab={<span><Icon type="area-chart" /> 报表统计</span>} key="2">
          <ResponsiveContainer minHeight={500}>
            <BarChart width={600} height={600} data={data} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
              <XAxis dataKey="name" />
              <YAxis />
              <CartesianGrid strokeDasharray="3 3" />
              <Tooltip />
              <Legend />
              <Bar dataKey="basePm" stackId="base" fill="#8884d8" />
              <Bar dataKey="baseSales" stackId="base" fill="#82ca9d" />
              <Bar dataKey="upsidePm" stackId="upside" fill="#f78e3d" />
              <Bar dataKey="upsideSales" stackId="upside" fill="#ffce3d" />
            </BarChart>
          </ResponsiveContainer>
        </Tabs.TabPane>
      </Tabs>
    </div>
  )
}

Quarterly.propTypes = {
  quarterly: PropTypes.object,
  location: PropTypes.object,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
}

export default connect(({ quarterly, loading }) => ({ quarterly, loading: loading.models.quarterly }))(Quarterly)
