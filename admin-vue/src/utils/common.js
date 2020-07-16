import Vue from 'vue'

window.isString = (o)=>{return Object.prototype.toString.call(o) == '[object String]'}
window.isObject = (o)=>{return Object.prototype.toString.call(o) == '[object Object]'}
window.isNumber = (o)=>{return Object.prototype.toString.call(o) == '[object Number]'}
window.isArray = (o)=>{return Object.prototype.toString.call(o) == '[object Array]'}
window.isFunction = (o)=>{return Object.prototype.toString.call(o) == '[object Function]'}

Vue.prototype.$get = function (url, parm){
  let dataParm = parm || {}
  return new Promise((resolve, reject) => {
    this.$http({
      url: this.$http.adornUrl(url),
      method: 'get',
      params: this.$http.adornParams(dataParm)
    }).then(({data}) => {
      if (data && data.code != 0) {
        this.$message(data.msg || '系统异常')
        return
      }
      resolve(data)
    }).catch(err => {
      reject(err)
    })
  })
}

Vue.prototype.$post = function (url, data, parm){
  parm = parm || {}
  let dataParm = isObject(data) ? this.$http.adornData(data) : JSON.stringify(data)
  let option = Object.assign({
    url: this.$http.adornUrl(url),
    method: 'post',
    data: dataParm
  }, parm)
  return new Promise((resolve, reject) => {
    this.$http(option).then(({data}) => {
      if (data && data.code != 0) {
        this.$message(data.msg || '系统异常')
        return
      }
      resolve(data)
    }).catch(err => {
      reject(err)
    })
  })
}
