<template>
    <v-btn icon @click="like">
        <v-icon color="red">{{ icon }}</v-icon>
    </v-btn>
</template>
<script>
export default {
    name: 'LikeBtn',
    props: {
        value: {
            type: Boolean,
            default: false
        }
    },
    computed: {
        icon() {
            return this.value ? 'favorite' : 'favorite_border';
        }
    },
    methods: {
        like() {
            this.$emit('input', !this.value);
        }
    }
};
</script>
