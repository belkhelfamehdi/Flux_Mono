# Flux Mono - Exercices WebFlux

Projet Spring Boot WebFlux avec R2DBC (H2 en memoire) contenant plusieurs exercices reactifs.

## Exercice 13 - API de gestion des produits avec stock

### Objectif
API reactive pour gerer les produits avec controle du stock.

### Modele Product
- `id` (Long): identifiant auto-genere
- `name` (String): nom du produit
- `price` (Double): prix
- `stock` (Integer): quantite disponible

### Endpoints
- `GET /api/products` : retourne tous les produits
- `GET /api/products/{id}` : retourne un produit par ID
- `POST /api/products` : cree un produit
- `PUT /api/products/{id}` : met a jour un produit
- `DELETE /api/products/{id}` : supprime un produit
- `GET /api/products/search?name=phone` : recherche par nom
- `PUT /api/products/{id}/buy?quantity=5` : reduit le stock apres achat

### Exemples JSON
Creation (`POST /api/products`)
```json
{
  "name": "Phone",
  "price": 799.99,
  "stock": 10
}
```

Mise a jour (`PUT /api/products/{id}`)
```json
{
  "name": "Phone Pro",
  "price": 999.99,
  "stock": 8
}
```

### Regle de stock
Si `quantity` depasse le stock disponible, l'API retourne:
- `409 Conflict`
- corps de reponse: `"Insufficient stock"`

### Base de donnees
Le schema SQL est dans `src/main/resources/schema.sql`.
La table `products` est creee au demarrage via `spring.sql.init.mode=always`.

### Test manuel (Postman/curl)
Exemples `curl`:

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Phone","price":799.99,"stock":10}'

curl http://localhost:8080/api/products

curl "http://localhost:8080/api/products/search?name=phone"

curl -X PUT "http://localhost:8080/api/products/1/buy?quantity=5"
```
