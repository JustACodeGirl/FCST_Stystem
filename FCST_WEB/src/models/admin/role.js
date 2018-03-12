import { create, remove, update, query } from '../../services/admin/role'
import { parse } from 'qs'

export default {
  namespace: 'adminRoles',
  state: {
    list: [],
    currentItem: {},
    modalVisible: 0,
    pagination: {
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: total => `共 ${total} 条`,
      current: 1,
      total: null,
    },
    selectedRowKeys: []
  },
  reducers: {
    querySuccess (state, action) {
      const { list, pagination } = action.payload;
      return { ...state,
        list,
        pagination: {
          ...state.pagination,
          ...pagination,
        } }
    },
    showAddModal (state, action) {
      return { ...state, ...action.payload, modalVisible: 1 }
    },
    showEditModal (state, action) {
      return { ...state, ...action.payload, modalVisible: 2 }
    },
    hideModal (state) {
      return { ...state, modalVisible: 0 }
    },
    selectRowKeys (state, action) {
      return { ...state, selectedRowKeys: [...action.payload]}
    }
  },
  effects: {
    *query ({ payload }, { call, put }) {
      const data = yield call(query, parse(payload));
      if (data) {
        yield put({
          type: 'querySuccess',
          payload: {
            list: data.data,
            pagination: data.page,
          },
        })
      }
    },
    *create ({ payload }, { call, put }) {
      yield put({ type: 'hideModal' });
    },
    *update ({ payload }, { select, call, put }) {
      yield put({ type: 'hideModal' });
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen(location => {
        dispatch({
          type: 'query',
          payload: location.query,
        })
      })
    },
  },
};
