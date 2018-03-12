import React from 'react'
import { Input, Select, Checkbox } from 'antd';
import styles from './followupPathInput.less';

const Option = Select.Option;
const CheckboxGroup = Checkbox.Group;

class FollowupPathInput extends React.Component {
  constructor(props) {
    super(props);

    const value = this.props.value || {};
    this.state = {
      id: value.id || 1,
      dateValue: value.dateValue || 1,
      dateUnit: value.dateUnit || '1',
      todo: value.todo || ["1", "2"],
    };
  }
  componentWillReceiveProps(nextProps) {
    // Should be a controlled component.
    if ('value' in nextProps) {
      const value = nextProps.value;
      this.setState(value);
    }
  }
  handleValueChange = (e) => {
    const dateValue = parseInt(e.target.value || 0, 10);
    if (isNaN(dateValue)) {
      return;
    }
    if (!('value' in this.props)) {
      this.setState({ dateValue });
    }
    this.triggerChange({ dateValue });
  };
  handleUnitChange = (dateUnit) => {
    if (!('value' in this.props)) {
      this.setState({ dateUnit });
    }
    this.triggerChange({ dateUnit });
  };
  handleTodoChange = (checkedValues) => {
    console.log(checkedValues);
  };
  triggerChange = (changedValue) => {
    // Should provide an event to pass value to Form.
    const onChange = this.props.onChange;
    if (onChange) {
      onChange(Object.assign({}, this.state, changedValue));
    }
  };
  render() {
    const { size } = this.props;
    const state = this.state;
    const options = [
      { label: '健康调查简表SF-36', value: "1" },
      { label: '视觉模拟评分', value: "2" },
    ];
    return (
      <span className={styles.pathInput}>
        第&nbsp;
        <Input
          type="text"
          size={size}
          value={state.dateValue}
          onChange={this.handleValueChange}
          style={{ width: 100, marginRight: 2 }}
        />
        <Select
          value={state.dateUnit}
          size={size}
          style={{ width: 80 }}
          onChange={this.handleUnitChange}
        >
          <Option value="1">天</Option>
          <Option value="2">周</Option>
          <Option value="3">月</Option>
          <Option value="4">年</Option>
        </Select>
        &nbsp;&nbsp;随访:
        <CheckboxGroup options={options} value={state.todo} onChange={this.handleTodoChange} />
      </span>
    );
  }
}

export default FollowupPathInput
