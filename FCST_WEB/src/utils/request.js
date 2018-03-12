import Ajax from 'robe-ajax'

export default function request (options) {
  var url = options.url;
  var data = options.paramPosition === 'BODY' ? JSON.stringify(options.data) : (options.data || {})

  return Ajax.ajax({
    url,
    method: options.method || 'get',
    traditional: true,
    dataType: 'JSON',
    data: data,
  }).done((data) => {
    return data
  })
}
