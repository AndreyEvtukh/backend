import baseConfig from './eslint.base.config.mjs';
import { htmlTemplateRules, typescriptRules } from './eslint.rules.mjs';
import angularTemplateParser from '@angular-eslint/template-parser';
import angularTemplatePlugin from '@angular-eslint/eslint-plugin-template';
import angularPlugin from '@angular-eslint/eslint-plugin';

export default [
    ...baseConfig,
    {
        ignores: [
            '**/dist/**',
            '**/*.js',
            '**/*.jsx',
            'apps/**/src/app/legacy/**/*.html',
            'libs/**/*.html',
        ],
    },
    {
        files: ['**/*.{ts,tsx,cts,mts,cjs,mjs}'],
        plugins: { '@angular-eslint': angularPlugin },
        rules: typescriptRules,
    },
    {
        files: ['**/*.html'],
        languageOptions: {
            parser: angularTemplateParser,
        },
        plugins: {
            '@angular-eslint/template': angularTemplatePlugin,
        },
        rules: htmlTemplateRules,
    },
];
