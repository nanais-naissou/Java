
# G2:   PAC-MAN — README

###### BELAL Anais 

## Présentation du jeu

Notre projet est une ré-implémentation de `Pac-Man` en Java, jouable en 2D, avec génération aléatoire du labyrinthe à chaque partie, et plusieurs extensions : différents niveaux de difficulté, suivi du joueur par la caméra, et projectiles pour éliminer les fantômes.

* __But du jeu :__ Collecter toutes les pièces tout en évitant d’être tué par les fantômes pour remporter la partie.



## Lancement du jeu

1. **Choix de la difficulté et du mode**
   - Ouvrez le fichier `config.txt` disponible dans le répertoire du projet.

   - Sélectionnez la difficulté de jeu (`Easy` ou `Hard`) et le mode de carte (`tore` ou `non-tore`).


2. **Choix de la taille de la carte**
   - Ouvrez le fichier `MainTask3` dans le package `game`.
   - Choisissez la taille de la carte souhaitée (par défaut : `20x20`) ; le nombre de fantômes est proportionnel à la taille de la carte (par exemple, pour une carte 20x20 : 4 fantômes).

3. **Exécution du jeu**
   - Exécutez le fichier `MainTask3` pour lancer le jeu.



## Contrôles disponibles

| Touche                         | Action                                   |
|--------------------------------|------------------------------------------|
| `Flèches`       | Déplacer Pac-Man dans la direction choisie|
| `Shift` + `Flèche droite/gauche`   | Faire tourner Pac-Man sur place          |
| `F `                             | Activer / désactiver le mouvement continu|
| `Espace`                         | Tirer un projectile                      |
| `P`                              | Zoom avant (zoom in)                     |
| `M`                              | Zoom arrière (zoom out)                  |
| `=`                              | Réinitialiser le zoom                    |
| `Ctrl`                           | Afficher le mode debug (Grid, stats…)  |


############################ ENGLISH VERSION #########################################
# G2:   PAC-MAN — README

###### BELAL Anais 

## Game Overview

Our project is a Java re-implementation of `Pac-Man`, playable in 2D, featuring a randomly generated maze for each game session, along with several extensions: different difficulty levels, camera tracking of the player, and projectiles to eliminate ghosts.

* **Objective:** Collect all the coins while avoiding being killed by ghosts in order to win the game.



## Running the Game

1. **Difficulty and game mode selection**
   - Open the `config.txt` file located in the project directory.

   - Select the game difficulty (`Easy` or `Hard`) and the map mode (`tore` or `non-tore`).


2. **Map size selection**
   - Open the `MainTask3` file located in the `game` package.
   - Choose the desired map size (default: `20x20`). The number of ghosts is proportional to the map size (for example, a `20x20` map contains 4 ghosts).

3. **Launching the game**
   - Run the `MainTask3` file to start the game.



## Available Controls

| Key | Action |
|--------------------------------|------------------------------------------|
| `Arrow keys` | Move Pac-Man in the selected direction |
| `Shift` + `Left/Right Arrow` | Rotate Pac-Man without moving |
| `F` | Enable / disable continuous movement |
| `Space` | Shoot a projectile |
| `P` | Zoom in |
| `M` | Zoom out |
| `=` | Reset zoom level |
| `Ctrl` | Display debug mode (grid, statistics, etc.) |


