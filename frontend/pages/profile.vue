<template>
    <div>User profile {{ user && user.username }}</div>
</template>
<script>
import {mapGetters, mapActions} from 'vuex';

export default {
    name: 'ProfilePage',
    beforeMount() {
        this.getMe();
    },
    methods: {
        ...mapActions({
            getMe: 'user/getMe'
        })
    },
    computed: {
        ...mapGetters({
            user: 'user/user'
        })
    }
};
</script>
