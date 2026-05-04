import { defineConfig } from 'vitepress'

const repoName = process.env.GITHUB_REPOSITORY?.split('/')[1]
const base = process.env.GITHUB_ACTIONS && repoName ? `/${repoName}/` : '/'

export default defineConfig({
  lang: 'es-CO',
  title: 'Sistema de Reserva de Ferry',
  description: 'Wiki del proyecto CSDT 2026: laboratorios, analisis tecnico y evidencia de calidad.',
  base,
  cleanUrls: true,
  ignoreDeadLinks: true,
  themeConfig: {
    logo: '/logo.svg',
    siteTitle: 'CSDT Ferry Wiki',
    search: {
      provider: 'local'
    },
    nav: [
      { text: 'Inicio', link: '/' },
      { text: 'Contexto', link: '/README' },
      { text: 'Primera Entrega', link: '/CSDT_PrimeraEntrega2026' },
      { text: 'Arquitectura', link: '/ArchitecturalSmells' }
    ],
    sidebar: [
      {
        text: 'Vision General',
        items: [
          { text: 'Portada', link: '/' },
          { text: 'Contexto original', link: '/README' }
        ]
      },
      {
        text: 'Laboratorios y Entregas',
        items: [
          { text: 'Lab 1: Code Smells y Refactoring', link: '/Code%20Smells%20and%20Refactoring%20Lab' },
          { text: 'Lab 2: Clean Code y XP', link: '/Clean%20Code%20and%20XP%20Practices%20Lab' },
          { text: 'Lab 3: DevEx y SPACE', link: '/DevEx%20and%20SPACE%20Lab' },
          { text: 'Primera Entrega 2026', link: '/CSDT_PrimeraEntrega2026' },
          { text: 'Lab 5: Deuda tecnica en procesos', link: '/Deuda%20tecnica%20en%20Procesos' },
          { text: 'Lab 6: Efecto Mariposa', link: '/Efecto%20Mariposa/Efecto%20Mariposa' },
          { text: 'Lab 7: Analisis Arquitectonico (Designite)', link: '/ArchitecturalSmells' }
        ]
      }
    ],
    socialLinks: [
      { icon: 'github', link: 'https://github.com/esteban0903/CSDT-Ferry-Reservation' }
    ],
    footer: {
      message: 'Curso CSDT 2026',
      copyright: 'Equipo: Esteban Aguilera Contreras, Carlos David Barrero, Julian Santiago Cardenas'
    }
  }
})
