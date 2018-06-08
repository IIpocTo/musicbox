<template>
    <div>Artist {{ album.id }}</div>
</template>
<script>
import {mapActions, mapGetters} from 'vuex';

export default {
    name: 'AlbumPage',
    beforeMount() {
        this.getAlbum();
    },
    methods: {
        ...mapActions({
            getAlbum: 'album/getAlbum'
        })
    },
    computed: {
        ...mapGetters({
            albums: 'album/album'
        })
    }
};
</script>
