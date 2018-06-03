<template>
    <div>
        <v-card
            hover
            raised
            v-for="(value, key) in artists" :key="key"
            @click.prevent="showArtist(value.id)"
        >
            {{ value.name }}
        </v-card>
    </div>
</template>
<script>
import { mapActions, mapGetters } from 'vuex';

export default {
    name: 'ArtistsPage',
    beforeMount() {
        this.getArtists();
    },
    methods: {
        ...mapActions({
            getArtists: 'artists/getArtists'
        }),
        showArtist(id) {
            // eslint-disable-next-line
            console.log(id);
            this.$router.push(`/artists/${id}`);
        }
    },
    computed: {
        ...mapGetters({
            artists: 'artists/artists'
        })
    }
};
</script>
