import React, { PropTypes } from 'react'
import { routerRedux } from 'dva/router'
import { connect } from 'dva'
import SchemeList from '../../components/common/list'
import SchemeSearch from '../../components/common/search'
import SchemeModal from '../../components/followup/schemeModal'

function Scheme ({ location, dispatch, schemes, loading }) {
  const { list, pagination, currentItem, modalVisible, modalType, selectedRowKeys } = schemes;
  const schemeModalProps = {
    item: modalType === 'create' ? {} : currentItem,
    type: modalType,
    visible: modalVisible,
    onOk (data) {
      dispatch({
        type: `schemes/${modalType}`,
        payload: data,
      })
    },
    onCancel () {
      dispatch({
        type: 'schemes/hideModal',
      })
    },
  };
  const onEditItem = (item) => {
    dispatch({
      type: 'schemes/showModal',
      payload: {
        modalType: 'update',
        currentItem: item,
      },
    })
  };

  const schemeListProps = {
    dataSource: list,
    loading,
    pagination,
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
        type: 'schemes/delete',
        payload: id,
      })
    },
    rowSelection: {
      selectedRowKeys,
      onChange: (selectedRowKeys) => {
        dispatch({
          type: 'schemes/selectRowKeys',
          payload: selectedRowKeys,
        })
      }
    },
    columns: [
      {
        title: '编号',
        dataIndex: 'id',
        key: 'id',
      }, {
        title: '方案名称',
        dataIndex: 'name',
        key: 'name',
      }, {
        title: '描述',
        dataIndex: 'desc',
        key: 'desc',
      }, {
        title: '操作',
        key: 'operation',
        width: 150,
        render: (text, record) => (
          <span onClick={(e) => {e.stopPropagation()}}>
            <a onClick={() => {onEditItem(record)}}>编辑</a>
          </span>
        ),
      },
    ],
  };

  const schemeSearchProps = {
    selectedRowKeys,
    onSearch (fieldsValue) {
      fieldsValue.keyword.length ? dispatch(routerRedux.push({
        pathname: '/schemes',
        query: {
          field: fieldsValue.field,
          keyword: fieldsValue.keyword,
        },
      })) : dispatch(routerRedux.push({
        pathname: '/schemes',
      }))
    },
    onAdd () {
      dispatch({
        type: 'schemes/showModal',
        payload: {
          modalType: 'create',
        },
      })
    },
    onEditItem (item) {
      dispatch({
        type: 'schemes/showModal',
        payload: {
          modalType: 'update',
          currentItem: item,
        },
      })
    },
    onDeleteItems (ids) {
      dispatch({
        type: 'schemes/delete',
        payload: ids[0]
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
  };

  const SchemeModalGen = () =>
    <SchemeModal {...schemeModalProps} />

  return (
    <div className="content-inner">
      <SchemeSearch {...schemeSearchProps} />
      <SchemeList {...schemeListProps} />
      <SchemeModalGen />
    </div>
  )
}

Scheme.propTypes = {
  schemes: PropTypes.object,
  location: PropTypes.object,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
};

export default connect(({ schemes, loading }) => ({ schemes, loading: loading.models.schemes }))(Scheme)
