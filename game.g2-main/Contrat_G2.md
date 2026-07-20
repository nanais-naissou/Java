# Contrat G2 : PAC-MAN


## Description Générale

* Jeu en 2D 
* [x] avec une carte pseudo-aléatoire
   * [x] générateur de labyrinthes

    c’est-à-dire qu’une nouvelle carte est générée de manière aléatoire à chaque exécution du jeu. 


## Entités du jeu

- **Player** (joueur) : trois vies
- **Bot** (fantômes) 
- [?] **Projectile** (ou flèche) 
- **Coins** (pièces à collecter) 
- Pac Gum
- **Mur** (obstacles fixes) 

## Conditions de victoire et de défaite

- **Victoire** : Le joueur gagne lorsqu’il a collecté toutes les pièces présentes sur la carte. 
- **Défaite** : Le joueur perd après trois morts. Il meurt chaque fois qu’il entre en contact avec un fantôme. 

## Actions possibles

### Player :
- `Move()` : déplacement 
- [?] `Shoot()` : tir de projectiles 
- `Turn()` : changement de direction 
- `Collect()` : ramassage de coins 


### Bot (Fantôme) :
- [x] `GhostPlayer()` : suit le joueur 
- [x] `Wandering()` : déplacement aléatoire 
- [x] `WallFollowing()` : suit les murs
- [x] reste loin de PacMan et l'attaque si PacMan est proche 

### Projectile :
- [?] `Move()` : avance dans une direction 

### Coins :
- `Disappear()` : disparition après collecte 

### Pac Gums

Quand PacMan mange une PacGum 
- [x] les fantômes deviennent vulnérables pour un temps limité
- [x] ils changent de couleurs
- [x] ils fuient PacMan
  * changement de bot 
- [?] PacMan claque des dents plus vite
 

[x] Quand un fantôme meurt
- il se transforme en juste les yeux
  * changement avatar
- les yeux rejoingnent la base d'où sortent les fantômes
  * changement de bot
- il reprend son comportement normal
__explication technique dans la vidéo__ :
  * changement avatar
  * changement de stunt
  * changement de bot

## Nombre d'entités par case

* [x] au moins 4 entités par case
  * coins + fantômes + yeux + PacMan


## Fonctionnalité de zoom

* si Zoom=1, on voit toute la map
* `+` : zoom avant centré sur le joueur, la caméra le suit 
* `-` : zoom arrière 
* `=` : réinitialisation du zoom 

* [?] Le joueur est toujours suivi par la caméra.


## Extensions possibles

### [] Menu de sélection de difficulté (au lancement du jeu)

#### [?] Mode Easy
- Fantômes **bleus** 
- Vitesse **faible** 
- Déplacement **naïf** = aléatoire
- **1 point de vie** : un seul projectile suffit pour les éliminer 

#### [?] Mode Hard
- Fantômes **rouges** : 
  * plus malin 
- Vitesse **moyenne**, proche de celle du joueur 
- **3 points de vie** : trois projectiles nécessaires pour les éliminer 

### Types de projectiles du player (disponibles dans tous les modes)

- [] **Missile** : suit le fantôme pendant 5 secondes, puis disparaît 
- [?] **Flèche** : se déplace en ligne droite selon la direction initiale, et disparaît en cas de collision avec un mur

### [] Option Tore selon le Menu

* [x] Tore or not Tore : Le mode **tore** est possible (la carte est connectée sur ses bords).

### BOT

* [] bot Java
* [x] bot FSM
* [] bot GAL (Parser + Visitor)

* [x] le player a un bot qui collecte les coins et les PacGums

### Grille et (x,y) metric 

* [x] PacMan en continu ou intermitent (flag)
* [x] PacMan est en metric


# ENGINE

## Model

#### Geometry
  * [x] Tore 
  * [x] not Tore
  * [?] can change de map size (nbcols, nbrows)
  * [ ] can change cell size (en unité métrique)

#### Collisions 
  * [4] entité(s) par cellule  
  * [ ] bounding box (visibles en mode debug)

#### Show 
  * [x] birth (toutes les xx secondes)
  * [?] health
  * [x] death

#### Adaptability = Dynamic change 
  * [x] of stunt 
  * [x] of avatar (changement d'image) 
  * [x] of bot/FSM

#### Show that actions take time

  * [x] bots do not think while the stunt acts
  
#### Single / multiple actions?

  * [x] one action at a time 
    * [ ] compatibility table per entity
  * [x] action abort : move for example
    * un fantôme se déplace et se fait manger par PacMan

## View
  * [x] view port 
    * [x] zoom
    * [x] translate (scrolling)
  * [ ] drawing happens in model coordinates
  
#### Two modes
  * [ ] full map fits the canvas
    * [ ] en hauteur
  * [x] cell-size is given pixels

#### Performance (mode debug)

  * [x] show elapsed tick (min, max, moyenne)
    * [ ] with the detail of elapsed time spent in bots
  * [x] show elapsed time of a paint (min, max, moyenne)
  * [x] number of FPS (min, max, moyenne)
  * [x] gestion de la charge
    * [x] maximum de fantômes (soit en mode vulnérables, soit PacMan invulnérables) 
    * [?] large model

#### Parser GAL

 * [ ] Bot en Java
 * [x] Bot en FSM Java
 * [] Bot en GAl + parser

