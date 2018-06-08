<template>
    <v-container>
        <div>User profile page: {{ user.username }}</div>
    </v-container>
</template>
<script>
import {mapGetters, mapActions} from 'vuex';

export default {
    name: 'ProfilePage',
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
