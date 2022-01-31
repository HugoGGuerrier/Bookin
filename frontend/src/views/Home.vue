<template>
  <div class="home center-align">
    <img
      alt="Bookin logo"
      src="logo.png"
    >
    <h2>Welcome on Bookin, the online open library project</h2>

    <h3 style="margin-top: 7rem">Suggestions for you</h3>
    <div class="container">

      <div class="row">
        <div class="col s12 m4 l3" v-for="book of books" :key="book.id">
          <book-card :title="book.title" :authors="book.authors" :lang="book.lang" />
        </div>
      </div>

    </div>
  </div>
</template>

<script lang="js">
import axios from 'axios'
import BookCard from '../components/BookCard.vue'

export default {
  name: 'Home',

  components: { BookCard },

  data () {
    const books = []
    return { books }
  },

  mounted () {
    this.getSuggestion()
  },

  methods: {
    getSuggestion () {
      axios.get(this.api + process.env.VUE_APP_SUGGESTION_ENDPOINT)
        .then(r => {
          this.books = r.data
        })
    }
  }
}
</script>

<style lang="css" scoped>

.home img {
  height: 200px;
  margin-top: 2rem;
}

</style>
