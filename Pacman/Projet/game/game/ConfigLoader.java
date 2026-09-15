package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigLoader {

	public static boolean loadMode() {
		try (BufferedReader reader = new BufferedReader(new FileReader("Config.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().toLowerCase().startsWith("mode:")) {
					String value = line.split(":")[1].trim().toLowerCase();
					return value.equals("hard"); // true = hard mode
				}
			}
		} catch (IOException e) {
			System.err.println("Config.txt not found or unreadable. Defaulting to EASY mode.");
		}

		System.out.println("No mode found in config.txt. Defaulting to EASY.");

		return false; // default to easy
	}

	public static boolean loadTore() {
		try (BufferedReader reader = new BufferedReader(new FileReader("Config.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().toLowerCase().startsWith("tore:")) {
					String value = line.split(":")[1].trim().toLowerCase();
					return value.equals("true");
				}
			}
		} catch (IOException e) {
			System.err.println("Config.txt not found or unreadable. Defaulting to TORE = false.");
		}

		System.out.println("No Tore setting found in config.txt. Defaulting to TORE = false.");
		return false;
	}
}
