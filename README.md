# Campus-Fix

**Projet Web distribué – Architecture Microservices**  
**Équipe : 4SSA5 – Groupe 4 (6 étudiants)**

## Présentation

Campus-Fix est une plateforme universitaire permettant de gérer les signalements de maintenance, les interventions techniques, les clubs, les événements et les réservations de salles et de ressources.

## Technologies prévues

- **Backend :** Java / Spring Boot (API REST)
- **Frontend :** Angular
- **Communication :** REST et RabbitMQ
- **Sécurité :** Keycloak
- **Documentation API :** Swagger / OpenAPI
- **Déploiement :** Docker / Docker Compose

## Structure du dépôt

```text
campus-fix-4SSA5-g4/
├── Backend/
|   campus-fix/
│      ├── user/
│      ├── maintenance/
│      ├── intervention/
│      ├── club/
│      ├── event/
│      ├── room-resource/
│      └── notification/
├── Frontend/
├── Documentation/
│   ├── diagrams/
│   ├── documentation.docx
│   └── presentation.pptx
└── README.md
```

## Organisation Git

- **`main`** : branche stable.
- **`user`, `maintenance`, `intervention`, `club`, `event`, `room-resource`, `notification`, `frontend`** : branches de travail par composant.
- Intégration via **Pull Requests** et suivi des tâches sur **Trello**.

## Mise en route

Le projet est en **phase de conception**. Les commandes de démarrage des services Spring Boot, du frontend Angular et de Docker Compose seront ajoutées après intégration.

## Livrables

Code source, diagrammes, documentation, contrats d'API, tests et démonstration finale.
