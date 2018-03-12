import { getFcstWeek, getBucketDate, getAccountManager } from '../services/common'
import { query } from '../services/parts'
import { handledata } from '../utils/handle'
import { parse } from 'qs'

export default {

  namespace: 'parts',

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
    partsQueryData: {},
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen(location => {
        dispatch({
          type: 'partsInit',
          payload: location.query,
        })
      })
    },
  },


  effects: {
    *query ({ payload }, { call, put }) {
      const params = {
        confidenceLevel: payload.confidencelevel || '',
        fcstWeek: payload.forecast || '',
        accountManager: payload.accountmanager || '',
        bucketDates: payload.selectDate || '',
      }
      const data = yield call(query, parse(params))
      if (data) {
        yield put({
          type: 'querySuccess',
          payload: {
            list: handledata(data.data),
            pagination: data.page,
            partsQueryData: {
              selectDates: params.bucketDates,
            },
          },
        })
      }
    },
    *partsInit ({ payload }, { call, put }) {
      const fcstWeek = yield call(getFcstWeek, {})
      const managers = yield call(getAccountManager, {})
      if (fcstWeek && managers) {
        yield put({
          type: 'querySuccess',
          payload: {
            list: [],
            pagination: {},
            partsQueryData: {
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
            partsQueryData: {
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
      const { list, pagination, partsQueryData } = action.payload
      return { ...state,
        list,
        pagination: {
          ...state.pagination,
          ...pagination,
        },
        partsQueryData: {
          ...state.partsQueryData,
          ...partsQueryData,
        },
      }
    },
  },

}
