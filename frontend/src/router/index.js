import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import About from '@/views/About.vue'
import Login from '@/views/Login.vue'
import Search from '@/views/Search.vue'
import Admin from '@/views/Admin.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/search',
    name: 'Search',
    component: Search
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.afterEach((to, from, fail) => {
  if (!fail) {
    const navTo = document.getElementById(to.name?.toString() + '-nav')
    const navFrom = document.getElementById(from.name?.toString() + '-nav')
    if (navFrom) navFrom.classList.remove('active')
    if (navTo) navTo.classList.add('active')
  }
})

export default router
