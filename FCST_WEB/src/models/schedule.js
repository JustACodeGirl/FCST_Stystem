import { query, create, remove, update } from '../services/schedule'
import { parse } from 'qs'

export default {

  namespace: 'schedule',

  state: {
    eventList: [],
    currentItem: {},
    modalVisible: false,
    modalType: 'create',
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      dispatch({ type: 'query' })
    },
  },

  effects: {
    *query ({ payload }, { call, put }) {
      const data = yield call(query, parse(payload));
      if (data) {
        yield put({
          type: 'querySuccess',
          payload: {
            eventList: data.data
          },
        })
      }
    },
    *'delete' ({ payload }, { call, put }) {
      const data = yield call(remove, { id: payload });
      if (data && data.success) {
        yield put({
          type: 'querySuccess',
          payload: {
            eventList: data.data,
          },
        })
      }
    },
    *create ({ payload }, { call, put }) {
      yield put({ type: 'hideModal' });
      const data = yield call(create, payload);
      if (data && data.success) {
        yield put({
          type: 'querySuccess',
          payload: {
            eventList: data.data,
          },
        })
      }
    },
    *update ({ payload }, { select, call, put }) {
      yield put({ type: 'hideModal' });
      const id = yield select(({ schedules }) => schedules.currentItem.id);
      const newSchedule = { ...payload, id };
      const data = yield call(update, newSchedule);
      if (data && data.success) {
        yield put({
          type: 'querySuccess',
          payload: {
            eventList: data.data,
          },
        })
      }
    }
  },

  reducers: {
    querySuccess (state, action) {
      const { eventList } = action.payload;
      return { ...state, eventList }
    },
    showModal (state, action) {
      return { ...state, ...action.payload, modalVisible: true }
    },
    hideModal (state) {
      return { ...state, modalVisible: false }
    }
  },

}
