<template>
    <v-card>
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
        <v-card-text>
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
            <v-btn
                icon ripple
                outline
                color="red"
            ><v-icon>favorite</v-icon></v-btn>
        </v-card-actions>
    </v-card>
</template>
<script>
export default {
    name: 'ArtistCard',
    props: {
        album: Object
    }
};
</script>
