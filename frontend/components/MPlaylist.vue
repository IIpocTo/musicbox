<template>
    <v-list>
        <template v-if="showTitle">
            <v-subheader>{{ title }}</v-subheader>
            <v-divider></v-divider>
        </template>
        <v-list-tile
            v-for="(track, i) in tracks"
            :key="i"
            avatar
        >
            <v-list-tile-avatar tile>
                <img :src="track.album.cover">
            </v-list-tile-avatar>
            <v-list-tile-content>
                <v-list-tile-title>{{ track.title }}</v-list-tile-title>
                <v-list-tile-sub-title>{{ track.artist.title }} &mdash; {{ track.album.title }}</v-list-tile-sub-title>
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
        title: {
            type: String,
            default: ''
        },
        showTitle: {
            type: Boolean,
            default: false
        },
        noDataText: {
            type: String,
            default: 'Плейлист пуст'
        }
    },
    components: {
        LikeBtn
    }
};
</script>
