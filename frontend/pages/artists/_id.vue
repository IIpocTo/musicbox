<template>
    <div>
        <v-progress-linear v-if="loading" indeterminate color="primary"></v-progress-linear>
        <v-alert :value="true" color="error" v-if="error && !loading">{{ error }}</v-alert>
        <template v-if="data && !loading">
            <v-card>
                <v-card-text v-if="data && !loading">
                    <v-layout row wrap>
                        <v-flex
                            xs6 offset-xs3
                            md5 offset-md0
                            lg4 offset-lg0
                            class="pa-3"
                        >
                            <img :src="data.image" :alt="data.name" width="100%">
                        </v-flex>
                        <v-flex
                            xs12 md7 lg8
                            class="pa-3"
                        >
                            <div class="title">{{ data.name }}</div>
                            <div class="body-1">{{ data.bio || '(биография отсутствует)' }}</div>
                            <div class="mt-3">
                                <span class="body-2">Жанры:</span>
                                <v-divider />
                                <v-chip
                                    v-for="(genre, i) in genresTranslated"
                                    :key="i"
                                    label
                                    dark color="primary"
                                    class="white--text"
                                ><v-icon left>label</v-icon>{{ genre }}</v-chip>
                                <v-divider />
                            </div>
                        </v-flex>
                    </v-layout>
                </v-card-text>
            </v-card>
            <div class="mt-3 subheading">Популярные альбомы</div>
            <v-divider class="mb-4"></v-divider>
            <card-grid
                v-if="data && !loading"
                :items="albums"
            >
                <template slot-scope="{ item }">
                    <album-card :album="item" height="100%"></album-card>
                </template>
                <div slot="no-data" class="text-xs-center">(альбомов не найдено)</div>
            </card-grid>
        </template>
    </div>
</template>
<script>
import GenreRenderer from '@/mixins/GenreRenderer';
import CardGrid from '@/components/common/CardGrid';
import AlbumCard from '@/components/AlbumCard';

export default {
    name: 'ArtistPage',
    mixins: [GenreRenderer('data')],
    data() {
        return {
            loading: false,
            error: null,
            data: null,
            albums: []
        };
    },
    computed: {
        artistId() {
            return this.$router.currentRoute.params.id;
        }
    },
    methods: {
        async getArtist() {
            const id = this.artistId;
            this.loading = true;
            try {
                const result = await this.$store.dispatch('remote/getById', {
                    name: 'artists',
                    id
                });
                if (result) {
                    this.data = result;
                    this.error = null;
                } else {
                    throw new Error('Указанного исполнителя не существует');
                }

                const popularAlbums = result.albumsIds;
                const queries = popularAlbums.map(
                    id => this.$store.dispatch('remote/getById', {
                        name: 'albums',
                        id
                    })
                );
                const results = await Promise.all(queries);
                console.log(results);
                this.albums = results;
            } catch (error) {
                this.error = error.message || String(error);
                this.data = null;
            }
            this.loading = false;
        }
    },
    async beforeMount() {
        await this.getArtist();
    },
    watch: {
        artistId() {
            this.getArtist();
        }
    },
    components: {
        CardGrid,
        AlbumCard
    }
};
</script>
