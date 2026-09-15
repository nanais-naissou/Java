package oop.term;

public class Text {

    private char[][] grid;
    private int rows;
    private int cols;

    public Text(int rows, int cols) {
        this.grid = new char[rows][cols];
        this.rows = rows;
        this.cols = cols;
        initialisergrid(); // initialisation de la grille
    }

    // remplit tout le tableau avec '\0'
    public void initialisergrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = '\0';
            }
        }
    }

    // efface la ligne en mettant tout a '\0'
    public void clear(int row) {
        // Vérif simple
        if (row < 0) {
            return;
        }
        if (row >= rows) {
            return;
        }
        for (int j = 0; j < cols; j++) {
            grid[row][j] = '\0';
        }
    }

    // insère un carac et décale vers la droite
    public void inserer(int row, int col, char c) {
        // vérif en mode "tout séparé"
        if (row < 0) {
            return;
        }
        if (row >= rows) {
            return;
        }
        if (col < 0) {
            return;
        }
        if (col >= cols) {
            return;
        }
        // décale les carac de la fin vers col
        for (int j = cols - 1; j > col; j--) {
            grid[row][j] = grid[row][j - 1];
        }
        grid[row][col] = c;//remplace l'ancien carac par le nouveau
    }

    // vice versa supprime le carac et décale vers la gauche 
    public char delete(int row, int col) {
        if (row < 0) {
            return '\0';
        }
        if (row >= rows) {
            return '\0';
        }
        if (col < 0) {
            return '\0';
        }
        if (col >= cols) {
            return '\0';
        }
        char temp = grid[row][col];
        for (int j = col; j < cols - 1; j++) {
            grid[row][j] = grid[row][j + 1];
        }
        grid[row][cols - 1] = '\0';
        return temp;
    }

    // backspace supprime le carac à gauche
    public char backspace(int row, int col) {
        if (col <= 0) {
            return '\0';
        }
        if (row < 0) {
            return '\0';
        }
        if (row >= rows) {
            return '\0';
        }
        return delete(row, col - 1);
    }

    // renvoie le carac a la position donnée
    public char getChar(int row, int col) {
        if (row < 0) {
            return '\0';
        }
        if (row >= rows) {
            return '\0';
        }
        if (col < 0) {
            return '\0';
        }
        if (col >= cols) {
            return '\0';
        }
        return grid[row][col];
    }

    // change le carac a une position
    public void setChar(int row, int col, char c) {
        if (row < 0) return;
        if (row >= rows) return;
        if (col < 0) return;
        if (col >= cols) return;
        grid[row][col] = c;
    }

    // retourne le nombre de lignes
    public int getRows() {
        return rows;
    }

    // retourne le nombre de colonnes
    public int getCols() {
        return cols;
    }
}
