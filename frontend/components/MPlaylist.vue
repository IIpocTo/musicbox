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
            @click="playTrack(i)"
        >
            <v-list-tile-avatar tile>
                <img :src="track.album.image">
            </v-list-tile-avatar>
            <v-list-tile-content>
                <v-list-tile-title>{{ track.name }}</v-list-tile-title>
                <v-list-tile-sub-title>{{ track.artist.name }} &mdash; {{ track.album.name }}</v-list-tile-sub-title>
            </v-list-tile-content>
            <v-list-tile-action>
                <v-btn icon><v-icon>play_arrow</v-icon></v-btn>
            </v-list-tile-action>
            <v-list-tile-action>
                <v-btn icon><v-icon>playlist_add</v-icon></v-btn>
            </v-list-tile-action>
            <v-list-tile-action>
                <like-btn></like-btn>
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
    methods: {
        playTrack() {}
    },
    components: {
        LikeBtn
    }
};
</script>
