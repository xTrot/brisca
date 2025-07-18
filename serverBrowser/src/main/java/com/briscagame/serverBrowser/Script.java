package com.briscagame.serverBrowser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briscagame.serverBrowser.handlers.JoinPrivateGameHandler;

public class Script {
    private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    public String cmd;
    public String in;
    public String out;
    public String err;
    public int exitCode;

    public static Script run(String cmd, String in) {
        int exitCode = 1;
        String out = "";
        String err = "";
        InputStream stdout = null;
        InputStream stderr = null;
        try {

            logger.debug("Trying: {}", cmd);
            Process process = new ProcessBuilder(cmd.split("\\s+")).start();

            if (in != null) {
                // Write to stdin (optional)
                OutputStream stdin = process.getOutputStream();
                stdin.write(in.getBytes());
                stdin.flush();
                stdin.close();
            }

            // Read stdout
            stdout = process.getInputStream();
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
            String line;
            while ((line = stdoutReader.readLine()) != null) {
                out += line + "\n";
            }
            logger.debug("Stdout: {}", out);
            stdout.close();

            // Read stderr
            stderr = process.getErrorStream();
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));
            while ((line = stderrReader.readLine()) != null) {
                err += line + "\n";
            }
            logger.debug("Stderr: {}", err);
            stderr.close();

            // Wait for the process to complete and get the return code
            exitCode = process.waitFor();
            logger.debug("Exit Code: {}", exitCode);

        } catch (IOException | InterruptedException e) {
            logger.error("{}", e);
        }

        Script rtn = new Script(cmd, in, out, err, exitCode);

        return rtn;
    }

    private Script(String cmd, String in, String out, String err, int exitCode) {
        this.cmd = cmd;
        this.in = in;
        this.out = out;
        this.err = err;
        this.exitCode = exitCode;
    }

}
