<template>
    <div>Artist {{ artistId }}</div>
</template>
<script>
export default {
    name: 'ArtistPage',
    computed: {
        artistId() {
            return this.$router.currentRoute.params.id;
        }
    }
};
</script>
