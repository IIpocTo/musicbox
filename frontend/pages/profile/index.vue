<template>
    <v-container>
        <v-card>
            <v-card-title>
                Добро пожаловать, {{ user.username }}!
            </v-card-title>
            <v-card-text>
                На этой странице вы можете изменить настройки своего профиля.
            </v-card-text>
        </v-card>
    </v-container>
</template>
<script>
import {mapGetters, mapActions} from 'vuex';

export default {
    name: 'ProfilePage',
    middleware: 'auth',

    computed: {
        ...mapGetters({
            user: 'user/user'
        })
    },
    methods: {
        ...mapActions({
            getMe: 'user/getMe'
        })
    }
};
</script>
