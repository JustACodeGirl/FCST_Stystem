/**
 * Created by leo on 2017/3/19.
 */
import React from 'react'
import { Card, Icon } from 'antd'
import styles from './external.less'

class External extends React.Component {
  componentDidMount() {

  }
  handleDeleteItem = (id) => {
    this.props.onDeleteItem(id);
  };
  render() {
    const events = this.props.events;
    const eventList = events.map((event, index) => {
      return (
        <Card bodyStyle={{padding: 10}} style={{margin: 4}} key={index}>
          <p>{event.title}</p>
          <p style={{color:'#aaaaaa'}}>{event.start} 至 {event.end || '-'}
            <a style={{float: 'right', color:'#ff0000'}} onClick={() => this.handleDeleteItem(event.id)}><Icon type="delete"/></a>
          </p>
        </Card>
      );
    });

    return (
      <div className={styles.external}>
        {eventList}
      </div>
    )
  }
}

export default External
