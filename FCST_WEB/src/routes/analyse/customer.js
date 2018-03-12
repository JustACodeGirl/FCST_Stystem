import React, { PropTypes } from 'react'
import { routerRedux } from 'dva/router'
import { connect } from 'dva'
import CustomerList from '../../components/common/table'
import CustomerQuery from '../../components/queryForm/customerQuery'

function Customer ({ location, dispatch, customer, loading }) {
  const { list, selectedRowKeys, customerQueryData, pagination } = customer
  const syncListProps = {
    dataSource: list,
    loading,
    location,
    pagination: false,
    isMotion: true,
    scroll: { x: 3000 },
    columns: [
      {
        title: 'Customer',
        dataIndex: 'customer',
        key: 'customer',
        width: 150,
        render: (value, row, index) => {
          const obj = {
            children: value,
            props: {},
          }
          if (row.index !== 0) {
            obj.props.rowSpan = row.index
          }
          if (row.index === 0) {
            obj.props.colSpan = 0
          }
          return obj
        },
      }, {
        title: 'PARTID',
        dataIndex: 'partid',
        key: 'partid',
        width: 150,
      },
    ],
  }
  if (customerQueryData.selectDates) {
    customerQueryData.selectDates.forEach(item => {
      syncListProps.columns.push({
        title: item,
        children: [{
          title: 'Qty',
          dataIndex: (item + 'Q'),
          key: (item + 'Q'),
          width: 70,
        }, {
          title: 'Revenue',
          dataIndex: (item + 'R'),
          key: (item + 'R'),
          width: 70,
        }],
        width:140,
      })
    })
  }

  const customerProps = {
    selectedRowKeys,
    customerData: customerQueryData,
    onSearch (value) {
      dispatch({
        type: 'customer/query',
        payload: value,
      })
    },
    onSelect (value) {
      console.log('onChange ', value)
      dispatch({
        type: 'customer/select',
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
      <CustomerQuery {...customerProps} />
      <CustomerList {...syncListProps} />
    </div>
  )
}

Customer.propTypes = {
  customer: PropTypes.object,
  location: PropTypes.object,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
}

export default connect(({ customer, loading }) => ({ customer, loading: loading.models.customer }))(Customer)
