//全局注册组件
import Vue from 'vue'

// https://webpack.js.org/guides/dependency-management/#require-context
const componentsContext = require.context('./', false, /[\w-]+.(vue)$/)
componentsContext.keys().forEach(component => {
  // 组件配置信息
  const componentConfig = componentsContext(component).default
  Vue.component(componentConfig.name, componentConfig)
})