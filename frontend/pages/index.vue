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
                        <v-alert type="info" :value="true" dismissible>
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
                    <v-flex xs6 lg4>
                        <artist-card :artist="bau"></artist-card>
                    </v-flex>
                    <v-flex xs6 lg4>
                        <artist-card :artist="iron"></artist-card>
                    </v-flex>
                    <v-flex xs6 lg4>
                        <album-card :album="rise"></album-card>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>
    </v-container>
</template>
<script>
import ArtistCard from '@/components/ArtistCard';
import AlbumCard from '@/components/AlbumCard';

export default {
    name: 'HomePage',
    data() {
        return {
            bau: {
                name: 'Б.А.У.',
                image: require('@/assets/image/bau.jpeg'),
                tags: ['Метал', 'Пародия', 'Гроул']
            },
            iron: {
                name: 'Iron Maiden',
                image: require('@/assets/image/iron.jpg'),
                tags: ['Хэви-Метал', 'Метал', 'Рок']
            },
            rise: {
                name: 'Rise of Tyrant',
                artist: {
                    name: 'Arch Enemy'
                },
                image: require('@/assets/image/rise.jpg'),
                tags: ['Метал', 'Мелодик-Дэт']
            }
        };
    },
    methods: {
        showLoginForm() {}
    },
    components: {
        ArtistCard,
        AlbumCard
    }
};
</script>