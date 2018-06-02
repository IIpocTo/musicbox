<template>
    <div>
        <v-card
                hover
                raised
                v-for="(value, key) in albums" :key="key"
                @click.prevent="() => showAlbum(value.id)"
        >
            {{ value.name }}
        </v-card>
    </div>
</template>
<script>
export default {
    name: 'AlbumsPage',
    data() {
      return {
        albums: []
      };
    },
    methods: {
      showAlbum() {

      }
    }
};
</script>
