import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vuetify from 'vite-plugin-vuetify'
import svgLoader from 'vite-svg-loader'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue({
      script: {
        defineModel: true
      }
    }),
    vuetify({
      styles: {
        configFile: fileURLToPath(new URL('./src/assets/styles/settings.scss', import.meta.url))
      }
    }),
    svgLoader(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  preview: {
    port: 3000
  },
  server: {
    port: 3000,
    proxy: {
      '/graphql': {
        target: "localhost:8080/graphql",
        changeOrigin: true,
        ws: true
      },
      '/keycloak.json': {
        target: "localhost:8085/",
        changeOrigin: true,
        ws: true
      }
    }
  },
})
