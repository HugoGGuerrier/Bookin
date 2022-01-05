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
  </div>
</template>

<script>
import M from 'materialize-css'

export default {
  name: 'Search',

  data () {
    const q = ''
    const advanced = false
    return { q, advanced }
  },

  mounted () {
    M.AutoInit()
  },

  methods: {
    sendQuery () {
      M.toast({ html: 'Send the query "' + this.q + '" in ' + (this.advanced ? 'advanced' : 'simple') + ' mode' })
    }
  }
}
</script>
