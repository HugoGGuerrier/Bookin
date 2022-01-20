<template>
  <div
    class="container"
    style="margin-top : 2.5rem;"
  >
    <div class="row">
      <div class="col s12">
        <div class="card">
          <div class="card-content grey-text text-darken-3">
            <span
              class="card-title"
              style="margin-bottom: 2rem;"
            >Login as an admin</span>

            <div class="input-field">
              <input
                id="last_name"
                type="text"
                v-model="mail"
              >
              <label for="last_name">Email</label>
            </div>

            <div class="input-field">
              <input
                id="password"
                type="password"
                v-model="password"
              >
              <label for="password">Password</label>
            </div>

            <a
              class="waves-effect waves-light btn green"
              style="margin-top: 1rem;"
              @click="login"
            >
              <i class="material-icons left">check</i>
              login
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import M from 'materialize-css'
import axios from 'axios'

export default {
  name: 'Login',

  data () {
    const mail = ''
    const password = ''
    return { mail, password }
  },

  mounted () {
    M.AutoInit()
  },

  methods: {

    login () {
      if (this.mail === '' || this.password === '') {
        this.wrongInput()
        return
      }

      const formData = new FormData()
      formData.append('mail', this.mail)
      formData.append('pass', this.password)

      axios.post(this.api + process.env.VUE_APP_LOGIN_ENDPOINT, formData)
        .then(this.success)
        .catch(this.fail)
    },

    wrongInput () {
      M.toast({ html: 'Mail and password cannot be empty' })
    },

    success () {
      sessionStorage.setItem('isAdmin', 'true')
      this.updateAdminNav()
      this.$router.push('admin')
    },

    fail (e) {
      M.toast({ html: e.response.data.msg })
    }

  }
}
</script>
