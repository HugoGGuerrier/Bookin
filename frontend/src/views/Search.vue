<template>
  <div
    class="container"
    style="margin-top : 2.5rem;"
  >
    <div class="row valign-wrapper">
      <div
        class="input-field col s10"
        style="margin-bottom: 0;"
      >
        <i class="material-icons prefix">search</i>
        <input
          id="icon_prefix"
          type="text"
          class="validate"
          v-model="q"
          @keyup.enter="sendQuery"
        >
        <label for="icon_prefix">Query</label>
      </div>

      <div class="col s2 center-align">
        <p
          class="grey-text text-darken-2"
          style="margin-bottom: 0.5rem; margin-top: 0;"
        >
          Advanced search
        </p>
        <div
          class="switch tooltipped"
          data-position="bottom"
          data-tooltip="Enable the regular expression usage in the query"
        >
          <label>
            Off
            <input
              type="checkbox"
              v-model="advanced"
            >
            <span class="lever" />
            On
          </label>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col s12 m4 l3" v-for="book of books" :key="book.id">
        <book-card :title="book.title" :authors="book.authors" :lang="book.lang" />
      </div>
    </div>
  </div>
</template>

<script>
import M from 'materialize-css'
import axios from 'axios'
import BookCard from '../components/BookCard.vue'

export default {
  name: 'Search',

  components: { BookCard },

  data () {
    const q = ''
    const advanced = false
    const books = []
    return { q, advanced, books }
  },

  mounted () {
    M.AutoInit()
  },

  methods: {

    sendQuery () {
      if (this.q.length < 3) {
        M.toast({ html: 'Query must be more the 2 character' })
        return
      }

      axios.get(this.api + process.env.VUE_APP_BOOK_ENDPOINT, { params: { q: this.q, advanced: this.advanced } })
        .then(r => {
          console.log(r.data)
          this.displayBooks(r.data)
        })
        .catch(e => {
          console.log(e)
          M.toast({ html: 'Error in book searching' })
        })
    },

    displayBooks (books) {
      this.books = books
    }
  }
}
</script>
