<template>
    <v-bottom-sheet inset :value="playerVisible && backendAvailable" hide-overlay persistent>
        <v-card tile>
            <!-- <v-progress-linear :value="50" height="3" class="my-0"></v-progress-linear> -->
            <v-slider
                v-model="progress"
                step="0"
                hide-details
                thumb-label
                :max="1" :min="0"
                color="primary"
                class="pa-0 ma-0 music-player-slider"
            />
            <v-list>
                <v-list-tile avatar>
                    <v-list-tile-avatar tile size="60" class="mr-2">
                        <img src="@/assets/image/oomph.jpg" alt="album cover">
                    </v-list-tile-avatar>
                    <v-list-tile-content>
                        <v-list-tile-title>{{ currentSong.title }}</v-list-tile-title>
                        <v-list-tile-sub-title>
                            {{ currentSong.artist.title }} &mdash; {{ currentSong.album.title }}
                        </v-list-tile-sub-title>
                    </v-list-tile-content>
                    <v-spacer/>
                    <v-list-tile-action>
                        <v-btn icon @click="getPrev">
                            <v-icon>fast_rewind</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                    <v-list-tile-action :class="{ 'mx-3': $vuetify.breakpoint.mdAndUp }">
                        <v-btn icon @click="togglePlayback">
                            <v-icon v-if="playing">pause</v-icon>
                            <v-icon v-else>play_arrow</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                    <v-list-tile-action :class="{ 'mr-2': $vuetify.breakpoint.mdAndUp }">
                        <v-btn icon @click="getNext">
                            <v-icon>fast_forward</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                    <v-list-tile-action class="px-0">
                        <v-btn icon>
                            <v-icon>more_vert</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                </v-list-tile>
            </v-list>
        </v-card>
    </v-bottom-sheet>
</template>
<script>
import VueHowler from 'vue-howler';

export default {
    mixins: [VueHowler],
    name: 'MusicPlayer',
    computed: {
        currentSong() {
            return this.$store.getters['player/currentSong'];
        },
        playerVisible() {
            return this.$store.state.player.visible;
        },
        backendAvailable() {
            return this.$store.state.backendAvailable;
        }
    },
    methods: {
        getNext() {

        },
        getPrev() {

        }
    }
};
</script>
