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
                v-for="(tag, i) in artist.tags"
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
            <v-btn
                icon ripple
                flat color="red"
            ><v-icon>favorite</v-icon></v-btn>
        </v-card-actions>
    </v-card>
</template>
<script>
export default {
    name: 'ArtistCard',
    props: {
        artist: Object
    },
    methods: {
        showPage(id) {
            this.$router.push(`/artists/${id}`);
        }
    }
};
</script>
