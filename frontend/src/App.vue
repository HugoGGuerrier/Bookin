<template>
  <div>
    <nav class="blue-grey darken-2">
      <div class="nav-wrapper">
        <router-link
          to="/"
          class="left brand-logo"
        >
          Bookin
        </router-link>
        <ul
          id="nav-mobile"
          class="right hide-on-med-and-down"
        >
          <li id="Search-nav">
            <router-link to="/search">
              <i class="material-icons">search</i>
            </router-link>
          </li>
          <li
            v-if="isAdmin"
            id="Admin-nav"
          >
            <router-link to="/admin">
              <i class="material-icons">edit</i>
            </router-link>
          </li>
          <li
            v-else
            id="Login-nav"
          >
            <router-link to="/login">
              <i class="material-icons">lock</i>
            </router-link>
          </li>
          <li id="About-nav">
            <router-link to="/about">
              <i class="material-icons">info</i>
            </router-link>
          </li>
        </ul>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<script>
import { computed } from '@vue/reactivity'

export default {
  name: 'App',

  setup () {
    const isAdmin = computed(() => sessionStorage.getItem('isAdmin') === 'true')
    return { isAdmin }
  }
}
</script>

<style lang="css">

nav .nav-wrapper .brand-logo {
  padding-left: 0.7rem;
}

.input-field input {
  border-bottom: 1px solid #424242 !important;
  box-shadow: 0 0px 0 0 #424242 !important;
  color: #424242;
}

.input-field input:focus {
  border-bottom: 1px solid #424242 !important;
  box-shadow: 0 0px 0 0 #424242 !important;
  color: #424242;
}

.input-field label {
  color: #424242a9 !important;
}

.input-field input:focus + label {
  color: #424242 !important;
}

.input-field .prefix {
  color: #424242 !important;
}

</style>
