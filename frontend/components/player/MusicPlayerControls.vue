<template>
    <v-bottom-sheet
        :inset="!$vuetify.breakpoint.mdAndDown"
        :value="playerVisible && backendAvailable"
        hide-overlay persistent
    >
        <v-card tile>
            <transition name="slide-y-reverse-transition">
                <div v-if="showPlaylist">
                    <v-layout>
                        <v-subheader>Текущий плейлист</v-subheader>
                        <v-spacer></v-spacer>
                        <v-btn icon @click="showPlaylist = false"><v-icon>close</v-icon></v-btn>
                    </v-layout>
                    <v-divider></v-divider>
                    <div class="playlist-wrapper">
                        <m-playlist
                            :tracks="playlist ? playlist.content : []"
                        ></m-playlist>
                        <v-divider class="mb-3"></v-divider>
                    </div>
                </div>
            </transition>
            <v-slider
                v-model="progress"
                step="0"
                hide-details
                :max="1" :min="0"
                color="primary"
                class="pa-0 ma-0 music-player-slider"
            />
            <v-list>
                <v-list-tile avatar>
                    <v-list-tile-action>
                        <v-btn icon :disabled="!hasPrev" @click="getPrev">
                            <v-icon>fast_rewind</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                    <v-list-tile-action>
                        <v-btn icon :disabled="!currentSong" @click="togglePlayback">
                            <v-icon v-if="player.playing">pause</v-icon>
                            <v-icon v-else>play_arrow</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                    <v-list-tile-action class="mx-0">
                        <v-btn icon :disabled="!hasNext" @click="getNext">
                            <v-icon>fast_forward</v-icon>
                        </v-btn>
                    </v-list-tile-action>
                    <v-list-tile-avatar tile size="60" class="mr-2">
                        <img :src="currentSong.album.image" alt="album cover" v-if="currentSong">
                    </v-list-tile-avatar>
                    <v-list-tile-content v-if="currentSong">
                        <v-list-tile-title>{{ currentSong.name }}</v-list-tile-title>
                        <v-list-tile-sub-title>
                            {{ currentSong.artist.name }} &mdash; {{ currentSong.album.name }}
                        </v-list-tile-sub-title>
                    </v-list-tile-content>
                    <v-list-tile-content v-else>
                        <v-list-tile-title class="grey--text">Нет музыки</v-list-tile-title>
                    </v-list-tile-content>
                    <v-spacer/>
                    <v-list-tile-action class="mx-0">
                        <like-btn v-model="like" v-if="currentSong"></like-btn>
                    </v-list-tile-action>
                    <v-list-tile-action class="mx-0">
                        <v-tooltip top>
                            <v-btn
                                slot="activator"
                                icon
                                @click="shuffle = !shuffle"
                            >
                                <v-icon :color="shuffle ? 'primary' : void 0">shuffle</v-icon>
                            </v-btn>
                            <span>Перемешать</span>
                        </v-tooltip>
                    </v-list-tile-action>
                    <v-list-tile-action class="mx-0">
                        <v-tooltip top>
                            <v-btn
                                slot="activator"
                                icon
                                @click="setRepeat('all')"
                            >
                                <v-icon :color="repeat === 'all' ? 'primary' : void 0">repeat</v-icon>
                            </v-btn>
                            <span>Повторять весь плейлист</span>
                        </v-tooltip>
                    </v-list-tile-action>
                    <v-list-tile-action class="mx-0">
                        <v-tooltip top>
                            <v-btn
                                slot="activator"
                                icon
                                @click="setRepeat('one')"
                            >
                                <v-icon :color="repeat === 'one' ? 'primary' : void 0">repeat_one</v-icon>
                            </v-btn>
                            <span>Повторять текущую композицию</span>
                        </v-tooltip>
                    </v-list-tile-action>
                    <v-list-tile-action class="mx-0">
                        <v-menu
                            v-model="volumeMenu"
                            min-width="300"
                            class="white"
                            left top offset-x
                        >
                            <v-btn icon slot="activator">
                                <v-icon>{{ volumeIcon }}</v-icon>
                            </v-btn>
                            <v-card class="pa-1">
                                <v-slider
                                    v-model="volume"
                                    :prepend-icon="volumeIcon"
                                    :prepend-icon-cb="() => toggleMute()"
                                    :min="0" :max="1" :step="0"
                                    hide-details
                                    class="pt-1"
                                ></v-slider>
                            </v-card>
                        </v-menu>
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
            :sources="sources"
            loop
            autoplay
            @end="onPlaybackEnd"
        ></howler-player>
    </v-bottom-sheet>
</template>
<script>
import HowlerPlayer from './HowlerPlayer';
import MPlaylist from '@/components/MPlaylist';
import LikeBtn from '@/components/common/LikeBtn';

const music = [
    'https://raw.githubusercontent.com/BobNobrain/temp-static/master/03.%20Passage%20of%20the%20Crane.mp3',
    'https://raw.githubusercontent.com/BobNobrain/temp-static/master/Dethklok%20-%20Fansong.mp3',
    'https://raw.githubusercontent.com/BobNobrain/temp-static/master/10.%20Corium.mp3',
    'https://raw.githubusercontent.com/BobNobrain/temp-static/master/Orpheus%20Omega%20-%20Tomorrow\'s%20Friends%20%20Yesterday\'s%20Ghosts.mp3'
];

