# Lost & Found Microservice — CampusFix

Microservice Spring Boot pour la gestion des **objets perdus et trouvés** sur le campus CampusFix.

---

## Fonctionnalités

- **Déclarations** : Déclarer un objet perdu (`PERDU`) ou signaler un objet trouvé (`TROUVE`)
- **Réclamations** : Réclamer la propriété d'un objet trouvé
- **Workflow automatique** : Quand une réclamation est acceptée, la déclaration devient `RESOLU` et les autres réclamations sont automatiquement rejetées
- **Filtres & Pagination** : Filtrer par type, catégorie, statut
- **Intégration inter-services** : Feign clients vers User, Room, Event MS avec mode mock configurable

---

## Prérequis

- Java 17
- Maven (via le wrapper inclus `mvnw`)
- **XAMPP avec MySQL** démarré

---

## Démarrage rapide

### Ordre de démarrage recommandé

1. **MySQL (XAMPP)** : Ouvrir le panneau de contrôle XAMPP et démarrer **MySQL** (port `3306`). La base `lostfound_db` est créée automatiquement au premier démarrage.
2. **Eureka Server** : Démarrer le serveur de découverte Eureka sur le port `8761` (`http://localhost:8761/eureka`).
3. **Microservice `lost-found`** : Lancer ce microservice sur le port `8085`.

```bash
# Depuis le répertoire lost-found/
./mvnw spring-boot:run
```

Ou sous Windows :

```cmd
mvnw.cmd spring-boot:run
```

L'application démarre sur le **port 8085** et s'enregistre auprès d'Eureka.

### Lancer une seconde instance (multi-instance)

Pour démarrer une seconde instance en parallèle (par exemple sur le port `8095`) :

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8095"
```

Ou depuis le JAR compilé :

```bash
java -jar target/lost-found-0.0.1-SNAPSHOT.jar --server.port=8095
```

### Actuator & Monitoring

- **Health** : `http://localhost:8085/actuator/health` *(inclut `db` et le statut personnalisé `integration`)*
- **Info** : `http://localhost:8085/actuator/info`
- **Metrics** : `http://localhost:8085/actuator/metrics`

### Accéder à Swagger UI

```
http://localhost:8085/swagger-ui.html
```

Documentation OpenAPI JSON :

```
http://localhost:8085/v3/api-docs
```

---

## Configuration

| Propriété | Valeur par défaut | Description |
|-----------|------------------|-------------|
| `server.port` | `8085` | Port du service |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/lostfound_db` | URL MySQL |
| `integration.mock-enabled` | `true` | Si `true`, les appels Feign sont simulés localement |
| `integration.user-service.url` | `http://localhost:8081` | URL du microservice User |
| `integration.room-service.url` | `http://localhost:8082` | URL du microservice Room |
| `integration.event-service.url` | `http://localhost:8084` | URL du microservice Event |

---

## Endpoints principaux

### Déclarations

| Méthode | URL | Description |
|---------|-----|-------------|
| `POST` | `/api/declarations` | Créer une déclaration (perdu ou trouvé) |
| `GET` | `/api/declarations` | Lister (filtres: `type`, `categorie`, `statut` + pagination) |
| `GET` | `/api/declarations/{id}` | Obtenir une déclaration |
| `PUT` | `/api/declarations/{id}` | Modifier une déclaration |
| `DELETE` | `/api/declarations/{id}` | Supprimer une déclaration |
| `PATCH` | `/api/declarations/{id}/statut` | Changer le statut |
| `GET` | `/api/declarations/{id}/stats` | Statistiques des réclamations |

### Réclamations

| Méthode | URL | Description |
|---------|-----|-------------|
| `POST` | `/api/reclamations` | Créer une réclamation |
| `GET` | `/api/reclamations/{id}` | Obtenir une réclamation |
| `PATCH` | `/api/reclamations/{id}/statut` | Accepter ou rejeter une réclamation |
| `GET` | `/api/declarations/{id}/reclamations` | Réclamations d'une déclaration |

### Par utilisateur

| Méthode | URL | Description |
|---------|-----|-------------|
| `GET` | `/api/users/{userId}/declarations` | Déclarations d'un utilisateur |
| `GET` | `/api/users/{userId}/reclamations` | Réclamations d'un utilisateur |

---

## Entités & Enums

- **Declaration** : `id`, `titre`, `description`, `categorie`, `typeDeclaration`, `statut`, `userId`, `salleId` *(optionnel, room-resource MS)*, `eventId` *(optionnel, event MS)*, `lieu`, `dateEvenement`, `imageUrl`, `createdAt`, `updatedAt`
- **Reclamation** : `id`, `declaration`, `userId`, `message`, `statut`, `dateReclamation`, `updatedAt` *(contrainte unique `(declaration_id, userId)`)*

### Enums

- **TypeDeclaration** : `PERDU`, `TROUVE`
- **StatutDeclaration** : `EN_ATTENTE`, `RESOLU`, `FERME`, `ARCHIVE`
- **CategorieObjet** : `ELECTRONIQUE`, `DOCUMENT`, `VETEMENT`, `CLE`, `SAC`, `BIJOU`, `LIVRE`, `AUTRE`
- **StatutReclamation** : `EN_ATTENTE`, `ACCEPTEE`, `REJETEE`

---

## Lancer les tests

```bash
./mvnw test
```

Ou sous Windows :

```cmd
mvnw.cmd test
```

---

## Build complet

```bash
./mvnw clean install
```
