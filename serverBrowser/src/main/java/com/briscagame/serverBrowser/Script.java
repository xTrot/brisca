package com.briscagame.serverBrowser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;

public class Script {
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

            System.out.println("Trying: " + cmd);
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
            System.out.println("Stdout:\n" + out);
            stdout.close();

            // Read stderr
            stderr = process.getErrorStream();
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));
            while ((line = stderrReader.readLine()) != null) {
                err += line + "\n";
            }
            System.out.println("Stderr:\n" + err);
            stderr.close();

            // Wait for the process to complete and get the return code
            exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
