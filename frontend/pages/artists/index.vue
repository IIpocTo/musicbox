<template>
    <div>
        <card-grid :items="artists" no-data-full-width>
            <template slot-scope="{ item }">
                <artist-card :artist="item" height="100%"></artist-card>
            </template>
            <v-alert :value="true" type="info" slot="no-data">
                Не найдено исполнителей
            </v-alert>
        </card-grid>
        <v-layout v-if="!wholeLoaded">
            <v-spacer />
            <v-btn
                large outline color="primary"
                :loading="loading"
                @click="fetchMore"
            >Загрузить ещё...</v-btn>
            <v-spacer />
        </v-layout>
    </div>
</template>
<script>
import CardGrid from '@/components/common/CardGrid';
import ArtistCard from '@/components/ArtistCard';

export default {
    name: 'ArtistsPage',
    data() {
        return {
            page: 1,
            limit: 12,
            loading: false,
            wholeLoaded: false
        };
    },
    beforeMount() {
        this.getArtists();
    },
    methods: {
        async getArtists() {
            this.loading = true;
            try {
                const n = await this.$store.dispatch('remote/list', {
                    name: 'artists',
                    page: this.page,
                    limit: this.limit
                });
                if (n === 0) {
                    this.wholeLoaded = true;
                }
            } catch (error) {
                this.$store.commit('showError', { error, text: 'Не удалось загрузить исполнителей' });
            }
            this.loading = false;
        },
        fetchMore() {
            this.page++;
            this.getArtists();
        },
        showArtist(id) {
            console.log(id);
            this.$router.push(`/artists/${id}`);
        }
    },
    computed: {
        artists() {
            return this.$store.state.remote.artists;
        }
    },

    components: {
        ArtistCard,
        CardGrid
    }
};
</script>
