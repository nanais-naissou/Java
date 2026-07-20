package game;

import java.util.*;

public class Generator {
	private static final char WALL = '#', PATH = ' ', PLAYER = 'P';
	private static final char GHOST1 = 'G', GHOST2 = 'g';
	private static final char DOT = '.', APPLE = 'A', STRAWBERRY = 'S', POWER = 'O', PACGUM = 'C';

	private final int HEIGHT, WIDTH;
	private final int ghostCount;
	private final int ghostCols, ghostRows;
	int HOUSE_W, HOUSE_H;
	int houseX, houseY;
	public static char[][] STATIC_MAZE; 

	private final char[][] maze;
	private final Random rand = new Random();
	private final List<int[]> doors = new ArrayList<>();
	int NGhosts = 0;

	public Generator(int height, int width, boolean tore) {
		this.HEIGHT = height | 1;
		this.WIDTH = width | 1;
		this.ghostCount = height*width/100;
		this.ghostCols = Math.min(ghostCount, 5);
		this.ghostRows = (int) Math.ceil((double) ghostCount / ghostCols);
		this.HOUSE_W = ghostCols + 2;
		this.HOUSE_H = ghostRows + 2;

		this.maze = new char[HEIGHT][WIDTH];

		buildEmptyMaze();
		carveMazeDFS(1, 1);
		mirrorMaze();
		if (tore) {
        openToroidalCorridors();
		}
		placeGhostHouse();
		placeGhosts();
		connectGhostHouseToMaze();
		placePlayerFarFromGhostHouse();
		placeItems();

		if (!ensureConnectivity()) {
			System.out.println("⚠ Maze not fully connected, regeneration may be needed.");
		}
	}

	private void buildEmptyMaze() {
		for (int y = 0; y < HEIGHT; y++)
			Arrays.fill(maze[y], WALL);
	}

	private void carveMazeDFS(int x, int y) {
		int[] dx = { 0, 0, 2, -2 };
		int[] dy = { -2, 2, 0, 0 };
		List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
		Collections.shuffle(dirs, rand);

		maze[y][x] = PATH;

		for (int dir : dirs) {
			int nx = x + dx[dir], ny = y + dy[dir];
			if (isInBounds(nx, ny) && maze[ny][nx] == WALL) {
				maze[y + dy[dir] / 2][x + dx[dir] / 2] = PATH;
				carveMazeDFS(nx, ny);
			}
		}
	}

	private boolean isInBounds(int x, int y) {
		return x > 0 && x < WIDTH / 2 && y > 0 && y < HEIGHT - 1;
	}

