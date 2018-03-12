module.exports = [
  // {
  //   key: 'patients',
  //   name: '患者信息',
  //   icon: 'user',
  // },
  {
    key: 'fcstSync',
    name: '数据同步',
    icon: 'swap',
  },
  {
    key: 'analyse',
    name: '查询分析',
    icon: 'bar-chart',
    clickable: false,
    child: [
      {
        key: 'dataQuery',
        name: 'Data',
      },
      {
        key: 'quarterly',
        name: 'Quarterly Overview',
      },
      {
        key: 'parts',
        name: 'By parts',
      },
      {
        key: 'customer',
        name: 'By customer',
      },
    ]
  },
  {
    key: 'schedule',
    name: '日程管理',
    icon: 'calendar',
  },
  // {
  //   key: 'followup',
  //   name: '随访管理',
  //   icon: 'heart-o',
  //   clickable: false,
  //   child: [
  //     {
  //       key: 'scheme',
  //       name: '随访方案',
  //     },
  //     // {
  //     //   key: 'list',
  //     //   name: '待随访列表',
  //     // }
  //   ],
  // },
  {
    key: 'admin',
    name: '用户管理',
    icon: 'lock',
    clickable: false,
    child: [
      {
        key: 'role',
        name: '用户角色',
      },
      {
        key: 'list',
        name: '用户列表',
      }
    ],
  }
]
