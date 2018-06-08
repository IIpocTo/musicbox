<template>
    <v-container fluid grid-list-lg>
        <v-layout row wrap :justify-center="!items.length">
            <v-flex
                xs6 lg4 xl3
                v-for="(item, i) in items"
                :key="i"
            >
                <slot :item="item"></slot>
            </v-flex>
            <template v-if="!items.length && !noDataFullWidth">
                <slot name="no-data"><div>Нет данных</div></slot>
            </template>
            <template v-if="!items.length && noDataFullWidth">
                <v-flex xs12>
                    <slot name="no-data"><div>Нет данных</div></slot>
                </v-flex>
            </template>
        </v-layout>
    </v-container>
</template>
<script>
export default {
    name: 'GridList',
    props: {
        items: Array,
        noDataFullWidth: {
            type: Boolean,
            default: false
        }
    }
};
</script>
