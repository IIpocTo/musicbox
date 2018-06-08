<template>
    <v-card>
        <v-card-media
            :src="artist.image"
            height="250px"
            @click.prevent="() => showPage(artist.id)"
            :style="{ cursor: 'pointer' }"
        >
        </v-card-media>
        <v-card-title class="headline">{{ artist.name }}</v-card-title>
        <v-card-text>
            <v-chip
                v-for="(genre, i) in artist.genres"
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
            <v-btn
                icon ripple
                flat color="red"
            ><v-icon>favorite</v-icon></v-btn>
        </v-card-actions>
    </v-card>
</template>
<script>
import * as i18n from '../util/i18n';

export default {
    name: 'ArtistCard',
    props: {
        artist: Object
    },
    methods: {
        translate(value) {
            return i18n.t(value);
        },
        showPage(id) {
            this.$router.push(`/artists/${id}`);
        }
    }
};
</script>
