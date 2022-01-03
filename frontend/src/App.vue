<template>
  <div>
    <nav class="blue-grey darken-2">
      <div class="nav-wrapper">
        <router-link to="/" class="left brand-logo">Bookin</router-link>
        <ul id="nav-mobile" class="right hide-on-med-and-down">
          <li id="Search-nav"><router-link to="/search"><i class="material-icons">search</i></router-link></li>
          <li id="Login-nav"><router-link to="/login"><i class="material-icons">lock</i></router-link></li>
          <li id="About-nav"><router-link to="/about"><i class="material-icons">info</i></router-link></li>
        </ul>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<style lang="css">

nav .nav-wrapper .brand-logo {
  padding-left: 0.7rem;
}

</style>
