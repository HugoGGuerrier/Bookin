import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import 'materialize-css/dist/css/materialize.min.css'
import 'material-design-icons/iconfont/material-icons.css'
import axios from 'axios'

// --- Set the axios credential config
axios.defaults.withCredentials = true

// Create the Vue application
const app = createApp(App)

// --- Set the global variables
const dev = process.env.NODE_ENV === 'development'
app.config.globalProperties.dev = dev

// Set the application api address between dev and prod
let api = ''
if (dev) api = process.env.VUE_APP_DEV_API
else api = process.env.VUE_APP_PROD_API
app.config.globalProperties.api = api

const mixins = {
  methods: {
    updateAdminNav () {
      const isAdmin = sessionStorage.getItem('isAdmin')
      const navLogin = document.getElementById('Login-nav')
      const navAdmin = document.getElementById('Admin-nav')
      if (isAdmin === 'true') {
        navAdmin.classList.remove('hide')
        navLogin.classList.add('hide')
      } else {
        navAdmin.classList.add('hide')
        navLogin.classList.remove('hide')
      }
    }
  }
}

// Initialise the router and mount the application
app.use(router).mixin(mixins).mount('#app')
