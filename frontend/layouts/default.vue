<template>
    <v-app>
        <v-navigation-drawer
            :clipped="clipped"
            v-model="drawer"
            fixed
            app
        >
            <v-list>
                <template v-if="!authorized">
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
                <template v-else>
                    <v-list-tile @click.prevent="logout" exact>
                        <v-list-tile-action>
                            <v-icon>input</v-icon>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>Выйти</v-list-tile-title>
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
                <v-list-tile v-if="authorized" to="/recommends">
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
                @keyup.enter="goSearch"
            ></v-text-field>
        </v-toolbar>
        <v-content>
            <v-container>
                <nuxt v-if="backendAvailable"/>
                <v-layout v-else row justify-center class="pt-5">
                    <v-flex xs12 md8 lg6 xl4 v-if="!loading">
                        <v-card>
                            <v-card-title class="title">
                                <v-icon large color="error" class="mr-3">warning</v-icon>
                                <span>Ошибка соединения</span>
                            </v-card-title>
                            <v-card-text>
                                При соединении с сервером произошла ошибка. Повторите запрос позже.
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn
                                    dark color="primary"
                                    :loading="loading"
                                    @click="checkConnection"
                                ><v-icon left>refresh</v-icon>Попробовать снова</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-flex>
                    <v-progress-circular v-else indeterminate color="primary"></v-progress-circular>
                </v-layout>
            </v-container>
            <div v-if="playerVisible && backendAvailable" style="height: 100px;"></div>
        </v-content>
        <v-footer
            :fixed="fixed" app
            dark class="px-3"
        >
            <music-player></music-player>
            <v-spacer></v-spacer>
            <div>&copy; 2018 TITANY</div>
        </v-footer>
        <v-snackbar multi-line top :timeout="0" :value="cookieNotifier">
            Этот сайт использует cookies. Продолжая пользоваться данным сайтом,
            вы даёте своё согласие на сохранение cookies на Вашем устройстве.
            <v-btn dark flat color="primary" ripple @click="hideCookieNotifier">Согласен</v-btn>
        </v-snackbar>
    </v-app>
</template>
<script>
import timeout from '@/util/timeout';
import MusicPlayer from '@/components/MusicPlayer';
import {mapGetters, mapActions} from 'vuex';
import jwtDecode from 'jwt-decode';
import {TOKENS} from '../util/connector';

export default {
    async beforeMount() {
        const token = global.localStorage.getItem(TOKENS.AUTHORIZATION);
        if (token) {
            const tokenData = jwtDecode(token);
            if (tokenData.expires * 1000 < Date.now()) {
                this.keepLoggedIn();
            }
        }
        await this.checkConnection();
        await this.$store.dispatch('user/getMe');
    },
    data() {
        return {
            loading: true,

            clipped: true,
            drawer: false,
            fixed: false,
            title: 'MusicBox',
            search: ''
        };
    },
    computed: {
        ...mapGetters({
            authorized: 'user/authorized'
        }),
        playerVisible() {
            return this.$store.state.player.visible;
        },
        backendAvailable() {
            return this.$store.state.backendAvailable;
        },

        cookieNotifier() {
            return this.$store.state.cookieNotifier;
        }
    },
    methods: {
        ...mapActions({
            keepLoggedIn: 'user/keepLoggedIn'
        }),
        logout() {
            this.$store.dispatch('user/logout');
        },
        goHome() {
            this.$router.push('/');
        },
        goSearch() {
            this.$router.push(`/search?q=${this.search}`);
            this.search = '';
        },
        async checkConnection() {
            this.loading = true;
            await this.$store.dispatch('checkBackend');
            await timeout(250);
            this.loading = false;
        },
        hideCookieNotifier() {
            this.$store.dispatch('hideCookieNotifier');
        }
    },
    components: {
        MusicPlayer
    },
    mounted() {
        if (!process.server) {
            this.$store.dispatch('renderCookieNotifier');
        }
    }
};
</script>
