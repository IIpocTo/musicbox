<template>
    <card-grid :items="artists" no-data-full-width>
        <template slot-scope="{ item }">
            <artist-card :artist="item"></artist-card>
        </template>
        <v-alert :value="true" type="info" slot="no-data">
            Не найдено исполнителей
        </v-alert>
    </card-grid>
</template>
<script>
// eslint-disable-next-line no-unused-vars
import { mapActions, mapGetters } from 'vuex';
import CardGrid from '@/components/common/CardGrid';
import ArtistCard from '@/components/ArtistCard';

export default {
    name: 'ArtistsPage',
    beforeMount() {
        this.getArtists();
    },
    methods: {
        ...mapActions({
            getArtists: 'artists/getArtists'
        }),
        showArtist(id) {
            console.log(id);
            this.$router.push(`/artists/${id}`);
        }
    },
    computed: {
        ...mapGetters({
            artists: 'artists/artists'
        })
    },

    components: {
        ArtistCard,
        CardGrid
    }
};
</script>
