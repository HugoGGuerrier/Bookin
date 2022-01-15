# Rapport sur la création du site web de bibliothèque en ligne "Bookin"

## Création de l'utilitaire de téléchargement

### L'utilisation de l'API Gutendex

Pour télécharger un nombre important d'ouvrages du domaine publique et peupler notre base de données, il était important de créer un script permettant d'explorer les livres présents en ligne et accessible depuis des API. Pour notre cas, nous avons utilisé l'API [Gutendex](https://gutendex.com/) permettant d'interroger de manière efficace la base de donneés du projet Gutenberg pour récupérer des listes de livres en fonction de certains critères et de trouver les liens permettant de les télécharger.
Nous avons donc développer une application nommée `book_downloader` permettant de récupérer un nombre important de livres tout en créant une base de données en JSON contenant toutes les informations relatives aux livres téléchargés. Pour utiliser cette application, il faut lire la documentation y étant attaché, dans le fichier `README.md`, et il est nécessaire d'utiliser cette application pour peupler efficacement la base de données de Bookin.

## Création du frontend

## Création du backend

### La table d'indexage

Afin de pouvoir retrouver les documents et les livres de manière efficace et précise, il faut créer une table d'indexage qui se structure comme suit :

```
indexTable = {
    "word" : {BOOK_ID:COUNTER}
}
```

Grâce à cette structure, il est possible, pour chaque mot, de trouver tous les documents dans lequel il est présent avec le nombre d'occurence dans ce document. Cela nous est utile pour effectuer la distance de Jaccard entre les documents pour effectuer des opérations de classement et de suggestion.

Afin de ne pas avoir plus de dépendance externe, nous avons décidé d'utiliser une simple variable Java pour contenir la table d'indexage, elle est initialisé à chaque fois que l'on lance l'application, ce qui prend un peu de temps au démarrage du serveur web. Il serait possible et pertinent d'utiliser un SGBD de type "big data" (comme MongoDB) pour stocker cette table d'indexage et ne pas avoir à la réinitialiser à chaque lancement de l'application.

### La fonctionnalité de recherche simple


### La fonctionnalité de recherche avancée


### Le tri des recherches


### La fontionnalité de suggestion
