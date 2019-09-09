// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import Vuetify from 'vuetify';
import config from './config';
import './http';
import 'vuetify/dist/vuetify.min.css';
import './assets/material.css';

Vue.use(Vuetify, { theme: config.theme });
Vue.prototype.$appConfig = config.appConfig;

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
