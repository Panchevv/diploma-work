'use strict';

module.exports = {
    'disallow-identifiers': {
        meta: {
            docs: {
                description: 'disallow identifiers',
                category: 'Possible Errors',
                recommended: false,
            },
            schema: [],
        },
        create: function(context) {
            return {
                Identifier: function(node) {
                    context.report({
                        node,
                        message: 'Identifiers not allowed for Super Important reasons.',
                    });
                },
            };
        },
    },
    'object-keys-single-quotes': {
        meta: {
            docs: {
                description: "Forbid usage of double quotes for object keys",
                category: "Layout & Formatting",
                recommended: false,
            },
            schema: [],
        },
        create(context) {
            return {
                ObjectExpression(node) {
                    node.properties.forEach(property => {
                        const keyRawValue = property?.key?.raw
                        if (keyRawValue?.startsWith('"')) {
                            context.report({
                                message: 'Object keys cannot be enclosed in double quotes. Use single quotes or no quotes.',
                                node,
                            })
                        }
                    })
                },
            }
        },
    },
};
