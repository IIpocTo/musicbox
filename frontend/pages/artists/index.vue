<template>
    <card-grid :items="artists" no-data-full-width>
        <template slot-scope="{ item }">
            <artist-card :artist="item" height="100%"></artist-card>
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
    data() {
        return {
            page: 1,
            limit: 50
        };
    },
    beforeMount() {
        this.getArtists({ page: this.page, limit: this.limit });
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
