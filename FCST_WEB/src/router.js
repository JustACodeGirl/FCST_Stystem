import React, { PropTypes } from 'react'
import { Router } from 'dva/router'
import App from './routes/app'

const cached = {};
const registerModel = (app, model) => {
  if (!cached[model.namespace]) {
    app.model(model)
    cached[model.namespace] = 1
  }
}

const Routers = function ({ history, app }) {
  const routes = [
    {
      path: '/',
      component: App,
      getIndexRoute (nextState, cb) {
        require.ensure([], require => {
          //registerModel(app, require('./models/fcstSync'))
          cb(null, { component: require('./routes/fcstSync') })
        }, 'fcstSync')
      },
      childRoutes: [
        {
          path: 'login',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/login'))
              cb(null, require('./routes/login/'))
            }, 'login')
          },
        }, {
          path: 'fcstSync',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              cb(null, require('./routes/fcstSync'))
            }, 'fcstSync')
          },
        },{
          path: 'analyse/dataQuery',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/dataQuery'))
              cb(null, require('./routes/analyse/dataQuery'))
            }, 'analyse-dataQuery')
          },
        },{
          path: 'analyse/quarterly',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/quarterly'))
              cb(null, require('./routes/analyse/quarterly'))
            }, 'analyse-quarterly')
          },
        }, {
          path: 'analyse/parts',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/parts'))
              cb(null, require('./routes/analyse/parts'))
            }, 'analyse-parts')
          },
        }, {
          path: 'analyse/customer',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/customer'))
              cb(null, require('./routes/analyse/customer'))
            }, 'analyse-customer')
          },
        }, {
          path: 'schedule',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/schedule'))
              cb(null, require('./routes/schedule'))
            }, 'schedule')
          },
        }, {
          path: 'followup/scheme',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/followup/schemes'))
              cb(null, require('./routes/followup/scheme'))
            }, 'followup-scheme')
          },
        }, {
          path: 'admin/role',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/admin/role'))
              cb(null, require('./routes/admin/role'))
            }, 'admin-role')
          },
        }, {
          path: 'admin/list',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/admin/list'))
              cb(null, require('./routes/admin/list'))
            }, 'admin-list')
          },
        }, {
          path: '*',
          name: 'error',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              cb(null, require('./routes/error'))
            }, 'error')
          },
        },
      ],
    },
  ]

  return <Router history={history} routes={routes} />
}

Routers.propTypes = {
  history: PropTypes.object,
  app: PropTypes.object,
}

export default Routers
