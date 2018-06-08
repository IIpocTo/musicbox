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
import {mapActions, mapGetters} from 'vuex';

export default {
    name: 'AlbumsPage',
    beforeMount() {
        this.getAlbums();
    },
    computed: {
        ...mapGetters({
            albums: 'albums/albums'
        })
    },
    methods: {
        showAlbum() {

        },
        ...mapActions({
            getAlbums: 'albums/getAlbums'
        })
    }
};
</script>
