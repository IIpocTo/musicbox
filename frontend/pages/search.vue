<template>
    <v-card>
        <v-card-title>
            Ваш поисковый запрос: {{ query }}
        </v-card-title>
        <v-tabs
            v-model="tab"
            slider-color="primary"
            fixed-tabs
        >
            <v-tab key="artists">Исполнители</v-tab>
            <v-tab key="albums">Альбомы</v-tab>
            <v-tab ket="tracks">Композиции</v-tab>
        </v-tabs>
        <v-card-text>
            <v-tabs-items v-model="tab">
                <v-tab-item
                    key="artists"
                >
                    <div v-if="Array.isArray(result.artists) && result.artists.length > 0">
                        <v-flex xs6 lg4 v-for="(value, key) in result.artists" :key="key">
                            <artist-card :artist="value"></artist-card>
                        </v-flex>
                    </div>
                    <div v-else>
                        Не найдено исполнителей
                    </div>
                </v-tab-item>
                <v-tab-item
                    key="albums"
                >
                    <div v-if="Array.isArray(result.albums) && result.albums.length > 0">
                        <v-flex xs6 lg4 v-for="(value, key) in result.albums" :key="key">
                            <album-card :album="value"></album-card>
                        </v-flex>
                    </div>
                    <div v-else>
                        Не найдено альбомов
                    </div>
                </v-tab-item>
                <v-tab-item
                    key="tracks"
                >
                    <div v-if="Array.isArray(result.tracks) && result.tracks.length > 0">
                        <v-flex xs6 lg4 v-for="(value, key) in result.tracks" :key="key">
                            Адын трек
                        </v-flex>
                    </div>
                    <div v-else>
                        Не найдено композиций
                    </div>
                </v-tab-item>
            </v-tabs-items>
        </v-card-text>
    </v-card>
</template>
<script>
import connector from '../util/connector';
import ArtistCard from '@/components/ArtistCard';
import AlbumCard from '@/components/AlbumCard';

export default {
    name: 'SearchPage',
    data() {
        let tab = '0';
        if (this.$route.query && this.$route.query.tab === 'albums') {
            tab = '1';
        }
        if (this.$route.query && this.$route.query.tab === 'tracks') {
            tab = '2';
        }
        return {
            tab,
            result: {
                albums: [],
                tracks: [],
                artists: []
            }
        };
    },
    beforeMount() {
        connector().get('search').then(res => {
            this.result = res;
        });
    },
    computed: {
        query() {
            return this.$route.query.q;
        }
    },
    components: {
        ArtistCard,
        AlbumCard
    },
    watch: {
        tab(nval, oval) {
            if (nval !== oval) {
                this.$router.push({
                    query: {
                        tab: nval === '0'
                            ? 'artists'
                            : nval === '1'
                                ? 'albums'
                                : 'tracks'
                    }
                });
            }
        }
    }
};
</script>