export default {
    name: 'MusicPlayerControls',
    data() {
        return {
            menu: false,
            showPlaylist: false,
            repeat: 'none',
            volumeMenu: false,
            shuffle: false,
            like: false,

            playerInstance: null
        };
    },
    computed: {
        currentSong() { return this.$store.getters['player/currentSong']; },
        playerVisible() { return this.$store.state.player.visible; },
        backendAvailable() { return this.$store.state.backendAvailable; },
        playlist() { return this.$store.state.player.playlist; },
        position() { return this.$store.state.player.position; },

        hasPrev() {
            if (!this.playlist) return false;
            if (this.repeat === 'all') return true;
            if (!this.shuffle) return this.position > 0;
            return this.shuffledPositions[0] !== this.position;
        },
        hasNext() {
            if (!this.playlist) return false;
            if (this.repeat === 'all') return true;
            if (!this.shuffle) return this.position < this.playlist.content.length - 1;
            return this.shuffledPositions[this.shuffledPositions.length - 1] !== this.position;
        },

        menuItems() {
            const result = [
                { icon: 'visibility_off', text: 'Спрятать плеер', action: 'hidePlayer' },
                { icon: 'playlist_play', text: 'К плейлисту', action: 'goToPlaylist' }
            ];
            if (this.currentSong) {
                result.push({ icon: 'playlist_add', text: 'Добавить в плейлист', action: 'addToPlaylist' });
            }
            return result;
        },

        sources() {
            if (!this.playlist || !this.currentSong) return [];
            const realUrl = this.currentSong.content;
            console.log(realUrl);
            return [music[this.position % 4]];
        },
        player() {
            if (this.playerInstance) return this.playerInstance;
            return {
                progress: 0,
                volume: 0.5,
                playing: false
            };
        },

        // fuck vue-howler's lazy author who didn't managed to do this himself
        progress: {
            get() {
                return this.player.progress;
            },
            set(value) {
                this.player.setProgress(value);
            }
        },
        volume: {
            get() {
                return this.player.volume;
            },
            set(value) {
                this.player.setVolume(value);
            }
        },

        shuffledPositions() {
            const tracks = this.playlist.content;
            console.log('shuffle!');
            const l = tracks.length;
            const result = new Array(l).fill(null).map((_, i) => i);
            for (let i = l - 1; i > 0; i--) {
                const r = (Math.random() * (i - 1)) | 0;
                [result[r], result[i]] = [result[i], result[r]];
            }
            console.log('shuffle!', result);
            return result;
        },

        volumeIcon() {
            if (!this.playerInstance) return 'volume_up';
            if (this.playerInstance.muted) return 'volume_off';
            const volume = this.playerInstance.volume;
            if (volume < 0.1) return 'volume_mute';
            if (volume < 0.5) return 'volume_down';
            return 'volume_up';
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
            this.showPlaylist = true;
        },


        getNext() {
            let position = this.$store.state.player.position;
            if (this.shuffle) {
                for (let i = 0; i < this.shuffledPositions.length; i++) {
                    if (this.shuffledPositions[i] === position) {
                        const nextI = (i + 1) % this.shuffledPositions.length;
                        position = this.shuffledPositions[nextI];
                        break;
                    }
                }
            } else {
                position += 1;
                if (position >= this.playlist.content.length) position = 0;
            }
            this.$store.commit('player/setPosition', position);
        },
        getPrev() {
            let position = this.$store.state.player.position;
            if (this.shuffle) {
                for (let i = 0; i < this.shuffledPositions.length; i++) {
                    if (this.shuffledPositions[i] === position) {
                        const prevI = (i - 1 + this.shuffledPositions.length) % this.shuffledPositions.length;
                        position = this.shuffledPositions[prevI];
                        break;
                    }
                }
            } else {
                position -= 1;
                if (position < 0) position = this.playlist.content.length - 1;
            }
            this.$store.commit('player/setPosition', position);
        },
        togglePlayback() {
            this.$refs.player.togglePlayback();
        },
        setRepeat(mode) {
            if (this.repeat === mode) this.repeat = 'none';
            else this.repeat = mode;
        },

        toggleMute() {
            if (this.playerInstance) {
                this.playerInstance.toggleMute();
            }
        },

        onPlaybackEnd() {
            if (this.repeat === 'one') return;
            if (this.hasNext) this.getNext();
            else this.playerInstance.stop();
        }
    },

    watch: {
        sources(nval) {
            if (nval.length) {
                this.$nextTick(() => {
                    this.playerInstance = this.$refs.player;
                });
            } else {
                this.playerInstance = null;
            }
        }
    },

    components: {
        HowlerPlayer,
        MPlaylist,
        LikeBtn
    }
};
</script>
<style lang="stylus" scoped>
.playlist-wrapper
    max-height 60vh
    overflow-y auto
</style>
