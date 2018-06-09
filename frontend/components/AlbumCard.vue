<template>
    <v-card :height="height" class="album-card">
        <nuxt-link :to="'/albums/' + album.id">
            <v-card-media :src="album.image" height="250px"></v-card-media>
        </nuxt-link>
        <v-card-title class="headline">
            <span>
                {{ album.name }}
            </span>
            <span class="grey--text">
                &nbsp;&mdash;
                {{ album.artist.name }}
            </span>
        </v-card-title>
        <v-card-text class="tags">
            <v-chip
                v-if="releaseDate"
                label
                dark color="primary"
                class="white--text body-2"
            ><v-icon left>calendar_today</v-icon>{{ releaseDate }}</v-chip>
            <v-chip
                label
                outline color="primary"
                class="white--text body-2"
            ><v-icon left color="primary">library_music</v-icon>{{ album.tracks.length }} треков</v-chip>
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

export default {
    name: 'ArtistCard',
    props: {
        album: Object,
        height: String
    },
    data() {
        return { like: false };
    },
    computed: {
        releaseDate() {
            const date = this.album.releaseDate;
            if (!date) return false;
            const m = date.month;
            const sm = m < 10 ? '0' + m : '' + m;
            return `${date.day}.${sm}.${date.year}`;
        }
    },
    components: {
        LikeBtn
    }
};
</script>
<style lang="stylus" scoped>
.album-card
    display flex
    flex-direction column
    flex-wrap no-wrap

    & .tags
        flex-grow: 2
</style>
