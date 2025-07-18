package sh.brisca.serverBrowser;

// import java.net.InetAddress;
// import java.net.UnknownHostException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.brisca.serverBrowser.handlers.JoinPrivateGameHandler;

public class EnvironmentVariable {
	public static int BROWSER_PORT;
	public static String BROWSER_HOSTNAME;
	public static String BROWSER_GAME_SPAWNER;
	public static String BROWSER_GAME_HEALTHCHECK;
	public static int BROWSER_GAME_PORT_RANGE_START;
	public static int BROWSER_GAME_PORT_RANGE_COUNT;
	public static int BROWSER_TARGET_SERVER_POOL;
	public static String RECORDING_DIR;
	public static String HOSTNAME;

	private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

	static void load() {
		String portString = Optional
				.ofNullable(System
						.getenv("BROWSER_PORT"))
				.orElse("9000");

		logger.info("Using port: {}", portString);
		try {
			BROWSER_PORT = Integer.parseInt(portString);
		} catch (NumberFormatException e) {
			logger.error("Error parsing BROWSER_PORT env variable to int:");
			logger.error("BROWSER_PORT={}", portString);
			logger.error("{}", e);
			throw new IllegalStateException("Env variable BROWSER_PORT must be an int.");
		}

		BROWSER_HOSTNAME = Optional
				.ofNullable(System
						.getenv("BROWSER_HOSTNAME"))
				.orElse("0.0.0.0");

		BROWSER_GAME_SPAWNER = Optional
				.ofNullable(System
						.getenv("BROWSER_GAME_SPAWNER"))
				.orElse("/app/spawner");
		// TODO: Write a validator that checs the file exists and is an executable.

		BROWSER_GAME_HEALTHCHECK = Optional
				.ofNullable(System.getenv("BROWSER_GAME_HEALTHCHECK"))
				.orElse("/app/healthCheck");
		// TODO: Write a validator that checs the file exists and is an executable.

		BROWSER_GAME_PORT_RANGE_START = Integer
				.parseInt(Optional
						.ofNullable(System
								.getenv("BROWSER_GAME_PORT_RANGE_START"))
						.orElse("9010"));
		// TODO: Write a validator

		BROWSER_GAME_PORT_RANGE_COUNT = Integer
				.parseInt(Optional
						.ofNullable(System
								.getenv("BROWSER_GAME_PORT_RANGE_COUNT"))
						.orElse("40"));
		// TODO: Write a validator

		BROWSER_TARGET_SERVER_POOL = Integer
				.parseInt(Optional
						.ofNullable(System
								.getenv("BROWSER_TARGET_SERVER_POOL"))
						.orElse("5"));

		RECORDING_DIR = Optional
				.ofNullable(System
						.getenv("RECORDING_DIR"))
				.orElse("/app/recordings");
		// TODO: Write a validator

		HOSTNAME = "browser";
		// try {
		// HOSTNAME = InetAddress.getLocalHost().getHostName();
		// } catch (UnknownHostException e) {
		// e.printStackTrace();
		// }
	}

}