	private void mirrorMaze() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH / 2; x++) {
				maze[y][WIDTH - 1 - x] = maze[y][x];
			}
		}
	}

	private void openToroidalCorridors() {
		for (int y = 1; y < HEIGHT - 1; y++) {
			if (maze[y][1] == PATH && maze[y][WIDTH - 2] == PATH) {
				maze[y][0] = PATH;
				maze[y][WIDTH - 1] = PATH;
			}
		}

		for (int x = 1; x < WIDTH - 1; x++) {
			if (maze[1][x] == PATH && maze[HEIGHT - 2][x] == PATH) {
				maze[0][x] = PATH;
				maze[HEIGHT - 1][x] = PATH;
			}
		}
	}

	private void placeGhostHouse() {
		houseX = WIDTH / 2 - HOUSE_W / 2;
		houseY = HEIGHT / 2 - HOUSE_H / 2;

		for (int y = 0; y < HOUSE_H; y++) {
			for (int x = 0; x < HOUSE_W; x++) {
				int gy = houseY + y, gx = houseX + x;
				maze[gy][gx] = (y == 0 || y == HOUSE_H - 1 || x == 0 || x == HOUSE_W - 1) ? WALL : PATH;
			}
		}

		int doorX = houseX + HOUSE_W / 2;
		int doorY = houseY + HOUSE_H - 1;
		maze[doorY][doorX] = PATH;
		doors.add(new int[] { doorY, doorX });
	}

	private void connectGhostHouseToMaze() {
		int doorX = houseX + HOUSE_W / 2;
		int doorY = houseY + HOUSE_H;

		int leftX = 1;
		while (maze[doorY][leftX] != PATH && leftX < doorX)
			leftX++;
		for (int x = Math.min(doorX, leftX); x <= Math.max(doorX, leftX); x++)
			maze[doorY][x] = PATH;

		int rightX = WIDTH - 2;
		while (maze[doorY][rightX] != PATH && rightX > doorX)
			rightX--;
		for (int x = Math.min(doorX, rightX); x <= Math.max(doorX, rightX); x++)
			maze[doorY][x] = PATH;
	}

	private void placeGhosts() {
		int placed = 0;
		for (int r = 0; r < ghostRows; r++) {
			for (int c = 0; c < ghostCols; c++) {
				if (placed >= ghostCount)
					return;
				int x = houseX + 1 + c;
				int y = houseY + 1 + r;
				if (maze[y][x] == PATH) {
					maze[y][x] = (placed < ghostCount / 2) ? GHOST1 : GHOST2;
					placed++;
				}
			}
		}
	}

	private void placePlayerFarFromGhostHouse() {
		int maxDist = 0, px = 1, py = 1;
		for (int y = 1; y < HEIGHT - 1; y++) {
			for (int x = 1; x < WIDTH - 1; x++) {
				if (maze[y][x] == PATH) {
					int dist = Math.abs(x - houseX) + Math.abs(y - houseY);
					if (dist > maxDist) {
						maxDist = dist;
						px = x;
						py = y;
					}
				}
			}
		}
		maze[py][px] = PLAYER;
	}

	private void placeItems() {
		List<int[]> paths = new ArrayList<>();
		for (int y = 1; y < HEIGHT - 1; y++)
			for (int x = 1; x < WIDTH - 1; x++)
				if (maze[y][x] == PATH && !isInGhostHouse(x, y))
					paths.add(new int[] { y, x });

		Collections.shuffle(paths, rand);
		int i = 0;

		int numGum = 20;
		int numApples = numGum / 2;
		int numStraw = numGum / 2;

		for (int j = 0; j < numGum && i < paths.size(); j++, i++)
			maze[paths.get(i)[0]][paths.get(i)[1]] = PACGUM;
		for (int j = 0; j < numApples && i < paths.size(); j++, i++)
			maze[paths.get(i)[0]][paths.get(i)[1]] = APPLE;
		for (int j = 0; j < numStraw && i < paths.size(); j++, i++)
			maze[paths.get(i)[0]][paths.get(i)[1]] = STRAWBERRY;
		for (; i < paths.size(); i++)
			maze[paths.get(i)[0]][paths.get(i)[1]] = DOT;
	}

	private boolean isInGhostHouse(int x, int y) {
		return x >= houseX && x < houseX + HOUSE_W && y >= houseY && y < houseY + HOUSE_H;
	}

	private boolean ensureConnectivity() {
		boolean[][] visited = new boolean[HEIGHT][WIDTH];
		Queue<int[]> queue = new LinkedList<>();

		outer: for (int y = 1; y < HEIGHT - 1; y++)
			for (int x = 1; x < WIDTH - 1; x++)
				if (maze[y][x] == PLAYER) {
					queue.add(new int[] { y, x });
					visited[y][x] = true;
					break outer;
				}

		int count = 0;
		Set<Character> walkable = Set.of(PATH, PLAYER, DOT, PACGUM, APPLE, STRAWBERRY, POWER);
		while (!queue.isEmpty()) {
			int[] curr = queue.poll();
			count++;
			for (int[] d : List.of(new int[] { 1, 0 }, new int[] { -1, 0 }, new int[] { 0, 1 }, new int[] { 0, -1 })) {
				int ny = curr[0] + d[0], nx = curr[1] + d[1];
				if (ny >= 0 && ny < HEIGHT && nx >= 0 && nx < WIDTH && !visited[ny][nx]) {
					if (walkable.contains(maze[ny][nx])) {
						visited[ny][nx] = true;
						queue.add(new int[] { ny, nx });
					}
				}
			}
		}

		int total = 0;
		for (int y = 1; y < HEIGHT - 1; y++)
			for (int x = 1; x < WIDTH - 1; x++)
				if (walkable.contains(maze[y][x]))
					total++;
		return count == total;
	}

	public char[][] getMaze() {
		return maze;
	}

	public List<int[]> getDoors() {
		return doors;
	}

	public int countDots() {
		int count = 0;
		for (char[] row : maze)
			for (char c : row)
				if (c == DOT)
					count++;
		return count;
	}

	public int countFruits() {
		int count = 0;
		for (char[] row : maze)
			for (char c : row)
				if (c == APPLE || c == STRAWBERRY)
					count++;
		return count;
	}

	public void printMaze() {
		for (char[] row : maze) {
			for (char c : row)
				System.out.print(c);
			System.out.println();
		}
	}
}
