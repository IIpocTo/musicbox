<template>
    <v-card :height="height" class="artist-card">
        <v-card-media
            :src="artist.image"
            height="250px"
            @click.prevent="() => showPage(artist.id)"
            :style="{ cursor: 'pointer' }"
        >
        </v-card-media>
        <v-card-title class="headline">{{ artist.name }}</v-card-title>
        <v-card-text class="tags">
            <v-chip
                v-for="(genre, i) in genres"
                :key="i"
                label
                dark color="primary"
                class="white--text"
            ><v-icon left>label</v-icon>{{ translate(genre) }}</v-chip>
        </v-card-text>
        <v-card-actions>
            <v-btn
                outline
                color="primary"
            ><v-icon>play_arrow</v-icon>Слушать</v-btn>
            <like-btn v-model="like"></like-btn>
        </v-card-actions>
    </v-card>
</template>
<script>
import * as i18n from '../util/i18n';
import LikeBtn from '@/components/common/LikeBtn';

export default {
    name: 'ArtistCard',
    props: {
        artist: Object,
        height: String,
        short: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return { like: false };
    },
    computed: {
        genres() {
            if (this.short) return this.artist.genres.slice(0, 3);
            return this.artist.genres;
        }
    },
    methods: {
        translate(value) {
            return i18n.t(value);
        },
        showPage(id) {
            this.$router.push(`/artists/${id}`);
        }
    },
    components: {
        LikeBtn
    }
};
</script>
<style lang="stylus" scoped>
.artist-card
    display flex
    flex-direction column
    flex-wrap no-wrap

    & .tags
        flex-grow: 2
</style>
