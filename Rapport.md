# Rapport sur la création du site web de bibliothèque en ligne "Bookin"

## Création de l'utilitaire de téléchargement

### L'utilisation de l'API Gutendex

Pour télécharger un nombre important d'ouvrages du domaine publique et peupler notre base de données, il était important de créer un script permettant d'explorer les livres présents en ligne et accessible depuis des API. Pour notre cas, nous avons utilisé l'API [Gutendex](https://gutendex.com/) permettant d'interroger de manière efficace la base de donneés du projet Gutenberg pour récupérer des listes de livres en fonction de certains critères et de trouver les liens permettant de les télécharger.
Nous avons donc développer une application nommée `book_downloader` permettant de récupérer un nombre important de livres tout en créant une base de données en JSON contenant toutes les informations relatives aux livres téléchargés. Pour utiliser cette application, il faut lire la documentation y étant attaché, dans le fichier `README.md`, et il est nécessaire d'utiliser cette application pour peupler efficacement la base de données de Bookin.

## Création du frontend

### Utilisation du framework Vue.js

Dans l'objectif de créer une application web simple et de manière rapide, nous avons choisi d'utiliser le framework web Vue.js. Il permet de simplement d'ajouter des pages et des composants réutilisables (comme les cartes pour les livres) et de consommer l'API de Bookin de manière efficace et simple (à l'aide de la librairie Axios).

### Utilisation du framework CSS Materialize

Créer le front-end d'une application web est parfois très long et fastidieux dans l'UI et l'UX, c'est pour cela que nous avons utiliser le framework CSS Materialize. Ce dernier permet de facilement et rapidement un front-end élégant, mais surtout "responsive", ce qui signifie que le site web sera adapté aux affichages "ordinateur", mais aussi aux affichages "téléphone".

Grâce à ces deux framework, il a été très simple et rapide de créer la partie front de notre applcation web, ce qui est une bonne chose car l'intérêt de ce projet réside non pas dans la partie front, mais bien dans les algorithmes présents dans le backend.

## Création du backend

### La table d'indexage

Afin de pouvoir retrouver les documents et les livres de manière efficace et précise, il faut créer une table d'indexage qui se structure comme suit :

```
indexTable = {
    "word" : {BOOK_ID:COUNTER}
}
```

Grâce à cette structure, il est possible, pour chaque mot, de trouver tous les documents dans lequel il est présent avec le nombre d'occurence dans ce document. Cela nous est utile pour effectuer la distance de Jaccard entre les documents pour effectuer des opérations de classement et de suggestion.

Afin de ne pas avoir plus de dépendance externe, nous avons décidé d'utiliser une simple variable Java pour contenir la table d'indexage, elle est initialisé à chaque fois que l'on lance l'application, ce qui prend un peu de temps au démarrage du serveur web.

Une autre option que nous avons implémenté est le stockage de cette table d'indexage dans un SGBD "big-data", MongoDB. Il est possible de configurer la connexion vers le serveur Mongo et de l'activer ou non. Cependant, l'initalisation de la table d'indexage, bien que nécessaire seulement une fois contrairement à la méthode précédente, est beaucoup plus longue.

Le maintien à jour de la table d'indexage est assuré par les fonction d'ajout et de suppression de livre. Il suffit simplement, lors de l'ajout d'un livre par exemple, de parcourir son contenu et de compléter la table d'indexage en fonction de ce dernier.

### La fonctionnalité de recherche simple


### La fonctionnalité de recherche avancée


### Le tri des recherches


### La fontionnalité de suggestion
