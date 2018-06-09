<template>
    <v-card :height="height" class="artist-card">
        <nuxt-link :to="'/artists/' + artist.id">
            <v-card-media
                :src="artist.image"
                height="250px"
                :style="{ cursor: 'pointer' }"
            >
            </v-card-media>
        </nuxt-link>
        <v-card-title class="headline">{{ artist.name }}</v-card-title>
        <v-card-text class="tags">
            <v-chip
                v-for="(genre, i) in genresTranslated"
                :key="i"
                label
                dark color="primary"
                class="white--text"
            ><v-icon left>label</v-icon>{{ genre }}</v-chip>
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
import LikeBtn from '@/components/common/LikeBtn';
import GenreRenderer from '@/mixins/GenreRenderer';

export default {
    name: 'ArtistCard',
    mixins: [GenreRenderer('artist')],
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
