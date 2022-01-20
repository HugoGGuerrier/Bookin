<template>
<div class="container">
  <div class="row">
    <h3 class="col s12 center-align grey-text text-darken-3">
      Admin page
    </h3>
  </div>

  <div class="row">
    <div class="col s12">
      <ul class="collapsible z-depth-0">
        <li>
          <div class="collapsible-header">
            <i class="material-icons">note_add</i>Add a book
          </div>
          <div class="collapsible-body">
            <div class="input-field">
              <input
                id="title"
                type="text"
                v-model="title"
              />
              <label for="title">Book title</label>
            </div>

            <div class="input-field">
              <input
                id="authors"
                type="text"
                v-model="authors"
              />
              <label for="authors">Authors (separated by commas)</label>
            </div>

            <div class="input-field">
              <input
                id="lang"
                type="text"
                v-model="lang"
              />
              <label for="lang">Language of the book</label>
            </div>

            <div class="file-field input-field">
              <div class="btn white grey-text text-darken-1">
                <span class="">Book file</span>
                <input id="book-file-input" type="file" accept=".txt, text/plain" />
              </div>
              <div class="file-path-wrapper">
                <input class="file-path" type="text" v-model="filePath" />
              </div>
            </div>

            <button class="btn waves-effect waves-light green lighten-1" @click="uploadBook">Submit
              <i class="material-icons right">send</i>
            </button>
          </div>
        </li>
      </ul>
    </div>
  </div>
</div>
</template>

<script>
import M from 'materialize-css'
import axios from 'axios'

export default {
  name: 'Admin',

  data () {
    const title = ''
    const authors = ''
    const lang = ''
    const filePath = ''

    return { title, authors, lang, filePath }
  },

  mounted () {
    if (sessionStorage.getItem('isAdmin') !== 'true') {
      this.$router.push('login')
    }
    M.AutoInit()
  },

  methods: {

    uploadBook () {
      const fileInput = document.getElementById('book-file-input')

      if (this.title === '' || this.authors === '' || this.lang === '' || fileInput.files[0] === undefined) {
        M.toast({ html: 'Please, fill all book information' })
        return
      }

      const formData = new FormData()
      formData.append('title', this.title)
      formData.append('authors', this.authors.split(','))
      formData.append('lang', this.lang)
      formData.append('file', fileInput.files[0])

      axios.post(this.api + process.env.VUE_APP_BOOK_ENDPOINT, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(() => {
          M.toast({ html: 'Book added !' })
          this.title = ''
          this.authors = ''
          this.lang = ''
          fileInput.files = []
          this.filePath = ''
        })
        .catch(e => {
          M.toast({ html: 'Book adding failed' })
          console.log(e)
        })
    }
  }
}
</script>
