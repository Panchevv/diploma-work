/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
  root: true,
  'extends': [
    'plugin:vue/vue3-strongly-recommended',
    'eslint:recommended',
    '@vue/eslint-config-typescript',
  ],
  plugins: ["eslint-plugin-local-rules"],
  ignorePatterns: ["*.config.ts", "eslint-local-rules.js", ".eslintrc.cjs"],
  rules: {
    /** Strongly Recommended **/
    'vue/first-attribute-linebreak': ["warn", { 'singleline': "ignore", 'multiline': "ignore" }],
    'vue/html-closing-bracket-newline': ["error", { 'singleline': "never", 'multiline': "never" }],
    'vue/html-end-tags': "error",
    'vue/html-indent': ["error", 4, {
      'attribute': 1,
      'baseIndent': 0,
      'closeBracket': 0,
      'alignAttributesVertically': true,
    }],
    'vue/html-quotes': ["error", "double", { 'avoidEscape': true }],
    'vue/html-self-closing': ["warn", {
      'html': {
        'void': "always",
        'normal': "never", // Maybe always?
        'component': "always"
      },
      'svg': "always",
      'math': "always"
    }],
    'vue/max-attributes-per-line': "off",
    'vue/no-multi-spaces': ["error", {
      'ignoreProperties': true
    }],
    // 'vue/require-prop-types' - potention problems with Typescript
    'vue/singleline-html-element-content-newline': "off",
    'vue/v-on-event-hyphenation': ["error", "never"],

    /** Recommended **/
    'vue/attributes-order': ["warn", {
      'order': [
        "DEFINITION",
        "CONDITIONALS",
        "LIST_RENDERING",
        "RENDER_MODIFIERS",
        "UNIQUE",
        "GLOBAL",
        "SLOT",
        "TWO_WAY_BINDING",
        "OTHER_DIRECTIVES",
        "OTHER_ATTR",
        "EVENTS",
        "CONTENT"
      ],
      'alphabetical': false
    }],
    'vue/component-tags-order': ["error", {
      'order': ["docs", "template", "script", "style"]
    }],
    //'vue/no-lone-template': "warn", // may be useful. Can't really understand it fully
    'vue/no-multiple-slot-args': "warn",
    'vue/no-v-html': "warn",
    'vue/order-in-components': "warn",
    'vue/this-in-template': ["error", "never"],

    /** Other **/
    'vue/block-tag-newline': "warn",
    'vue/component-name-in-template-casing': ["error", "PascalCase"],
    'vue/component-options-name-casing': ["error", "PascalCase"],
    'vue/custom-event-name-casing': ["error", "camelCase"],
    'vue/define-emits-declaration': ["error", "type-based"],
    'vue/define-macros-order': ["warn", { 'order': ["defineOptions", "defineProps", "defineEmits", "defineSlots"] }],
    'vue/define-props-declaration': ["error", "type-based"],
    'vue/html-button-has-type': ["error", {
      'button': true,
      'reset': true,
      'submit': true
    }],
    'vue/match-component-file-name': ["warn", {
      'extensions': ["jsx", "tsx", "vue"],
      'shouldMatchCase': true
    }],
    'vue/no-bare-strings-in-template': ["warn", {
      'allowlist': [
        "(", ")", ",", ".", "&", "+", "-", "=", "*", "/", "#", "%", "!", "?", ":", "[", "]", "{", "}", "<", ">", "\u00b7", "\u2022", "\u2010", "\u2013", "\u2014", "\u2212", "|"
      ],
      'attributes': {
        '/.+/': ["title", "aria-label", "aria-placeholder", "aria-roledescription", "aria-valuetext"],
        'input': ["placeholder"],
        'img': ["alt"]
      },
      'directives': ["v-text"]
    }],
    'vue/no-ref-object-destructure': "warn",
    'vue/no-required-prop-with-default': "error",
    'vue/no-restricted-class': ["error", "container", "row", "col"],
    'vue/no-restricted-html-elements': ["error",
      "b", "i", "sub", "sup",
      "acronym", "applet", "basefont", "big", "center", "dir", "font", "frame", "frameset", "noframes", "strike", "tt" // deprecated HTML tags
    ],
    'vue/no-this-in-before-route-enter': "error",
    'vue/no-unsupported-features': ["error", {
      'version': "^3.3.0",
      'ignores': []
    }],
    'vue/no-unused-properties': ["warn", {
      'groups': ["props", "data", "computed", "methods", "setup"]
    }],
    'vue/no-unused-refs': "warn",
    'vue/no-useless-mustaches': "error",
    'vue/no-useless-v-bind': "error",
    'vue/no-v-text': "error",
    'vue/padding-line-between-blocks': ["error", "always"],
    'vue/prefer-define-options': "error",
    'vue/prefer-true-attribute-shorthand': "warn",
    'vue/require-direct-export': "error",
    'vue/require-expose': "error",
    'vue/require-macro-variable-name': ["warn", {
      "defineProps": "props",
      "defineEmits": "emit",
      "defineSlots": "slots",
      "useSlots": "slots",
      "useAttrs": "attrs"
    }],
    // 'vue/require-typed-object-prop': "error", // may become problematic. Change to warn in that case // Not released yet
    'vue/require-typed-ref': "error",
    'vue/script-indent': ["error", 4, {
      'baseIndent': 0,
      'switchCase': 1
    }],
    'vue/valid-define-options': "error",

    /** <template> rules **/
    'vue/array-bracket-spacing': ["warn", "always"],
    'vue/arrow-spacing': "warn",
    'vue/brace-style': ["warn", "1tbs", { 'allowSingleLine': true }],
    'vue/camelcase': "error",
    'vue/comma-dangle': ["warn", {
      'arrays': "always-multiline",
      'objects': "always-multiline",
      'imports': "always-multiline",
      'exports': "always-multiline",
      'functions': "ignore"
    }],
    'vue/comma-spacing': "warn",
    'vue/comma-style': ["warn", "last", {
      'exceptions': {
        'ArrayExpression': false,
        'ArrayPattern': false,
        'ArrowFunctionExpression': false,
        'CallExpression': false,
        'FunctionDeclaration': false,
        'FunctionExpression': false,
        'ImportDeclaration': false,
        'ObjectExpression': false,
        'ObjectPattern': false,
        'VariableDeclaration': false,
        'NewExpression': false
      }
    }],
    'vue/dot-location': ["warn", "object"],
    'vue/dot-notation': "warn",
    'vue/eqeqeq': ["error", "always", {
      'null': "ignore"
    }],
    'vue/func-call-spacing': "error",
    'vue/key-spacing': ["error", {
      'beforeColon': false,
      'afterColon': true,
      'mode': "minimum"
    }],
    'vue/keyword-spacing': "error",
    'vue/no-constant-condition': "error",
    'vue/no-empty-pattern': "error",
    'vue/no-irregular-whitespace': "warn",
    'vue/no-loss-of-precision': "warn",
    // 'vue/no-restricted-syntax': ["error", { }], // TODO: Configure
    'vue/no-useless-concat': "warn",
    'vue/object-curly-newline': ["error", {
      'multiline': true,
      'consistent': true
    }],
    'vue/object-curly-spacing': ["warn", "always"],
    'vue/object-shorthand': ["error", "properties", {
      'avoidQuotes': true
    }],
    'vue/operator-linebreak': ["error", "after", {
      'overrides': {}
    }],
    'vue/prefer-template': "error",
    'vue/space-in-parens': ["warn", "never"],
    'vue/space-infix-ops': ["error", { 'int32Hint': true }],
    'vue/template-curly-spacing': ["warn", "never"],

    /** JS/TS rules **/
    'array-bracket-spacing': ["warn", "always"],
    'arrow-spacing': "warn",
    'brace-style': ["warn", "1tbs", { 'allowSingleLine': true }],
    'camelcase': "warn",
    'comma-dangle': ["warn", {
      'arrays': "always-multiline",
      'objects': "always-multiline",
      'imports': "always-multiline",
      'exports': "always-multiline",
      'functions': "ignore"
    }],
    'comma-spacing': "warn",
    'comma-style': ["warn", "last", {
      'exceptions': {
        'ArrayExpression': false,
        'ArrayPattern': false,
        'ArrowFunctionExpression': false,
        'CallExpression': false,
        'FunctionDeclaration': false,
        'FunctionExpression': false,
        'ImportDeclaration': false,
        'ObjectExpression': false,
        'ObjectPattern': false,
        'VariableDeclaration': false,
        'NewExpression': false
      }
    }],
    'dot-location': ["warn", "object"],
    'dot-notation': "warn",
    'eqeqeq': ["error", "always", {
      'null': "ignore"
    }],
    'func-call-spacing': "off",
    'indent': ["error", 4],
    'key-spacing': ["error", {
      'beforeColon': false,
      'afterColon': true,
      'mode': "minimum"
    }],
    'keyword-spacing': "error",
    'no-constant-condition': ["error", {
      'checkLoops': false
    }],
    'no-empty-pattern': "error",
    'no-irregular-whitespace': "error",
    'no-loss-of-precision': "warn",
    // 'no-restricted-syntax': ["error", { }], // TODO: Configure
    'no-useless-concat': "warn",
    'object-curly-newline': ["error", {
      'multiline': true,
      'consistent': true
    }],
    'object-curly-spacing': ["warn", "always"],
    'object-shorthand': ["error", "properties", {
      'avoidQuotes': true
    }],
    'operator-linebreak': ["error", "after", {
      'overrides': {}
    }],
    'prefer-template': "error",
    'space-in-parens': ["warn", "never"],
    'space-infix-ops': ["error", { 'int32Hint': true }],
    'template-curly-spacing': ["warn", "never"],
    'no-var': "error",
    '@typescript-eslint/func-call-spacing': "error",

    /** Custom Rules **/
    "local-rules/object-keys-single-quotes": "error",
  },
  overrides: [
    {
      'files': ["*.vue"],
      'rules': {
        'indent': "off"
      }
    }
  ],
  parserOptions: {
    ecmaVersion: 'latest'
  }
}
