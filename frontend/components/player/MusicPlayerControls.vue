<template>
    <v-bottom-sheet
        :inset="!$vuetify.breakpoint.mdAndDown"
        :value="playerVisible && backendAvailable"
        hide-overlay persistent
    >
        <v-card tile>
            <transition name="slide-y-reverse-transition">
                <div v-if="showPlaylist">
                    <v-subheader>Текущий плейлист</v-subheader>
                    <v-divider></v-divider>
                    <v-list>
                        <v-list-tile avatar>
                            <v-list-tile-avatar tile>
                                <img src="@/assets/image/oomph.jpg">
                            </v-list-tile-avatar>
                            <v-list-tile-content>
                                <v-list-tile-title>True Beauty Is So Painful</v-list-tile-title>
                                <v-list-tile-sub-title>Oomph! &mdash; True Beauty Is So Painful</v-list-tile-sub-title>
                            </v-list-tile-content>
                            <v-list-tile-action>
                                <v-btn icon><v-icon>play_arrow</v-icon></v-btn>
                            </v-list-tile-action>
                            <v-list-tile-action>
                                <v-btn icon><v-icon>playlist_add</v-icon></v-btn>
                            </v-list-tile-action>
                            <v-list-tile-action>
                                <v-btn icon><v-icon>favorite</v-icon></v-btn>
                            </v-list-tile-action>
                        </v-list-tile>
                    </v-list>
                    <v-divider class="mb-3"></v-divider>
                </div>
            </transition>
            <!-- <v-progress-linear :value="50" height="3" class="my-0"></v-progress-linear> -->
            <v-slider
                v-model="player.progress"
                step="0"
                hide-details
                :max="1" :min="0"
                color="primary"
                class="pa-0 ma-0 music-player-slider"
            />
            <v-list>
                <v-list-tile avatar>
                    <v-list-tile-avatar tile size="60" class="mr-2">
                        <img src="@/assets/image/oomph.jpg" alt="album cover" v-if="currentSong">
                    </v-list-tile-avatar>
                    <v-list-tile-content v-if="currentSong">
                        <v-list-tile-title>{{ currentSong.title }}</v-list-tile-title>
                        <v-list-tile-sub-title>
                            {{ currentSong.artist.title }} &mdash; {{ currentSong.album.title }}
                        </v-list-tile-sub-title>
                    </v-list-tile-content>
                    <v-list-tile-content v-else>
                        <v-list-tile-title>Нет музыки</v-list-tile-title>
                    </v-list-tile-content>
                    <v-spacer/>
                    <v-list-tile-action>
                        <v-btn icon @click="getPrev">
                            <v-icon>fast_rewind</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                    <v-list-tile-action :class="{ 'mx-3': $vuetify.breakpoint.mdAndUp }">
                        <v-btn icon @click="togglePlayback">
                            <v-icon v-if="player.playing">pause</v-icon>
                            <v-icon v-else>play_arrow</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                    <v-list-tile-action :class="{ 'mr-2': $vuetify.breakpoint.mdAndUp }">
                        <v-btn icon @click="getNext">
                            <v-icon>fast_forward</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                    <v-list-tile-action class="px-0">
                        <v-menu v-model="menu" top offset-y>
                            <v-btn icon slot="activator">
                                <v-icon>more_vert</v-icon>
                            </v-btn>
                            <v-list dense>
                                <v-list-tile
                                    v-for="(menuItem, i) in menuItems"
                                    :key="i"
                                    @click="onMenuItemClick(menuItem)"
                                >
                                    <v-list-tile-title>
                                        <v-icon left>{{ menuItem.icon }}</v-icon>
                                        {{ menuItem.text }}
                                    </v-list-tile-title>
                                </v-list-tile>
                            </v-list>
                        </v-menu>
                    </v-list-tile-action>
                </v-list-tile>
            </v-list>
        </v-card>
        <howler-player
            v-if="sources.length"
            ref="player"
            :audio-sources="sources"
            loop autoplay
        ></howler-player>
    </v-bottom-sheet>
</template>
<script>
import HowlerPlayer from './HowlerPlayer';

export default {
    name: 'MusicPlayerControls',
    data() {
        return {
            menu: false,

            menuItems: [
                { icon: 'visibility_off', text: 'Спрятать плеер', action: 'hidePlayer' },
                { icon: 'playlist_play', text: 'К плейлисту', action: 'goToPlaylist' },
                { icon: 'playlist_add', text: 'Добавить в плейлист', action: 'addToPlaylist' },
                { icon: 'favorite', text: 'Мне нравится', action: 'likeSong' }
            ],

            showPlaylist: false
        };
    },
    computed: {
        currentSong() {
            return this.$store.getters['player/currentSong'];
        },
        playerVisible() {
            return this.$store.state.player.visible;
        },
        backendAvailable() {
            return this.$store.state.backendAvailable;
        },

        playlist() {
            return this.$store.state.player.playlist;
        },

        sources() {
            return [];
        },
        player() {
            return this.$refs.player || ({
                progress: 0,
                playing: false
            });
        }
    },
    methods: {
        onMenuItemClick({ action, args = [] }) {
            if (typeof this[action] === typeof Function) {
                this[action](...args);
            } else {
                console.error('Unknown action: ' + action);
            }
        },
        hidePlayer() {
            this.$store.commit('player/setVisibility', false);
        },
        goToPlaylist() {
            // TODO
        },


        getNext() {
        },
        getPrev() {
        },
        togglePlayback() {
            this.$refs.player.togglePlayback();
        }
    },

    components: {
        HowlerPlayer
    }
};
</script>
