export const typescriptRules = {
    '@angular-eslint/prefer-standalone': 'error',
    '@angular-eslint/directive-selector': [
        'error',
        { type: 'attribute', prefix: ['app', 'lib'], style: 'camelCase' }
    ],
    '@angular-eslint/component-selector': [
        'error',
        { type: 'element', prefix: ['app', 'lib'], style: 'kebab-case' }
    ],
    '@typescript-eslint/no-explicit-any': 'off',
    '@typescript-eslint/no-unused-vars': [
        'warn',
        { argsIgnorePattern: '^_', varsIgnorePattern: '^_' }
    ],
    'no-console': 'warn',
    'no-debugger': 'warn',
    indent: ['error', 4, { SwitchCase: 1 }],
    'quotes': 'off'
};

export const htmlTemplateRules = {
    '@angular-eslint/template/eqeqeq': 'warn',
    '@angular-eslint/template/no-negated-async': 'error',
    '@angular-eslint/template/banana-in-box': 'error'
};
