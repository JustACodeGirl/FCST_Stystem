/**
 * Created by leo on 2017/3/19.
 */
import React, { PropTypes } from 'react'
import { connect } from 'dva'
import Calendar from '../components/schedule/calendar'
import External from '../components/schedule/external'
import EventModal from '../components/schedule/modal'
import styles from './schedule.less'

function Schedule({ location, dispatch, schedule, loading }) {
  const { eventList, currentItem, modalType, modalVisible } = schedule;

  function onDeleteItem (id) {
    dispatch({
      type: 'schedule/delete',
      payload: id,
    })
  }

  const eventModalProps = {
    item: currentItem,
    type: modalType,
    visible: modalVisible,
    onOk (data) {
      dispatch({
        type: `schedule/${modalType}`,
        payload: data,
      })
    },
    onCancel () {
      dispatch({
        type: 'schedule/hideModal',
      })
    },
  };

  const eventListProps = {
    events: eventList,
    onEditItem (item) {
      dispatch({
        type: 'schedule/showModal',
        payload: {
          modalType: 'update',
          currentItem: item,
        },
      })
    },
    onAdd (start, end) {
      dispatch({
        type: 'schedule/showModal',
        payload: {
          modalType: 'create',
          currentItem: {start, end},
        },
      })
    },
    onDeleteItem (id) {
      dispatch({
        type: 'schedule/delete',
        payload: id,
      })
    },
  };

  return (
    <div className="content-inner">
      <div className={styles.schedule}>
        <div className={styles.left}>
          <Calendar {...eventListProps}/>
        </div>
        <div className={styles.right}>
          <External events={eventList} onDeleteItem={onDeleteItem}/>
        </div>
      </div>
      <EventModal {...eventModalProps}/>
    </div>
  )
}

Schedule.propTypes = {
  schedule: PropTypes.object,
  location: PropTypes.object,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
};

export default connect(({ schedule, loading }) => ({ schedule, loading: loading.models.schedule }))(Schedule)
