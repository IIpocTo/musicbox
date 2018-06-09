import * as i18n from '@/util/i18n';

export default function (propName) {
    return {
        computed: {
            genresTranslated() {
                if (!this[propName]) return [];
                let genres = this[propName].genres || [];
                if (this.short) genres = genres.slice(0, 3);
                return genres.map(g => i18n.t(g));
            }
        }
    };
};
