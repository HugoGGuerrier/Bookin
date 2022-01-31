<template>
  <div class="card hoverable small left-align" @click="download">
    <div class="card-content grey-text">
      <span class="card-title grey-text text-darken-2">{{ title }}</span>
      <p class="grey-text text-darken-2">{{ authors.join(" | ") }}</p>
      <p>{{ lang }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BookCard',

  props: {
    title: String,
    authors: Array,
    lang: String
  },

  methods: {
    download () {
      // DO the download of the book
    }
  }
}
</script>
