---
layout: home

hero:
  name: Sistema de Reserva de Ferry
  text: Wiki CSDT 2026
  tagline: Documentacion del semestre consolidada en GitHub Pages con VitePress.
  actions:
    - theme: brand
      text: Ver contexto base
      link: /README
    - theme: alt
      text: Ir a laboratorios
      link: /Code%20Smells%20and%20Refactoring%20Lab

features:
  - title: Contexto y punto de partida
    details: Proyecto base, arquitectura original, quickstart y endpoints del sistema.
    link: /README
  - title: Laboratorios de calidad
    details: Code smells, clean code, XP, DevEx/SPACE, deuda de procesos y reflexion de IA.
    link: /Clean%20Code%20and%20XP%20Practices%20Lab
  - title: Evidencia arquitectonica
    details: Resultados de Designite con impactos y recomendaciones de evolucion.
    link: /ArchitecturalSmells
---

## Equipo

- Esteban Aguilera Contreras
- Carlos David Barrero
- Julian Santiago Cardenas

## Ruta recomendada de lectura

1. Contexto original del proyecto: [docs/README.md](./README.md)
2. Lab 1 - Code Smells and Refactoring: [Code Smells and Refactoring Lab](./Code%20Smells%20and%20Refactoring%20Lab.md)
3. Lab 2 - Clean Code + XP: [Clean Code and XP Practices Lab](./Clean%20Code%20and%20XP%20Practices%20Lab.md)
4. Lab 3 - DevEx + SPACE: [DevEx and SPACE Lab](./DevEx%20and%20SPACE%20Lab.md)
5. Primera entrega 2026: [CSDT_PrimeraEntrega2026](./CSDT_PrimeraEntrega2026.md)
6. Lab 5 - Deuda tecnica en procesos: [Deuda tecnica en Procesos](./Deuda%20tecnica%20en%20Procesos.md)
7. Lab 6 - Efecto Mariposa: [Efecto Mariposa](./Efecto%20Mariposa/Efecto%20Mariposa.md)
8. Analisis de arquitectura con Designite: [ArchitecturalSmells](./ArchitecturalSmells.md)

## Repositorios

- Repositorio original: https://github.com/jaygajera17/E-commerce-project-springBoot
- Fork del equipo CSDT: https://github.com/esteban0903/CSDT-Ferry-Reservation

## Linea del tiempo

| Fecha       | Hito |
|-------------|------|
| 2026-02-10  | Se define el indice general y el inventario inicial de code smells. |
| 2026-02-22  | Se documenta analisis de Clean Code, buenas practicas y XP. |
| 2026-03-13  | Se integran SonarCloud, GitHub CodeQL y JaCoCo en CI. |
| 2026-03-21  | Se documenta analisis DevEx y SPACE con baseline de metricas. |
| 2026-03-29  | Se incrementa cobertura de pruebas e integra OWASP al workflow. |
| 2026-04-08  | Se agrega Lab 6 sobre Vibe Coding vs Spec-Driven Development. |
| 2026-04-16  | Se publica analisis de Architectural Smells con Designite. |

## Publicacion en GitHub Pages

Esta wiki se despliega automaticamente desde GitHub Actions cuando hay cambios en la rama principal.

- Configuracion del workflow: [.github/workflows/deploy-docs.yml](../.github/workflows/deploy-docs.yml)
- Comando local para visualizar: npm run docs:dev
