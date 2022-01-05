<template>
  <div class="home center-align">
    <img
      alt="Bookin logo"
      src="logo.png"
    >
    <h2>Welcome on Bookin, the online open library project</h2>
    <p v-if="dev">DEV MODE</p>
  </div>
</template>

<script>

export default {
  name: 'Home',

  data () {
    const dev = process.env.NODE_ENV === 'development'
    return { dev }
  }
}
</script>

<style scoped>

.home img {
  height: 200px;
  margin-top: 2rem;
}

</style>
