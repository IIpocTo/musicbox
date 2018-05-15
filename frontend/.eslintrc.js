module.exports = {
    root: true,
    parserOptions: {
        parser: 'babel-eslint',
        sourceType: 'module'
    },
    env: {
        browser: true,
        node: true
    },
    extends: [
        'standard',
        'plugin:vue/strongly-recommended'
    ],
    // required to lint *.vue files
    plugins: [
        'vue'
    ],
    // add your custom rules here
    rules: {
        'arrow-parens': 0,
        'curly': 0,
        // allow async-await
        'generator-star-spacing': 0,
        'indent': ['error', 4],
        // allow debugger during development
        'no-debugger': process.env.NODE_ENV === 'production' ? 2 : 0,
        'no-multiple-empty-lines': [2, { max: 2, maxEOF: 1, maxBOF: 0 }],
        'one-var': 0,
        'semi': ['error', 'always'],
        'space-before-function-paren': ['error', {
            'anonymous': 'always',
            'named': 'never',
            'asyncArrow': 'always'
        }],

        'vue/attributes-order': 'off',
        'vue/order-in-components': 'off',
        'vue/html-self-closing': 'off',
        'vue/max-attributes-per-line': ['warn', {
            'singleline': 7,
            'multiline': {
                'max': 5,
                'allowFirstLine': false
            }
        }],
        'vue/html-indent': ['warn', 4, {
            'attribute': 1,
            'closeBracket': 0,
            'alignAttributesVertically': false,
            'ignores': []
        }],
        'vue/require-default-prop': 'off',
        'vue/require-prop-types': 'off'
    },
    globals: {}
}
