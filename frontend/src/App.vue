<template>
  <div>
    <nav>
      <div class="nav-wrapper">
        <router-link to="/" class="left brand-logo">LOGO</router-link>
        <ul id="nav-mobile" class="right hide-on-med-and-down">
          <li><router-link to="/search"><i class="material-icons">search</i></router-link></li>
          <li><router-link to="/about"><i class="material-icons">info</i></router-link></li>
        </ul>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<style>
</style>
