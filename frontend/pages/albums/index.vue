<template>
    <card-grid :items="albums" no-data-full-width>
        <template slot-scope="{ item }">
            <album-card :album="item"></album-card>
        </template>
        <v-alert :value="true" type="info" slot="no-data">
            Не найдено альбомов
        </v-alert>
    </card-grid>
</template>
<script>
import {mapActions, mapGetters} from 'vuex';
import CardGrid from '@/components/common/CardGrid';
import AlbumCard from '@/components/AlbumCard';

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
    },
    components: {
        CardGrid,
        AlbumCard
    }
};
</script>
