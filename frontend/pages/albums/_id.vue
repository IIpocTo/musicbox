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
                            <div class="title">
                                {{ data.name }} <span class="grey--text"> &mdash; {{ artist.name }}</span>
                            </div>
                            <div class="my-3" v-if="releaseDate">
                                <v-icon>calendar_today</v-icon>
                                <span class="body-2 ml-2">Дата выхода:</span>
                                {{ releaseDate }}
                            </div>
                            <v-divider />
                            <m-playlist
                                :tracks="tracks"
                                :name="data.name"
                                show-title show-count
                            ></m-playlist>
                        </v-flex>
                    </v-layout>
                </v-card-text>
            </v-card>
        </template>
    </div>
</template>
<script>
import MPlaylist from '@/components/MPlaylist';

export default {
    name: 'AlbumPage',
    data() {
        return {
            loading: false,
            error: null,
            data: null,
            artist: null
        };
    },
    computed: {
        albumId() {
            return this.$router.currentRoute.params.id;
        },
        releaseDate() {
            const date = this.data.releaseDate;
            if (!date) return false;
            const m = date.month;
            const sm = m < 10 ? '0' + m : '' + m;
            return `${date.day}.${sm}.${date.year}`;
        },

        tracks() {
            return this.data.tracks.map(t => Object.assign(
                {
                    artist: this.artist,
                    album: this.data
                },
                t
            ));
        }
    },
    methods: {
        async getAlbum() {
            const id = this.albumId;
            this.loading = true;
            try {
                const result = await this.$store.dispatch('remote/getById', {
                    name: 'albums',
                    id
                });
                if (result) {
                    this.data = result;
                    this.error = null;
                } else {
                    throw new Error('Указанного альбома не существует');
                }

                const creator = result.artists[0];
                // const artist = await this.$store.dispatch('remote/getById', {
                //     name: 'artists',
                //     id: creator
                // });
                this.artist = creator;
            } catch (error) {
                this.error = error.message || String(error);
                this.data = null;
            }
            this.loading = false;
        }
    },
    async beforeMount() {
        await this.getAlbum();
    },
    watch: {
        artistId() {
            this.getAlbum();
        }
    },
    components: {
        MPlaylist
    }
};
</script>
