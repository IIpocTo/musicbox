<template>
    <v-container grid-list-md>
        <v-layout
            row wrap
            justify-center
            align-center
        >
            <v-flex xs12 md10 lg8>
                <v-layout row wrap>
                    <v-flex xs12>
                        <v-alert type="info" v-model="unauthorizedNotifierVisible" dismissible>
                            <span>Чтобы мы могли подбирать Вам персональные рекомендации, вам необходимо представиться. </span>
                            <a
                                href="#"
                                @click.prevent="showLoginForm()"
                            >Войти</a>
                        </v-alert>
                    </v-flex>
                    <v-flex xs12 md6 lg8>
                        <v-card height="100%">
                            <v-card-title class="headline">Добро пожаловать на MusicBox!</v-card-title>
                            <v-card-text>
                                <p>
                                    Добро пожаловать на портал Musicbox! Здесь вы можете найти свои любимые треки
                                    и открыть для себя новые звучания и исполнителей. Регистрируйтесь на портале,
                                    найдите то, что вам нравится, прослушайте, и система сможет предложить вам новые
                                    композиции на основе ваших предпочтений. Лаконичный, современный и удобный
                                    интерфейс позволит вам быстро ориентироваться в портале, а стриминговая
                                    технология прослушивания позволит слушать музыку совершенно бесплатно и без
                                    рекламы. Регистрируйтесь на Musicbox!
                                </p>
                            </v-card-text>
                        </v-card>
                    </v-flex>
                    <v-flex xs12 md6 lg4>
                        <v-card>
                            <v-list two-line>
                                <v-subheader>Слушайте музыку без ограничений!</v-subheader>
                                <v-divider></v-divider>
                                <v-list-tile avatar @click="() => {}">
                                    <v-list-tile-avatar>
                                        <v-icon large>person</v-icon>
                                    </v-list-tile-avatar>
                                    <v-list-tile-content>
                                        Открывайте новых исполнителей
                                    </v-list-tile-content>
                                </v-list-tile>
                                <v-list-tile avatar @click="() => {}">
                                    <v-list-tile-avatar>
                                        <v-icon large>album</v-icon>
                                    </v-list-tile-avatar>
                                    <v-list-tile-content>
                                        Находите новые альбомы
                                    </v-list-tile-content>
                                </v-list-tile>
                                <v-list-tile avatar @click="() => {}">
                                    <v-list-tile-avatar>
                                        <v-icon large>stars</v-icon>
                                    </v-list-tile-avatar>
                                    <v-list-tile-content>
                                        Получайте персональные рекомендации
                                    </v-list-tile-content>
                                </v-list-tile>
                            </v-list>
                        </v-card>
                    </v-flex>
                    <template v-if="!loading">
                        <card-grid :items="artists">
                            <template slot-scope="{ item }">
                                <artist-card :artist="item" height="100%" short></artist-card>
                            </template>
                        </card-grid>
                    </template>
                    <v-flex v-else xs12 class="text-xs-center">
                        <v-progress-circular indeterminate color="primary"></v-progress-circular>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>
    </v-container>
</template>
<script>
import CardGrid from '@/components/common/CardGrid';
import ArtistCard from '@/components/ArtistCard';
import AlbumCard from '@/components/AlbumCard';

export default {
    name: 'HomePage',
    mounted() {
        this.getArtists();
    },
    data() {
        return {
            unauthorizedNotifier: true,
            loading: false
        };
    },
    methods: {
        showLoginForm() {
            this.$router.push('/login?tab=login');
        },
        async getArtists() {
            this.loading = true;
            await this.$store.dispatch('remote/list', {
                name: 'artists',
                page: 1,
                limit: 6
            });
            this.loading = false;
        }
    },
    computed: {
        artists() {
            return this.$store.state.remote.artists.slice(0, 6);
        },

        unauthorizedNotifierVisible: {
            get() {
                if (!this.$store.state.user.user) {
                    return this.unauthorizedNotifier;
                } else {
                    return false;
                }
            },
            set(value) {
                this.unauthorizedNotifier = value;
            }
        }
    },
    components: {
        ArtistCard,
        AlbumCard,
        CardGrid
    }
};
</script>
