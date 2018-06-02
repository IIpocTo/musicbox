<template>
    <v-app>
        <v-navigation-drawer
            :clipped="clipped"
            v-model="drawer"
            fixed
            app
        >
            <v-list>
                <template v-if="!loggedIn">
                    <v-list-tile to="/login?tab=login" exact>
                        <v-list-tile-action>
                            <v-icon>input</v-icon>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>Войти</v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile to="/login?tab=register" exact>
                        <v-list-tile-action>
                            <v-icon>edit</v-icon>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>Регистрация</v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                </template>
                <v-list-tile to="/artists">
                    <v-list-tile-action>
                        <v-icon>person</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Исполнители</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-list-tile to="/albums">
                    <v-list-tile-action>
                        <v-icon>album</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Альбомы</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-list-tile to="/playlists">
                    <v-list-tile-action>
                        <v-icon>playlist_play</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Плейлисты</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-list-tile v-if="loggedIn" to="/recommends">
                    <v-list-tile-action>
                        <v-icon>stars</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Рекомендации</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
            </v-list>
        </v-navigation-drawer>
        <v-toolbar fixed app :clipped-left="clipped" color="primary" dark>
            <v-toolbar-side-icon @click="drawer = !drawer"></v-toolbar-side-icon>
            <v-toolbar-title
                @click.prevent="goHome()"
                :style="{ cursor: 'pointer' }"
            >
                {{ title }}
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-text-field
                v-model="search"
                prepend-icon="search"
                placeholder="Поиск музыки..."
                clearable
                solo-inverted flat
            ></v-text-field>
        </v-toolbar>
        <v-content>
            <v-container>
                <nuxt />
            </v-container>
            <div v-if="playerVisible" style="height: 100px;"></div>
        </v-content>
        <v-footer
            :fixed="fixed" app
            dark class="px-3"
        >
            <music-player></music-player>
            <v-spacer></v-spacer>
            <div>&copy; 2018 TITANY</div>
        </v-footer>
    </v-app>
</template>
<script>
import MusicPlayer from '@/components/MusicPlayer';

export default {
    data() {
        return {
            clipped: true,
            drawer: false,
            fixed: false,
            title: 'MusicBox',
            loggedIn: false,
            search: ''
        };
    },
    computed: {
        playerVisible() {
            return this.$store.state.player.visible;
        }
    },
    methods: {
        goHome() {
            this.$router.push('/');
        }
    },
    components: {
        MusicPlayer
    }
};
</script>
