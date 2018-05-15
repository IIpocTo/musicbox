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
                    <v-list-tile>
                        <v-list-tile-action>
                            <v-icon>input</v-icon>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>Войти</v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile>
                        <v-list-tile-action>
                            <v-icon>edit</v-icon>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>Регистрация</v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                </template>
                <v-list-tile>
                    <v-list-tile-action>
                        <v-icon>person</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Исполнители</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-list-tile>
                    <v-list-tile-action>
                        <v-icon>album</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Альбомы</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-list-tile>
                    <v-list-tile-action>
                        <v-icon>playlist_play</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Плейлисты</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
                <v-list-tile v-if="loggedIn">
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
            <v-toolbar-title>{{ title }}</v-toolbar-title>
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
        </v-content>
        <v-footer :fixed="fixed" app class="px-3">
            <span>&copy; 2018 TITANY</span>
        </v-footer>
    </v-app>
</template>
<script>
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
    }
};
</script>
