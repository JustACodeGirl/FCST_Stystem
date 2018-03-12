import { query } from '../services/quarterly'
import { getFcstWeek, getBucketDate } from '../services/common'
import { parse } from 'qs'
import { handleQuarterlydata } from '../utils/handle'

export default {

  namespace: 'quarterly',

  state: {
    list: [],
    pagination: false,
    queryInfo: {},
    quarterlyQueryData: {},
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen(location => {
        dispatch({
          type: 'quarterlyInit',
          payload: location.query,
        })
      })
    },
  },


  effects: {
    *quarterlyInit ({ payload }, { call, put }) {
      const fcstWeek = yield call(getFcstWeek, {})
      if (fcstWeek) {
        yield put({
          type: 'querySuccess',
          payload: {
            list: [],
            pagination: {},
            quarterlyQueryData: {
              fcstWeekDate: fcstWeek,
            },
          },
        })
      }
    },
    *query ({ payload }, { call, put }) {
      const params = {
        fcstWeek: payload.forecast,
        bucketDates: payload.selectDate,
      }
      const data = yield call(query, parse(params))
      if (data) {
        const resData = handleQuarterlydata(data.data)
        yield put({
          type: 'querySuccess',
          payload: {
            list: resData.arrs,
            pagination: false,
            quarterlyQueryData: {
              selectDates: resData.title,
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
            pagination: false,
            quarterlyQueryData: {
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
      const { list, pagination, quarterlyQueryData } = action.payload
      return { ...state,
        list,
        pagination,
        quarterlyQueryData: {
          ...state.quarterlyQueryData,
          ...quarterlyQueryData,
        },
      }
    },
  },

}
