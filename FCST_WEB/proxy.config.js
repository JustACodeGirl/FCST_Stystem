// let mock = {}
//
// require('fs').readdirSync(require('path').join(`${__dirname}/mock`)).forEach((file) => {
//   Object.assign(mock, require('./mock/' + file))
// })
//
// mock = Object.assign({}, mock, {
//   'POST /api/upload': 'http://10.88.17.79:8080/fcst/api/upload',
// })
//
// module.exports = mock
module.exports = {

  //'POST /api/*': 'http://localhost:18080/SaleForcast/',
  'POST /api/*': 'http://116.211.106.187:8383/fcst/',
  'GET /api/*': 'http://116.211.106.187:8383/fcst/',
}
