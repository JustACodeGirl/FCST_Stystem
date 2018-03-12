import { getFcstWeek, getBucketDate, getAccountManager } from '../services/common'
import { query } from '../services/customer'
import { handleCustomerdata } from '../utils/handle'
import { parse } from 'qs'

export default {

  namespace: 'customer',

  state: {
    list: [],
    pagination: {
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: total => `共 ${total} 条`,
      current: 1,
      total: null,
    },
    queryInfo: {},
    customerQueryData: {},
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen(location => {
        dispatch({
          type: 'customerInit',
          payload: location.query,
        })
      })
    },
  },


  effects: {
    *query ({ payload }, { call, put }) {
      const params = {
        region: payload.region || '',
        confidenceLevel: payload.confidencelevel || '',
        fcstWeek: payload.forecast || '',
        accountManager: payload.accountmanager || '',
        bucketDates: payload.selectDate || '',
      }
      const data = yield call(query, parse(params))
      if (data) {
        const resData = handleCustomerdata(data.data)
        yield put({
          type: 'querySuccess',
          payload: {
            list: resData.arrs,
            pagination: false,
            customerQueryData: {
              selectDates: resData.title,
            },
          },
        })
      }
    },
    *customerInit ({ payload }, { call, put }) {
      const fcstWeek = yield call(getFcstWeek, {})
      const managers = yield call(getAccountManager, {})
      if (fcstWeek && managers) {
        yield put({
          type: 'querySuccess',
          payload: {
            list: [],
            pagination: {},
            customerQueryData: {
              fcstWeekDate: fcstWeek,
              accountmanager: managers,
              bucketDate: '',
            },
          },
        })
      }
    },
    *select ({ payload }, { call, put }) {
      const bucket = yield call(getBucketDate, { fcstWeek: payload })
      if (bucket) {
        yield put({
          type: 'querySuccess',
          payload: {
            list: [],
            pagination: {},
            customerQueryData: {
              fcstWeekDate: payload,
              bucketDate: bucket.data,
            },
          },
        })
      }
    },
  },

  reducers: {
    querySuccess (state, action) {
      const { list, pagination, customerQueryData } = action.payload
      return { ...state,
        list,
        pagination: {
          ...state.pagination,
          ...pagination,
        },
        customerQueryData: {
          ...state.customerQueryData,
          ...customerQueryData,
        },
      }
    },
  },

}
