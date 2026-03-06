# Exercice 13 - API de gestion des produits avec stock

## Description
Cette API reactive (Spring WebFlux + R2DBC) permet de gerer un catalogue de produits avec gestion du stock.

## Endpoints
- `GET /api/products` : retourne tous les produits.
- `GET /api/products/{id}` : retourne un produit par ID.
- `POST /api/products` : cree un nouveau produit.
- `PUT /api/products/{id}` : met a jour un produit.
- `DELETE /api/products/{id}` : supprime un produit.
- `GET /api/products/search?name=phone` : recherche par nom.
- `PUT /api/products/{id}/buy?quantity=5` : reduit le stock apres achat.

## Exemples JSON
### Creer un produit (`POST /api/products`)
```json
{
  "name": "Phone X",
  "price": 799.99,
  "stock": 20
}
```

### Mettre a jour un produit (`PUT /api/products/{id}`)
```json
{
  "name": "Phone X Pro",
  "price": 899.99,
  "stock": 15
}
```

## Controle du stock (challenge)
- Si `quantity` <= 0 : reponse `400 Bad Request`.
- Si le produit n'existe pas : reponse `404 Not Found`.
- Si `quantity` depasse le stock disponible : reponse `409 Conflict` avec message `Insufficient stock`.

## Fichier SQL
Le schema SQL global du projet est dans :
- `src/main/resources/schema.sql`

La table `products` y est definie et creee automatiquement au demarrage avec `spring.sql.init.mode=always`.
