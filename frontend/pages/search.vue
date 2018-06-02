<template>
    <div>
        <div>You entered query: {{ query }}</div>
        <div>This is a search page</div>
    </div>
</template>
<script>
// import connector from "../util/connector";

export default {
    name: 'SearchPage',
    beforeMount() {
        // connector().get('/search');/
    },
    computed: {
        query() {
            return this.$route.query.q;
        }
    }
};
</script>
