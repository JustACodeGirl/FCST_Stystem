import React, { PropTypes } from 'react'
import { Menu, Icon, Popover, Badge } from 'antd'
import { Link } from 'dva/router'
import styles from './main.less'
import Menus from './menu'

const SubMenu = Menu.SubMenu

function Header ({ user, logout, switchSider, siderFold, isNavbar, menuPopoverVisible, location, switchMenuPopover, navOpenKeys, changeOpenKeys }) {
  let handleClickMenu = e => {
    switch (e.key) {
      case 'logout': logout()
      break
    }
  };
  const menusProps = {
    siderFold: false,
    darkTheme: false,
    isNavbar,
    handleClickNavMenu: switchMenuPopover,
    location,
    navOpenKeys,
    changeOpenKeys,
  }
  return (
    <div className={styles.header}>
      {isNavbar
        ? <Popover placement="bottomLeft" onVisibleChange={switchMenuPopover} visible={menuPopoverVisible} overlayClassName={styles.popovermenu} trigger="click" content={<Menus {...menusProps} />}>
          <div className={styles.siderbutton}>
            <Icon type="bars" />
          </div>
        </Popover>
        : <div className={styles.siderbutton} onClick={switchSider}>
          <Icon type={siderFold ? 'menu-unfold' : 'menu-fold'} />
        </div>}

      <Menu className="header-menu" mode="horizontal" onClick={handleClickMenu}>
        <SubMenu style={{
          float: 'right',
        }} title={<span> <Icon type="user" />
          {user.userCode} </span>}
        >
          <Menu.Item key="logout">
            <a>注销</a>
          </Menu.Item>
        </SubMenu>
        <Menu.Item key="calendar" className={styles.sidermenu}>
          <Link to={'/schedule'}>
            <Badge count={0} style={{top: '-15px'}}>
              <span><Icon type="calendar"/>日程</span>
            </Badge>
          </Link>
        </Menu.Item>
      </Menu>
    </div>
  )
}

Header.propTypes = {
  user: PropTypes.object,
  logout: PropTypes.func,
  switchSider: PropTypes.func,
  siderFold: PropTypes.bool,
  isNavbar: PropTypes.bool,
  menuPopoverVisible: PropTypes.bool,
  location: PropTypes.object,
  switchMenuPopover: PropTypes.func,
  navOpenKeys: PropTypes.array,
  changeOpenKeys: PropTypes.func,
}

export default Header
