<template>
    <v-list>
        <template v-if="showTitle">
            <v-subheader>
                {{ name }}
                <span v-if="showCount" class="grey--text ml-3 body-1">({{ tracks.length }} треков)</span>
            </v-subheader>
            <v-divider></v-divider>
        </template>
        <v-list-tile
            v-for="(track, i) in tracks"
            :key="i"
            avatar
            :color="i === currentSongIndex ? 'primary' : void 0"
            :class="{ 'current-song': i === currentSongIndex }"
            @click="playTrack(i)"
        >
            <v-list-tile-avatar tile>
                <img :src="track.album.image">
            </v-list-tile-avatar>
            <v-list-tile-content>
                <v-list-tile-title>{{ track.name }}</v-list-tile-title>
                <v-list-tile-sub-title>{{ track.artist.name }} &mdash; {{ track.album.name }}</v-list-tile-sub-title>
            </v-list-tile-content>
            <v-list-tile-action v-if="i !== currentSongIndex">
                <v-btn icon @click="() => {}"><v-icon>play_arrow</v-icon></v-btn>
            </v-list-tile-action>
            <v-list-tile-action>
                <v-btn icon @click.native.stop="() => {}"><v-icon>playlist_add</v-icon></v-btn>
            </v-list-tile-action>
            <v-list-tile-action>
                <like-btn @click.native.stop></like-btn>
            </v-list-tile-action>
        </v-list-tile>
        <v-list-tile v-if="!tracks.length">
            <v-list-tile-content>
                <v-list-tile-title class="text-xs-center">{{ noDataText }}</v-list-tile-title>
            </v-list-tile-content>
        </v-list-tile>
    </v-list>
</template>
<script>
import LikeBtn from '@/components/common/LikeBtn';

export default {
    name: 'MPlaylist',
    props: {
        tracks: Array,
        name: {
            type: String,
            default: ''
        },
        showTitle: {
            type: Boolean,
            default: false
        },
        showCount: {
            type: Boolean,
            default: false
        },
        noDataText: {
            type: String,
            default: 'Плейлист пуст'
        }
    },
    computed: {
        currentSongId() {
            const song = this.$store.getters['player/currentSong'];
            if (song) return song.id;
            return NaN; // to be unequal to anything
        },
        currentSongIndex() {
            const id = this.currentSongId;
            for (let i = 0; i < this.tracks.length; i++) {
                if (this.tracks[i].id === id) return i;
            }
            return -1;
        }
    },
    methods: {
        playTrack(i) {
            if (i === this.currentSongIndex) return;
            this.$store.commit('player/play', {
                playlist: {
                    name: this.name || 'Безымянный плейлист',
                    content: this.tracks
                },
                position: i
            });
        }
    },
    components: {
        LikeBtn
    }
};
</script>
<style lang="stylus" scoped>
.current-song
    background rgba(0, 0, 0, 0.1)
    transition background 200ms
    &:hover
        background unset
</style>
