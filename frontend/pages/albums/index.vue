<template>
    <div>
        <card-grid :items="albums" no-data-full-width>
            <template slot-scope="{ item }">
                <album-card :album="item" height="100%"></album-card>
            </template>
            <v-alert :value="true" type="info" slot="no-data">
                Не найдено альбомов
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
import AlbumCard from '@/components/AlbumCard';

export default {
    name: 'AlbumsPage',
    data() {
        return {
            page: 1,
            limit: 12,
            loading: false,
            wholeLoaded: false
        };
    },
    beforeMount() {
        this.getAlbums();
    },
    computed: {
        albums() {
            return this.$store.state.remote.albums;
        }
    },
    methods: {
        async getAlbums() {
            this.loading = true;
            try {
                const n = await this.$store.dispatch('remote/list', {
                    name: 'albums',
                    page: this.page,
                    limit: this.limit
                });
                if (n === 0) {
                    this.wholeLoaded = true;
                }
            } catch (error) {
                this.$store.commit('showError', { error, text: 'Не удалось загрузить альбомы' });
            }
            this.loading = false;
        },
        fetchMore() {
            this.page++;
            this.getAlbums();
        }
    },
    components: {
        CardGrid,
        AlbumCard
    }
};
</script>
