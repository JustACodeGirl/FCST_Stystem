/**
 * Created by leo on 2017/3/19.
 */
import React from 'react'
import $ from 'jquery';
import 'moment/min/moment.min.js';

import 'fullcalendar/dist/fullcalendar.js';
import 'fullcalendar/dist/fullcalendar.css';
import 'fullcalendar/dist/locale-all';

import styles from './calendar.less';

class Calendar extends React.Component {
  componentDidUpdate() {
    const { calendar } = this.refs;
    $(calendar).fullCalendar('removeEventSources');
    $(calendar).fullCalendar('addEventSource', this.props.events);
  }

  handleEventEdit = (event, delta, revertFunc) => {

  };

  componentDidMount() {
    const { events, onAdd, onEditItem, onDeleteItem } = this.props;
    const { calendar } = this.refs;
    $(calendar).fullCalendar({
      header: {
        left: 'prev,next today',
        center: 'title',
        right: 'month,agendaWeek,agendaDay'
      },
      height: $('.content-inner').height(),
      locale: 'zh-cn',
      selectable: true,
      selectHelper: true,
      select: function(start, end) {
        onAdd(start, end);
        $(calendar).fullCalendar('unselect');
      },
      eventClick: function (event) {
        onEditItem(event);
      },
      editable: true,
      eventResize: this.handleEventEdit,
      eventDrop: this.handleEventEdit,
      navLinks: true, // can click day/week names to navigate views
      eventLimit: true, // allow "more" link when too many events
      eventSources: {
        events: events
      }
    })
  }

  render() {
    return (
      <div ref="calendar" className={styles.calendar}></div>
    )
  }
}

export default Calendar
