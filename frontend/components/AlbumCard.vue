<template>
    <v-card :height="height" class="album-card">
        <v-card-media :src="album.image" height="250px"></v-card-media>
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
                v-for="(tag, i) in album.tags"
                :key="i"
                label
                dark color="primary"
                class="white--text"
            ><v-icon left>label</v-icon>{{ tag }}</v-chip>
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
