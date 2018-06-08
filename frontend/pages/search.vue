<template>
    <v-card>
        <v-card-title>
            Результаты поиска по запросу "<b>{{ query }}</b>"
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
                    <card-grid :items="result.artists || []" no-data-full-width>
                        <template slot-scope="{ item }">
                            <artist-card :artist="item"></artist-card>
                        </template>
                        <v-alert :value="true" type="info" slot="no-data">
                            Не найдено исполнителей
                        </v-alert>
                    </card-grid>
                </v-tab-item>
                <v-tab-item
                    key="albums"
                >
                    <card-grid :items="result.albums || []" no-data-full-width>
                        <template slot-scope="{ item }">
                            <album-card :album="item"></album-card>
                        </template>
                        <v-alert :value="true" type="info" slot="no-data">
                            Не найдено альбомов
                        </v-alert>
                    </card-grid>
                </v-tab-item>
                <v-tab-item
                    key="tracks"
                >
                    <m-playlist
                        :tracks="result.tracks || []"
                        no-data-text="Треков не найдено"
                    ></m-playlist>
                </v-tab-item>
            </v-tabs-items>
        </v-card-text>
    </v-card>
</template>
<script>
import connector from '../util/connector';
import ArtistCard from '@/components/ArtistCard';
import AlbumCard from '@/components/AlbumCard';
import MPlaylist from '@/components/MPlaylist';
import CardGrid from '@/components/common/CardGrid';

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
        AlbumCard,
        CardGrid,
        MPlaylist
    },
    watch: {
        tab(nval, oval) {
            if (nval !== oval) {
                this.$router.push({
                    query: {
                        tab: ['artists', 'albums', 'tracks'][nval] || '',
                        q: this.query
                    }
                });
            }
        }
    }
};
</script>
