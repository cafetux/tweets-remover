# Tweets remover

Ce petit programme en java permet de supprimer ses tweets et retweets, avec des politiques différentes.

Les threads (suite de tweets où on se répond à soit même) sont gérés comme un seul tweet.

Le point d'entrée est la classe Main, qui scrolle tout les tweets du compte configuré, puis les tries/regroupe par Thread si besoin, puis supprime si besoin.

Il n'est pas impossible que sur un compte assez actifs/ancien les premiers lancement échouent pour des raisons de quotéq d'appel.

## Configuration

Il est nécessaire de renseigner les informations de connection à twitter dans le fichier twitter4j.properties

```
oauth.consumerKey=
oauth.consumerSecret=
oauth.accessToken=
oauth.accessTokenSecret=
```

## Politiques par défaut

Les tweets sont supprimés après 6 jours, avec un sursis de 2 jours de plus par engagement (like+RT). Ils sont immunisés au bout de 30 engagements.

Les retweets (un RT a son propre status ID indépendament du tweet retweeté) sont supprimés au bout d'1 mois.

